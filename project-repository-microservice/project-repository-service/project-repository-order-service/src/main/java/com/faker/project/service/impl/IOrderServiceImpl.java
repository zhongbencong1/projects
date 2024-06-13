package com.faker.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.faker.project.account.AddressInfo;
import com.faker.project.account.BalanceInfo;
import com.faker.project.common.TableId;
import com.faker.project.entity.PageSimpleOrderDetail;
import com.faker.project.entity.ProjectOrder;
import com.faker.project.feign.NotSecuredBalanceClient;
import com.faker.project.feign.NotSecuredGoodsClient;
import com.faker.project.feign.SecureAddressClient;
import com.faker.project.feign.SecuredGoodsClient;
import com.faker.project.filter.AccessContext;
import com.faker.project.goods.SimpleGoodsInfo;
import com.faker.project.mapper.OrderServiceMapper;
import com.faker.project.order.LogisticsMessage;
import com.faker.project.order.OrderInfo;
import com.faker.project.service.IOrderService;
import com.faker.project.source.LogisticsSource;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * order实现类
 */
@Slf4j
@Service
@EnableBinding(LogisticsSource.class) // stream 通信信道注解
@Transactional(rollbackFor = Exception.class)
@SuppressWarnings("all")
public class IOrderServiceImpl extends ServiceImpl<OrderServiceMapper, ProjectOrder> implements IOrderService {

    @Autowired
    private SecureAddressClient secureAddressClient;

    @Autowired
    private SecuredGoodsClient securedGoodsClient;

    @Autowired
    private NotSecuredGoodsClient notSecuredGoodsClient;

    @Autowired
    private NotSecuredBalanceClient notSecuredBalanceClient;

    @Autowired // stream发射器
    private LogisticsSource logisticsSource;

    @Override
    public Page<ProjectOrder> findAllByUserId(Long userId, Integer offset, Integer limit, Boolean order) {
        log.info("findAllByUserId input param: userId:{}, offset:{}, limit:{}, order:{}.", userId, offset, limit, order);
        LambdaQueryWrapper<ProjectOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProjectOrder::getUserId, userId);
        queryWrapper.orderBy(Boolean.TRUE, order, ProjectOrder::getUserId);
        Page<ProjectOrder> pageInfo = new Page<>(offset, limit);
        Page<ProjectOrder> result = this.page(pageInfo, queryWrapper);
        log.info("findAllByUserId output param: result.size:{}", result.getSize());
        return result;
    }

    /** 创建订单: 分布式事务
     * 创建订单会涉及到多个步骤和校验, 当不满足情况时直接抛出异常; 抛出异常则触发分布式事务回滚
     * 1.校验请求对象是否合法
     * 2.创建订单
     * 3.扣减商品库存
     * 4.扣减用户余额
     * 5.发送订单物流消息 SpringCloud Stream + Kafka
     */
    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public TableId createOrder(OrderInfo orderInfo) {
        // 1.获取地址信息
        AddressInfo addressInfo = secureAddressClient.getAddressInfoByTablesId(
                new TableId(Collections.singletonList(new TableId.Id(orderInfo.getUserAddress())))).getData();
        // 校验请求地址对象是否合法(商品不需要校验)
        if (CollectionUtils.isEmpty(addressInfo.getAddressItems())) {
            throw new RuntimeException("user address is not exist, msg: " + orderInfo.getUserAddress());
        }

        // 2.创建订单
        ProjectOrder projectOrder = new ProjectOrder(
                AccessContext.getLoginUserInfo().getId(),
                orderInfo.getUserAddress(),
                JSON.toJSONString(orderInfo.getOrderItemList()));
        boolean save = this.save(projectOrder);
        log.info("create order user is: {}, order id is: {}, result is: {}",
                AccessContext.getLoginUserInfo().getId(), projectOrder.getId(), save);

        // 3.扣减库存
        Boolean booleanCommonResponse = notSecuredGoodsClient.deductGoodsInventory(
                orderInfo.getOrderItemList().stream()
                        .map(OrderInfo.OrderItem::toDeductGoodsInventory)
                        .collect(Collectors.toList())).getData();
        if (!booleanCommonResponse) {
            throw new RuntimeException("deduct goods inventory failure");
        }

        // 4.扣减账户余额
        // 4.1 获取商品信息, 计算总价格
        List<SimpleGoodsInfo> goodsInfos = notSecuredGoodsClient.getSimpleGoodsInfoByTableId(
                new TableId(orderInfo.getOrderItemList()
                        .stream().map(s -> new TableId.Id(s.getGoodsId()))
                        .collect(Collectors.toList())))
                .getData();
        Map<Long, SimpleGoodsInfo> goodsToGoodsInfo = goodsInfos.stream().collect(Collectors.toMap(SimpleGoodsInfo::getId, Function.identity()));
        Long balance = 0L;
        for (OrderInfo.OrderItem orderItem : orderInfo.getOrderItemList()) {
            balance += goodsToGoodsInfo.get(orderItem.getGoodsId()).getPrice() * orderItem.getCount();
        }
        assert balance > 0;

        // 4.2 填写总价格, 扣减账户余额
        BalanceInfo balanceInfo = notSecuredBalanceClient.deductBalance(
                new BalanceInfo(AccessContext.getLoginUserInfo().getId(), balance)).getData();
        if (Objects.isNull(balanceInfo)) {
            throw new RuntimeException("deduct user balance failure");
        }
        log.info("deduct user balance， order id: {}, balanceInfo: {}", projectOrder.getId(), balanceInfo);

        // 5.发送订单物流消息： stream + kafka
        LogisticsMessage logisticsMessage = new LogisticsMessage(
                AccessContext.getLoginUserInfo().getId(), projectOrder.getId(), orderInfo.getUserAddress(), null);
        Message<String> message = MessageBuilder.withPayload(JSON.toJSONString(logisticsMessage)).build();
        boolean send = logisticsSource.logisticsOutput().send(message); // 发送: 成功true; 失败false
        if (!send) {
            throw new RuntimeException("send logistics message failure");
        }
        log.info("send create order message to kafka with stream: {}", JSON.toJSONString(logisticsMessage));
        return new TableId(Collections.singletonList(new TableId.Id(projectOrder.getId())));
    }

    @Override
    public PageSimpleOrderDetail getOrderListByPage(Integer offset, Integer limit, Boolean order) {
        if (offset <= 0) {
            offset = 1;
        }

        // 1页10条数据, 按照id倒序
        LambdaQueryWrapper<ProjectOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(ProjectOrder::getId)
                .eq(ProjectOrder::getUserId, AccessContext.getLoginUserInfo().getId());
        List<ProjectOrder> orders = this.page(new Page<>(offset, limit), queryWrapper).getRecords();
        if (CollectionUtils.isEmpty(orders)) {
            return new PageSimpleOrderDetail(Collections.emptyList(), Boolean.FALSE);
        }




        return null;
    }
}
