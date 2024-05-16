package com.faker.project.rocket;

import com.alibaba.fastjson.JSON;
import com.faker.project.vo.KafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * RocketMQ消费者 消费string类型消息
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMQProducer.TOPIC, consumerGroup = "rocketmq-consumer-string")
public class RocketMQConsumerMessage implements RocketMQListener<String> {

    /** @param message 参数为rocketmq消费的对象, 对message进行消费 */
    @Override
    public void onMessage(String message) {
        KafkaMessage rocketMessage = JSON.parseObject(message, KafkaMessage.class);
        log.info("RocketMQConsumerMessage consume message: {}", JSON.toJSONString(rocketMessage));
    }

}
