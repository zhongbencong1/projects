package com.faker.project.rocket;

import com.alibaba.fastjson.JSON;
import com.faker.project.vo.KafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * 通过 RocketMQ 发送消息: 同步 / 异步
 * rocketmq消息 基于 spring messaging 实现
 */
@Slf4j
@Component
public class RocketMQProducer {

    /** rocketmq的topic 类似 kafka的topic 默认读写队列是4个 */
    public static final String TOPIC = "project-rocket-mq";

    /** rocketmq的tag 相当于给message打了标签, 对消息进行过滤 */
    public static final String TAG = "rocket-message-tag";

    /** rocketmq客户端 用来发送消息 */
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /** 使用同步的方式发送消息, 不指定 key 和 tag, 对应 RocketMQConsumerMessage类 进行消费 */
    public void sendMessageWithValue(String value) {
        // 随机选择 topic 的message queue 发送消息
        SendResult sendResult = rocketMQTemplate.syncSend(TOPIC, value);
        log.info("send message result: {}", JSON.toJSONString(sendResult));

        // 最后一个字符串"hashKey", 用作shardingKey做区块分区(类似kafka的partition)
        SendResult sendResultOrder = rocketMQTemplate.syncSendOrderly(TOPIC, value, "hashKey");
        log.info("send message order result: {}", JSON.toJSONString(sendResultOrder));
    }

    /**
     * 使用异步方式发送消息 并且指定key, key方便消息提取, 对应 RocketMQConsumerExtMessage类 进行消费
     * rocket中的key: rocketmq消息中索引的键, 可以快速的检索到消息, 多个key: “key_1 key_2 key_3 ” 中间用空格拼接起来的字符串
     * rocket中的key: 不具有类似kafka的key分区的效果, 分区的效果使用的是hashKey(该java类 37 行最后一个参数)
     */
    public void sendMessageWithKey(String key, String value) {
        Message<String> message = MessageBuilder.withPayload(value).setHeader(RocketMQHeaders.KEYS, key).build();
        // 异步发送消息, 并设置回调 参数自动回填
        rocketMQTemplate.asyncSend(TOPIC, message, new SendCallback() {
            // 成功发送消息 回调
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("sendMessageWithKey success result: {}", JSON.toJSONString(sendResult));
            }
            // 发送消息失败 回调
            @Override
            public void onException(Throwable th) {
                log.info("sendMessageWithKey failed reason: {}", th.getMessage(), th);
            }
        });
    }

    /**
     * 使用同步的方式发送消息 带有tag, 发送的是java pojo, 对应 RocketMQConsumerObject类 进行消费
     * 不同的消费者组订阅同一个topic topic有不同的tag进行区分, 同一个消费者组必须订阅相同的tag, 不同消费者组可以订阅不同的tag
     * */
    public void sendMessageWithTag(String tag, String value) {
        KafkaMessage rocketMessage = JSON.parseObject(value, KafkaMessage.class);
        SendResult sendResult = rocketMQTemplate.syncSend(String.format("%s:%s", TOPIC, tag), rocketMessage);
        log.info("sendMessageWithTag sendResult: {}", JSON.toJSONString(sendResult));
    }

    /** 使用同步的方式发送消息, 带有key和tag, 对应 RocketMQConsumerTagMessage类 进行消费 */
    public void sendMessageWithAll(String key, String tag, String value) {
        Message<String> message = MessageBuilder.withPayload(value).setHeader(RocketMQHeaders.KEYS, key).build();
        SendResult sendResult = rocketMQTemplate.syncSend(String.format("%s:%s", TOPIC, tag), message);
        log.info("sendMessageWithAll sendResult: {}", JSON.toJSONString(sendResult));
    }

}
