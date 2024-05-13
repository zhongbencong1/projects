package com.faker.project.kafka;

import com.faker.project.vo.KafkaMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * kafka 消费者
 */
@Slf4j
@Component
public class KafkaConsumer {

    /** 序列化 将java对象转换为字符串 */
    private final ObjectMapper mapper;

    public KafkaConsumer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /** 监听 kafka 消息并消费 */
    @KafkaListener(topics = {"kafka-faker"}, groupId = "springboot-kafka-1")
    public void listener_first(ConsumerRecord<String, String> record) throws Exception {
        String key = record.key();
        String value = record.value();

        KafkaMessage kafkaMessage = mapper.readValue(value, KafkaMessage.class);
        log.info("listener_first kafkaMessage: {}, {}", key, mapper.writeValueAsString(kafkaMessage));
    }

    /** 监听 kafka 消息并消费 未知被转的类 */
    @KafkaListener(topics = {"kafka-faker"}, groupId = "springboot-kafka-2")
    public void listener_second(ConsumerRecord<?, ?> record) throws Exception {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            KafkaMessage kafkaMessage_ex = mapper.readValue(message.toString(), KafkaMessage.class);
            log.info("listener_second kafkaMessage: {}", mapper.writeValueAsString(kafkaMessage_ex));
        }
    }
}
