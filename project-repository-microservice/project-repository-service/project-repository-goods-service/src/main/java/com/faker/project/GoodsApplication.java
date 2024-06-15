package com.faker.project;

import com.faker.project.conf.DataSourceProxyAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

/**
 * 商品微服务启动入口
 * 启动依赖组件/中间件: Redis + MySQL + Nacos + Kafka + Zipkin
 * http://127.0.0.1:8001/project-repository-goods-service/doc.html
 * */
@EnableDiscoveryClient
@SpringBootApplication
@Import(DataSourceProxyAutoConfiguration.class) // 引入seata配置的数据源
public class GoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class, args);
    }
}
