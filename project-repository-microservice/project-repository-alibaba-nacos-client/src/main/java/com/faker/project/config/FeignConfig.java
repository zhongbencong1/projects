package com.faker.project.config;

import feign.Logger;
import feign.Request;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * OpenFeign 配置类
 * */
@Configuration
public class FeignConfig {

    /** 开启 OpenFeign 日志 */
    @Bean
    public Logger.Level feignLogger() {
        return Logger.Level.FULL;   //  需要注意, logback日志级别需要修改成 debug
    }

    /**
     * OpenFeign 开启重试
     * period = 500 发起当前请求的时间间隔, 单位是 ms
     * maxPeriod = 1000 发起当前请求的最大时间间隔, 单位是 ms
     * maxAttempts = 5 最多请求次数
     * */
    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(500, SECONDS.toMillis(1), 5);
    }

    public static final int CONNECT_TIMEOUT_MILLS = 5000;
    public static final int READ_TIMEOUT_MILLS = 5000;

    /** 对请求的 连接超时时间 和 响应时间 进行限制, true:转发请求是否算时间 */
    @Bean
    public Request.Options options() {
        return new Request.Options(CONNECT_TIMEOUT_MILLS,
                TimeUnit.MICROSECONDS, READ_TIMEOUT_MILLS, TimeUnit.MILLISECONDS, true);
    }
}
