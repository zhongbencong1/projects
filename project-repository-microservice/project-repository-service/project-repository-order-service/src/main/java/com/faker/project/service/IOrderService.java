package com.faker.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.faker.project.entity.PageSimpleOrderDetail;
import com.faker.project.entity.ProjectOrder;
import com.faker.project.order.OrderInfo;

/**
 * order接口
 */
public interface IOrderService {

    /** 根据用户id获取所有订单 */
    Page<ProjectOrder> findAllByUserId(Long userId, Integer offset, Integer limit, Boolean order);

    /** 下单操作(分布式事务): 创建订单 -> 扣减库存 -> 扣减余额 -> 创建物流信息(stream + kafka) */
    Long createOrder(OrderInfo orderInfo);

    /** 获取当前用户订单信息 */
    PageSimpleOrderDetail getOrderListByPage(Integer offset, Integer limit, Boolean order);
}
