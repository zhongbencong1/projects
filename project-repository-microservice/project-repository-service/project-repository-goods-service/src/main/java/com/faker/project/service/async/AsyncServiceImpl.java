package com.faker.project.service.async;

import com.alibaba.fastjson.JSON;
import com.faker.project.constant.GoodsConstant;
import com.faker.project.entity.RepositoryGoods;
import com.faker.project.goods.GoodsInfo;
import com.faker.project.goods.SimpleGoodsInfo;
import com.faker.project.service.IAsyncService;
import com.faker.project.service.impl.IGoodsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 异步服务接口实现
 * */
@Slf4j
@Service
@Transactional
public class AsyncServiceImpl implements IAsyncService {

    @Autowired
    private IGoodsServiceImpl iGoodsService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 异步任务需要加上注解@Async, 并指定使用的线程池 getAsyncExecutor
     * 异步任务处理两件事:
     *  1. 将商品信息保存到数据表
     *  2. 更新商品缓存
     * */
    @Async("getAsyncExecutor")
    @Override
    public void asyncImportGoods(List<GoodsInfo> goodsInfos, String taskId) {
        log.info("async task running taskId: [{}]", taskId);
        StopWatch watch = StopWatch.createStarted(); // 计时
        // 1. 如果是 goodsInfo 中存在重复的商品, 不保存; 直接返回, 记录错误日志
        // 请求数据是否合法的标记
        boolean isIllegal = false;

        // 将商品信息字段 joint 在一起, 用来判断是否存在重复
        Set<String> goodsJointInfos = new HashSet<>(goodsInfos.size());
        // 过滤出来的, 可以入库的商品信息(规则按照自己的业务需求自定义即可)
        List<GoodsInfo> filteredGoodsInfo = new ArrayList<>(goodsInfos.size());

        // 走一遍循环, 过滤非法参数与判定当前请求是否合法
        for (GoodsInfo goods : goodsInfos) {
            // 基本条件不满足的, 直接过滤器
            if (goods.getPrice() <= 0 || goods.getSupply() <= 0) {
                log.info("goods info is invalid: [{}]", JSON.toJSONString(goods));
                continue;
            }
            // 组合商品信息
            String jointInfo = String.format("%s,%s,%s", goods.getGoodsCategory(), goods.getBrandCategory(), goods.getGoodsName());
            if (goodsJointInfos.contains(jointInfo)) {
                isIllegal = true;
            }

            // 加入到两个容器中
            goodsJointInfos.add(jointInfo);
            filteredGoodsInfo.add(goods);
        }

        // 如果存在重复商品或者是没有需要入库的商品, 直接打印日志返回
        if (isIllegal || CollectionUtils.isEmpty(filteredGoodsInfo)) {
            watch.stop();
            log.warn("import nothing: [{}]", JSON.toJSONString(filteredGoodsInfo));
            log.info("check and import goods done: [{}ms]", watch.getTime(TimeUnit.MILLISECONDS));
            return;
        }

        List<RepositoryGoods> ecommerceGoods = filteredGoodsInfo.stream()
                .map(RepositoryGoods::infoToGoods)
                .collect(Collectors.toList());
        List<RepositoryGoods> targetGoods = new ArrayList<>(ecommerceGoods.size());

        // 2. 保存 goodsInfo 之前先判断下是否存在重复商品
        ecommerceGoods.forEach(g -> {
            RepositoryGoods repositoryGoods = iGoodsService.findFirstByGoodsCategoryAndBrandCategoryAndGoodsName(
                    g.getGoodsCategory(), g.getBrandCategory(), g.getGoodsName()).orElse(null);
            if (Objects.nonNull(repositoryGoods)) {
                return;
            }
            targetGoods.add(g);
        });

        // 商品信息入库
        boolean saveSuccess = iGoodsService.saveBatch(targetGoods);

        if (saveSuccess) {
            // 将入库商品信息同步到 Redis 中
            saveNewGoodsInfoToRedis(targetGoods);
            log.info("save goods info to db and redis: [{}]", targetGoods.size());
            watch.stop();
            log.info("check and import goods success: [{}ms]", watch.getTime(TimeUnit.MILLISECONDS));
        } else {
            log.info("save goods failed");
        }
    }

    /**
     * 将保存到数据表中的数据缓存到 Redis 中
     * dict: key -> <id, SimpleGoodsInfo(json)> 存储简版信息
     * */
    private void saveNewGoodsInfoToRedis(List<RepositoryGoods> savedGoods) {
        // 由于 Redis 是内存存储, 只存储简单商品信息
        Map<String, String> id2JsonObject = savedGoods.stream()
                .map(RepositoryGoods::goodsToSimple)
                .collect(Collectors.toMap(s -> s.getId().toString(), JSON::toJSONString));

        // 保存到 Redis 中, ECOMMERCE_GOODS_DICT_KEY为第一层区分: 多个项目共同使用同一个redis时, 各自存各自的key
        redisTemplate.opsForHash().putAll(GoodsConstant.ECOMMERCE_GOODS_DICT_KEY, id2JsonObject);
    }
}
