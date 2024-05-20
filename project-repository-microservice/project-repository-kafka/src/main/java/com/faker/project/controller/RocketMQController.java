package com.faker.project.controller;

import com.alibaba.fastjson.JSON;
import com.faker.project.rocket.RocketMQProducer;
import com.faker.project.vo.KafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * springboot 集成 rocketmq 发送消息
 */
@Slf4j
@RestController
@RequestMapping("/rocketmq")
public class RocketMQController {

    private static final KafkaMessage rocketmqMessage = new KafkaMessage(101, "faker");

    @Autowired
    private RocketMQProducer rocketMQProducer;

    @GetMapping("/rocket-value")
    public void sendRocketMQMessageWithValue() {
        rocketMQProducer.sendMessageWithValue(JSON.toJSONString(rocketmqMessage));
    }

    @GetMapping("/rocket-key")
    public void sendRocketMQMessageWithKey() {
        rocketMQProducer.sendMessageWithKey("key", JSON.toJSONString(rocketmqMessage));
    }

    /** 消费者会根据 RocketMQProducer.TAG 进行过滤; key仅作为查找索引使用 不用关注 */
    @GetMapping("/rocket-tag")
    public void sendRocketMQMessageWithTag() {
        rocketMQProducer.sendMessageWithTag(RocketMQProducer.TAG, JSON.toJSONString(rocketmqMessage));
    }

    /** 消费者会根据 RocketMQProducer.TAG 进行过滤; key仅作为查找索引使用 不用关注 */
    @GetMapping("/rocket-all")
    public void sendRocketMQMessageWithAll() {
        rocketMQProducer.sendMessageWithAll("key", RocketMQProducer.TAG, JSON.toJSONString(rocketmqMessage));
    }
}
