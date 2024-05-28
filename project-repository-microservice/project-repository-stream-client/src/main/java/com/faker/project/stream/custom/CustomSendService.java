package com.faker.project.stream.custom;

import com.alibaba.fastjson.JSON;
import com.faker.project.vo.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

/**
 * 使用自定义的通信信道 CustomSource 实现消息的发送
 * */
@Slf4j
@EnableBinding(CustomSource.class)
public class CustomSendService {

    @Autowired
    private CustomSource customSource;

    /** 使用自定义的输出信道发送消息 */
    public void sendMessage(Message message) {
        String msg = JSON.toJSONString(message);
        log.info("CustomSendService send message: {}", msg);
        customSource.customOutput().send(MessageBuilder.withPayload(msg).build());
    }
}
