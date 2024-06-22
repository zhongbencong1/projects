package com.faker.project.sink;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 自定义物流信息接收器(消费者)
 */
public interface LogisticsSink {

    /** 输入信道name */
    String INPUT = "logisticsInput";

    /** 物流sink(槽): INPUT */
    @Input(INPUT)
    SubscribableChannel logisticsInput();
}
