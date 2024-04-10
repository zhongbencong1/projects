package com.faker.project.config;

import com.faker.project.constant.GatewayConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置登录请求转发规则
 * */
@Configuration
public class RouteLocatorConfig {

    public static String PREFIX;

    public static String PORT;

    @Value("${server.servlet.context-path}")
    public void setPrefix(String prefix) {
        PREFIX = prefix;
    }

    @Value("${server.port}")
    public void setPort(String port) {
        PORT = port;
    }

    /** 使用代码定义路由规则, 在网关 拦截下登录和注册接口，然后转发给网关 网关通过过滤器来实现登录注册 */
    @Bean
    public RouteLocator loginRouteLocator(RouteLocatorBuilder builder) {

        // 手动定义 Gateway 路由规则需要指定 id(全局唯一即可) path 和 uri
        return builder.routes()
                .route("authority", r -> r.path(
                        PREFIX + GatewayConstant.LOGIN_URI ,
                        PREFIX + GatewayConstant.REGISTER_URI
                        ).uri("http://localhost:" + PORT)
                ).build();
    }
}
