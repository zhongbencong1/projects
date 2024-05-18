package com.faker.project.stream;

import com.alibaba.fastjson.JSON;
import com.faker.project.vo.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

/**
 * 使用默认的信道实现消息的接收
 * */
@Slf4j
@EnableBinding(Sink.class)
public class DefaultReceiveService {

    /** 使用默认的输入信道接收消息 @StreamListener(Sink.INPUT) */
    @StreamListener(Sink.INPUT)
    public void receiveMessage(Object payload) {
        log.info("DefaultReceiveService receiveMessage");
        Message message = JSON.parseObject(payload.toString(), Message.class);
        // 模拟消费消息
        log.info("DefaultReceiveService consume message success: [{}]", JSON.toJSONString(message));
    }
}
