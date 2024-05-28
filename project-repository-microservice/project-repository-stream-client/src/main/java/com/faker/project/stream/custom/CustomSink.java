package com.faker.project.stream.custom;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 自定义输入信道: 参考 org.springframework.cloud.stream.messaging.Sink接口
 */
public interface CustomSink {
    // 字符串必须和 "input" 区分开, 该字符串代表唯一通信信道
    String INPUT = "customInput";

    /** 输入信道的名称是 CustomSink, 需要使用 Stream 绑定器在 yml 文件中声明 */
    @Input(CustomSink.INPUT)
    SubscribableChannel input();
}
