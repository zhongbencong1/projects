package com.faker.project.stream.custom;

import com.alibaba.fastjson.JSON;
import com.faker.project.vo.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;

/**
 * 使用自定义的输入信道实现消息的接收
 */
@Slf4j
@EnableBinding(CustomSink.class)
public class CustomReceiveService {

    /** 使用自定义的输入信道接收消息 */
    @StreamListener(CustomSink.INPUT)
    public void receiveMessage(@Payload Object payload) {
        log.info("CustomReceiveService consume message");
        Message message = JSON.parseObject(payload.toString(), Message.class);
        // 模拟消费消息
        log.info("CustomReceiveService consume message success: {}", JSON.toJSONString(message));
    }
}
