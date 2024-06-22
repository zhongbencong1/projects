package com.faker.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Sentinel 集成到 SpringCloud 工程中
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class SentinelClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SentinelClientApplication.class, args);
    }
}