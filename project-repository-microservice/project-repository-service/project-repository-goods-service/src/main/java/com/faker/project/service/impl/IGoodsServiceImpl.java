package com.faker.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.faker.project.common.TableId;
import com.faker.project.constant.GoodsConstant;
import com.faker.project.entity.RepositoryGoods;
import com.faker.project.goods.DeductGoodsInventory;
import com.faker.project.goods.GoodsInfo;
import com.faker.project.goods.SimpleGoodsInfo;
import com.faker.project.mapper.GoodsServiceMapper;
import com.faker.project.service.IGoodsService;
import com.faker.project.vo.PageSimpleGoodsInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 商品微服务相关服务功能实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class IGoodsServiceImpl extends ServiceImpl<GoodsServiceMapper, RepositoryGoods> implements IGoodsService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public List<GoodsInfo> getGoodsInfoByTableId(TableId tableId) {
        // 详细的商品信息, 不能从 redis cache 中去拿
        Set<Long> ids = tableId.getIds().stream()
                .map(TableId.Id::getId)
                .collect(Collectors.toSet());
        log.info("get goods info by ids: [{}]", JSON.toJSONString(ids));

        List<RepositoryGoods> goodsList = this.listByIds(ids);
        return goodsList.stream().map(RepositoryGoods::goodsToInfo).collect(Collectors.toList());
    }

    /** 分页不能从 redis cache 中去拿 */
    @Override
    public PageSimpleGoodsInfo getSimpleGoodsInfoByPage(int pageNum) {
        pageNum = Math.max(pageNum, 1);// 默认是第一页

        // 这里分页的规则(你可以自由修改): 1页10调数据, 按照 id 倒序排列
        Page<RepositoryGoods> page = new Page<>(pageNum - 1, 10);
        List<SimpleGoodsInfo> simpleGoodsInfos = this.page(page).getRecords()
                .stream().map(RepositoryGoods::goodsToSimple).collect(Collectors.toList());

        // 是否还有更多页: 总页数是否大于当前给定的页
        boolean hasMore = page.getTotal() > page.getCurrent() * page.getSize();
        return new PageSimpleGoodsInfo(simpleGoodsInfos, hasMore);
    }

    /** 获取商品的简单信息, 可以从 redis cache 中去拿, 拿不到需要从 DB 中获取并保存到 Redis 里面 */
    @Override
    public List<SimpleGoodsInfo> getSimpleGoodsInfoByTableId(TableId tableId) {
        // 查询redis中缓存的SimpleGoodsInfo
        List<Object> goodIds = tableId.getIds().stream().map(i -> i.getId().toString()).collect(Collectors.toList());
        List<Object> cachedSimpleGoodsInfos = redisTemplate.opsForHash()
                .multiGet(GoodsConstant.ECOMMERCE_GOODS_DICT_KEY, goodIds)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 1. Redis 没有命中任何id
        if (CollectionUtils.isEmpty(cachedSimpleGoodsInfos)) {
            return queryGoodsFromDBAndCacheToRedis(tableId);
        }

        // 2. Redis 命中全部id
        if (cachedSimpleGoodsInfos.size() == goodIds.size()) {
            log.info("get simple goods info by ids (from cache): [{}]", JSON.toJSONString(goodIds));
            return parseCachedGoodsInfo(cachedSimpleGoodsInfos);
        }

        // 3. Redis 命中部分id, 需要获取全部: redis中缓存 + DB查询
        if (cachedSimpleGoodsInfos.size() < goodIds.size()) {
            // 一半从数据表中获取 , 一半从 redis cache 中获取
            List<SimpleGoodsInfo> redisCache = parseCachedGoodsInfo(cachedSimpleGoodsInfos);
            Set<String> redisCacheIds = redisCache.stream()
                    .map(SimpleGoodsInfo::getId).map(Object::toString).collect(Collectors.toSet());
            // 取差集: 传递进来的参数ids - 缓存中查到的ids = 需要查询的ids
            List<Long> needQueryIds = tableId.getIds().stream()
                    .map(TableId.Id::toString).filter(s -> !redisCacheIds.contains(s))
                    .distinct()
                    .map(Long::valueOf).collect(Collectors.toList());

            // 缓存中没有的, 查询数据表并缓存
            List<SimpleGoodsInfo> queryByDB = queryGoodsFromDBAndCacheToRedis(
                    new TableId(needQueryIds.stream().map(TableId.Id::new).collect(Collectors.toList())));
            // 合并 并返回
            log.info("get simple goods info by ids (from db and cache): [{}]", JSON.toJSONString(queryByDB));
            return new ArrayList<>(CollectionUtils.union(redisCache, queryByDB));
        }

        log.warn("get SimpleGoodsInfo By TableId happen error, method param is [{}]", tableId);
        return new ArrayList<>();
    }

    /** 将redis缓存中的数据反序列化成 Java Pojo List<SimpleGoodsInfo> 对象 */
    private List<SimpleGoodsInfo> parseCachedGoodsInfo(List<Object> cachedSimpleGoodsInfo) {
        return cachedSimpleGoodsInfo.stream()
                .map(s -> JSON.parseObject(s.toString(), SimpleGoodsInfo.class))
                .collect(Collectors.toList());
    }

    /** 从DB中查询数据, 并缓存到 Redis 中 */
    private List<SimpleGoodsInfo> queryGoodsFromDBAndCacheToRedis(TableId tableId) {
        // 从数据表中查询数据并做转换
        List<Long> ids = tableId.getIds().stream().map(TableId.Id::getId).distinct().collect(Collectors.toList());
        log.info("get simple goods info by ids (from db): [{}]", JSON.toJSONString(ids));
        List<RepositoryGoods> goodsList = this.listByIds(ids);
        List<SimpleGoodsInfo> result = goodsList.stream().map(RepositoryGoods::goodsToSimple).collect(Collectors.toList());

        // 将结果保存到 Redis 中, 下一次可以直接从 redis cache 中查询
        log.info("cache goods info: [{}]", JSON.toJSONString(ids));
        Map<String, String> id2JsonObject = result.stream().collect(Collectors.toMap(s -> s.getId().toString(), JSON::toJSONString));
        redisTemplate.opsForHash().putAll(GoodsConstant.ECOMMERCE_GOODS_DICT_KEY, id2JsonObject);
        return result;
    }

    @Override
    public Boolean deductGoodsInventory(List<DeductGoodsInventory> deductGoodsInventories) {
        // 检验参数
        deductGoodsInventories.forEach(d -> {
            if (d.getCount() <= 0) {
                throw new RuntimeException("purchase goods count need > 0");
            }
        });

        // 根据id查询库存信息
        List<RepositoryGoods> goodsList = this.listByIds(deductGoodsInventories.stream()
                .map(DeductGoodsInventory::getGoodsId).collect(Collectors.toSet()));

        // 异常处理: 查询不到商品对象 或 查询出来的商品数量与传递的不一致, 抛异常
        if (CollectionUtils.isEmpty(goodsList) || goodsList.size() != deductGoodsInventories.size()) {
            log.error("request is not valid, param is [{}], goodsList size is [{}]", deductGoodsInventories.size(), goodsList.size());
            throw new RuntimeException("request param is not valid");
        }

        // 检查是不是可以扣减库存, 再去扣减库存,  goodsId2Inventory转化为 map(id, DeductGoodsInventory)提高效率
        Map<String, DeductGoodsInventory> goodsId2Inventory = deductGoodsInventories.stream()
                .collect(Collectors.toMap(s -> s.getGoodsId().toString(), Function.identity()));
        goodsList.forEach(g -> {
            Long currentInventory = g.getInventory();
            Integer needDeductInventory = goodsId2Inventory.get(g.getId().toString()).getCount();
            if (currentInventory < needDeductInventory) {
                log.error("goods inventory is not enough: [{}] [{}], [{}]", g.getId(), currentInventory, needDeductInventory);
                throw new RuntimeException("goods inventory is not enough, goods id is: [{}]" + g.getId());
            }
            // 扣减库存
            g.setInventory(currentInventory - needDeductInventory);
            log.info("deduct goods inventory: [{}], [{}], [{}]", g.getId(), currentInventory, g.getInventory());
        });

        // 更新扣减过库存的所有数据
        boolean result = this.saveOrUpdateBatch(goodsList);
        log.info("deduct goods inventory done");
        return result;
    }

    /** 在表中查出是否有相同信息的商品 */
    public Optional<RepositoryGoods> findFirstByGoodsCategoryAndBrandCategoryAndGoodsName(String goodsCategory, String brandCategory, String goodsName) {
        LambdaQueryWrapper<RepositoryGoods> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RepositoryGoods::getGoodsCategory, goodsCategory);
        queryWrapper.eq(RepositoryGoods::getBrandCategory, brandCategory);
        queryWrapper.eq(RepositoryGoods::getGoodsName, goodsName);
        return Optional.ofNullable(this.getOne(queryWrapper));
    }
}
