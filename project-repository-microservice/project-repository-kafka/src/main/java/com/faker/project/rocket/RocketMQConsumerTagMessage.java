package com.faker.project.rocket;

import com.alibaba.fastjson.JSON;
import com.faker.project.vo.KafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 第二个RocketMQ消费者, 指定消费带有 tag的消息: 注解中selectorExpression指定过滤的tag, producer生产的消息带有相同tag才会被消费
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMQProducer.TOPIC, consumerGroup = "rocketmq-consumer-tag-string", selectorExpression = RocketMQProducer.TAG)
public class RocketMQConsumerTagMessage implements RocketMQListener<String> {

    /** @param message 参数为rocketmq消费的对象, 对message进行消费 */
    @Override
    public void onMessage(String message) {
        KafkaMessage rocketMessage = JSON.parseObject(message, KafkaMessage.class);
        log.info("RocketMQConsumerTagMessage consume message: {}", JSON.toJSONString(rocketMessage));
    }
}
