package com.faker.project.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 使用 Ribbon 之前的配置, @LoadBalanced增强 RestTemplate (负载均衡)
 * */
@Component
public class RibbonConfig {

    /** 注入 RestTemplate */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
