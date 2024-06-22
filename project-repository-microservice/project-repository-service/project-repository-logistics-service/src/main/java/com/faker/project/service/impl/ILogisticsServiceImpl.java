package com.faker.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.faker.project.entity.LogisticsInfo;
import com.faker.project.mapper.LogisticsServiceMapper;
import com.faker.project.service.ILogisticsService;
import com.faker.project.sink.LogisticsSink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * 物流服务实现
 */
@Slf4j
@Service
@EnableBinding(LogisticsSink.class)
public class ILogisticsServiceImpl extends ServiceImpl<LogisticsServiceMapper, LogisticsInfo> implements ILogisticsService {

    /**
     * 订阅监听信道中 微服务发送的物流消息
     */
    @StreamListener(value = LogisticsSink.INPUT)
    public void consumeLogisticsMessage(@Payload Object payload) {
        log.info("ILogisticsServiceImpl consumeLogisticsMessage get payload is {}", payload);
        LogisticsInfo logisticsInfo = JSON.parseObject(String.valueOf(payload), LogisticsInfo.class);
        boolean save = this.save(logisticsInfo);
        log.info("ILogisticsServiceImpl consumeLogisticsMessage result is {}", save);
    }

}