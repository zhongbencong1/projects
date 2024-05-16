package com.faker.project.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * kafka 生产者
 */
@Slf4j
@Component
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /** 发送 kafka 消息 并获取返回信息(通知): 1.异步 2.同步 */
    public void sendKafkaMessage(String key, String value, String topic) {
        if (StringUtils.isBlank(value) || StringUtils.isBlank(topic)) {
            throw new IllegalArgumentException("value or topic is null or empty");
        }
        // kafkaTemplate.send 默认以异步方式发送消息, 并且不处理结果
        ListenableFuture<SendResult<String, String>> future =
                StringUtils.isBlank(key) ? kafkaTemplate.send(topic, value) : kafkaTemplate.send(topic, key, value);

        // 处理结果: 异步处理 / 同步处理 (会产生性能损耗和处理延迟, 结合业务决策是否需要处理结果)
        // 1.异步回调获取通知
        future.addCallback(
                success -> {assert null != success && Objects.nonNull(success.getRecordMetadata());
                // 获取原信息: topic 分区 offset
                String topic_back = success.getRecordMetadata().topic();
                int partition = success.getRecordMetadata().partition();
                long offset = success.getRecordMetadata().offset();
                log.info("send kafka message success: {}, {}, {}", topic_back, partition, offset);
            }, failure -> log.error("send kafkaMessage failure, param: {}, {}, {}", key, value, topic)
        );

        // 2.同步等待的方式获取通知
        try {
            // SendResult<String, String> sendResult = future.get();
            // 设置超时值, 超时后框架抛出异常
            SendResult<String, String> sendResult_time_delay = future.get(5, TimeUnit.SECONDS);
            // 获取原信息: topic 分区 offset
            String topic_back = sendResult_time_delay.getRecordMetadata().topic();
            int partition = sendResult_time_delay.getRecordMetadata().partition();
            long offset = sendResult_time_delay.getRecordMetadata().offset();

            log.info("send kafka message success: {}, {}, {}", topic_back, partition, offset);
        } catch (Exception ex) {
            log.error("send kafkaMessage failure, param: {}, {}, {}", key, value, topic);
        }
    }
}
