package com.faker.project;

import com.faker.project.conf.DataSourceProxyAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

/**
 * 物流微服务启动入口
 */
@Import(DataSourceProxyAutoConfiguration.class)
@SpringBootApplication
@EnableDiscoveryClient
public class LogisticsApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogisticsApplication.class, args);
    }
}
