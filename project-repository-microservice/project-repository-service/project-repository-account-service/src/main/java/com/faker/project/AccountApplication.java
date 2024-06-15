package com.faker.project;

import com.faker.project.conf.DataSourceProxyAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

/**
 * 用户账户微服务启动入口
 * 127.0.0.1:8003/${server.servlet.context-path}/swagger-ui.html
 * */
@SpringBootApplication
@EnableDiscoveryClient
@Import(DataSourceProxyAutoConfiguration.class) // 引入seata配置的数据源
public class AccountApplication {

    public static void main(String[] args) {

        SpringApplication.run(AccountApplication.class, args);
    }
}
