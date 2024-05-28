package com.faker.project.partition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.binder.PartitionSelectorStrategy;
import org.springframework.stereotype.Component;

/**
 * 决定message发送到哪个分区的策略, 根据key进行分区发送
 */
@Slf4j
@Component
public class CustomPartitionSelectorStrategy implements PartitionSelectorStrategy {

    /** @Param partitionCount 分区个数, 返回分区位置 */
    @Override
    public int selectPartition(Object key, int partitionCount) {
        // 分区处理逻辑: 比如通过某个字段来设置消息应该发送到哪个分区
        int partition = key.toString().hashCode() % partitionCount;

        log.info("spring cloud stream custom selector info: {}, {}, {}", key, partitionCount, partition);
        return partition;
    }
}
