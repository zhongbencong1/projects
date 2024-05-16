package com.faker.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * HystrixDashboard 启动
 * 127.0.0.1:9998/project-repository-hystrix-dashboard/hystrix/
 */
@EnableDiscoveryClient  // 注册中心发现
@SpringBootApplication
@EnableHystrixDashboard  // 开启Hystrix dashboard 注解
public class HystrixDashboardApplication {
    public static void main(String[] args) {
        SpringApplication.run(HystrixDashboardApplication.class , args);
    }
}
