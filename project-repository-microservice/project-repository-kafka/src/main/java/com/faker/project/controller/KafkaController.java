package com.faker.project.controller;

import com.faker.project.kafka.KafkaProducer;
import com.faker.project.vo.KafkaMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * springboot 集成 kafka 发送消息
 */
@Slf4j
@RestController
@RequestMapping("/kafka")
public class KafkaController {

    private final ObjectMapper mapper;
    private final KafkaProducer kafkaProducer;

    public KafkaController(ObjectMapper mapper, KafkaProducer kafkaProducer) {
        this.mapper = mapper;
        this.kafkaProducer = kafkaProducer;
    }

    /** 发送 kafka 消息 */
    @GetMapping("/send-message")
    public void sendMessage(@RequestParam(required = false) String key, @RequestParam() String topic) throws Exception {
        KafkaMessage kafkaMessage = new KafkaMessage(1, "project-repository-kafka");
        kafkaProducer.sendKafkaMessage(key, mapper.writeValueAsString(kafkaMessage), topic);
    }
}
