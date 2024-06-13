package com.faker.project.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建订单时发送的物流消息: Stream 物流消息对象
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogisticsMessage {

    /** 用户表主键 id */
    private Long userId;

    /** 订单表主键 id */
    private Long orderId;

    /** 用户地址表主键 id */
    private Long addressId;

    /** 备注信息(json 存储) */
    private String extraInfo;
}
