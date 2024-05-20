package com.faker.project.rocket;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 第三个 rocketmq消费者
 * 使用MessageExt类接收被消费对象, 可以通过messageExt.getKeys() 获取consumer中加入的key
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMQProducer.TOPIC, consumerGroup = "rocketmq-consumer-ext-string")
public class RocketMQConsumerExtMessage implements RocketMQListener<MessageExt> {

    // RocketMQListener接口的参数决定onMessage方法的入参类型
    @Override
    public void onMessage(MessageExt messageExt) {
        String value = new String(messageExt.getBody());
        log.info("RocketMQConsumerExtMessage result value: {}, keys: {}", value, messageExt.getKeys());

        // 打开下面的注释会导致bug, 参考https://blog.csdn.net/pange1991/article/details/79175448
        // 如果没有特殊情况，RocketMQ的消息就打印body部分(真正的消息内容)就够看了，不要打印MessageExt对象
        // 扩展: 充血模型和贫血模型 https://blog.csdn.net/qq_40223688/article/details/116002898
        // log.info("messageExt: {}", JSON.toJSONString(messageExt));
    }
}
