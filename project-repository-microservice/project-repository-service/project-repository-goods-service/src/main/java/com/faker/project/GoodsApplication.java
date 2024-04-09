package com.faker.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 商品微服务启动入口
 * 启动依赖组件/中间件: Redis + MySQL + Nacos + Kafka + Zipkin
 * http://127.0.0.1:8001/ecommerce-goods-service/doc.html
 * */
@EnableDiscoveryClient
@SpringBootApplication
public class GoodsApplication {

    public static void main(String[] args) {

        SpringApplication.run(GoodsApplication.class, args);
    }
}
