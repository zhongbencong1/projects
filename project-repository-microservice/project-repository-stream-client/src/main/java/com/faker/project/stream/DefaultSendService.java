package com.faker.project.stream;

import com.alibaba.fastjson.JSON;
import com.faker.project.vo.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;

/**
 * 使用默认的通信信道实现消息的发送: @EnableBinding(Source.class)
 */
@Slf4j
@EnableBinding(Source.class)
public class DefaultSendService {

    @Autowired
    private Source source;

    /** 使用默认的输出信道发送消息 */
    public void sendMessage(Message message) {
        String msg = JSON.toJSONString(message);
        log.info("DefaultSendService send message: {}", msg);

        // stream 基于Spring Messaging实现, Spring Messaging, 统一消息的编程模型, 是 Stream 组件的重要组成部分之一
        source.output().send(MessageBuilder.withPayload(msg).build());
    }
}
