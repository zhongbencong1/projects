package com.faker.project.sampler;

import brave.sampler.RateLimitingSampler;
import brave.sampler.Sampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * sleuth日志采集配置 使用配置的方式设定抽样率
 * */
@Configuration
public class SamplerConfig {

    /**
     * 限速采集策略 每秒追踪的最大数量
     * */
    @Bean
    public Sampler sampler() {
        return RateLimitingSampler.create(100);
    }

//    /** 概率采集, 默认的采样策略, 默认值是 0.1 (10%的请求被采样) */
//    @Bean
//    public Sampler defaultSampler() {
//        return ProbabilityBasedSampler.create(0.1f);
//    }
}
