package com.faker.project.conf;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * sentinel开启微服务间rest调用保护, 需要给restTemplate做一些配置
 */
@Slf4j
@Configuration
public class SentinelConfig {

    /** 对'restTemplate'进行配置 */
    @Bean
    @SentinelRestTemplate(
            fallback = "handleFallback", fallbackClass = RestTemplateExceptionUtil.class,
            blockHandler = "handleBlock", blockHandlerClass = RestTemplateExceptionUtil.class
    )
    public RestTemplate configRestTemplate() {
        return new RestTemplate();
    }
}