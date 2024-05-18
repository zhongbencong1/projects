package com.faker.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息传递对象: SpringCloud Stream + Kafka/RocketMQ
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private Integer id;

    private String projectName;

    private String org;

    private String author;

    private String version;

    /** 返回一个默认的消息, 方便使用 */
    public static Message defaultMessage() {
        return new Message(1, "pro-stream-client", "faker.com", "faker", "1.0");
    }
}
