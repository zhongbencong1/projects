package com.faker.project.controller;

import com.faker.project.common.TableId;
import com.faker.project.entity.PageSimpleOrderDetail;
import com.faker.project.order.OrderInfo;
import com.faker.project.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单服务对外http接口
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    /** 购买(分布式事务): 创建订单 -> 扣减库存 -> 扣减余额 -> 发送物流消息 */
    @PostMapping("/create-order")
    public TableId createOrder(@RequestBody OrderInfo orderInfo) {
        return orderService.createOrder(orderInfo);
    }

    /** 获取当前用户的订单信息: 带有分页 */
    @GetMapping("/order-detail")
    public PageSimpleOrderDetail getSimpleOrderDetailByPage(
            @RequestParam(value = "offset", defaultValue = "1") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return orderService.getOrderListByPage(offset, limit);
    }
}
