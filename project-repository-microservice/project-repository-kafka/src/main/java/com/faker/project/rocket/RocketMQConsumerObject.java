package com.faker.project.rocket;

import com.alibaba.fastjson.JSON;
import com.faker.project.vo.KafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 第四个 rocketmq消费者, 根据 RocketMQProducer.TAG 进行过滤消息: 存在selectorExpression
 * 使用KafkaMessage类接收被消费对象, 直接消费java pojo
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMQProducer.TOPIC, consumerGroup = "rocketmq-consumer-obj", selectorExpression = RocketMQProducer.TAG)
public class RocketMQConsumerObject implements RocketMQListener<KafkaMessage> {
    @Override
    public void onMessage(KafkaMessage rocketMessage) {
        log.info("RocketMQConsumerObject result: {}", JSON.toJSONString(rocketMessage));
    }
}
