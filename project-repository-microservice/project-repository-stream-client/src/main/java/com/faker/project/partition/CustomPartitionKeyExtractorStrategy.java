package com.faker.project.partition;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.binder.PartitionKeyExtractorStrategy;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * 自定义策略: 从message中提取 分区关键字 partition key
 * 会根据key进行分区处理
 */
@Slf4j
@Component
public class CustomPartitionKeyExtractorStrategy implements PartitionKeyExtractorStrategy {

    @Override
    public Object extractKey(Message<?> message) {
        com.faker.project.vo.Message customMessage =
                JSON.parseObject(message.getPayload().toString(), com.faker.project.vo.Message.class);
        // 自定义提取 key
        String key = customMessage.getAuthor();
        log.info("spring cloud stream custom partition Key: {}", key);
        return key;
    }
}
