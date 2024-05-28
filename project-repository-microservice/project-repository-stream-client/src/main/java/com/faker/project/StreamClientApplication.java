package com.faker.project;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 基于 SpringCloud Stream 构建消息驱动微服务
 * stream 相当于对 消息中间件进行了一次封装
 * */
@EnableDiscoveryClient
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class StreamClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(StreamClientApplication.class, args);
    }
}
