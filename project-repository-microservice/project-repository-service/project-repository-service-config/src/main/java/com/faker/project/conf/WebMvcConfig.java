package com.faker.project.conf;

import com.alibaba.cloud.seata.web.SeataHandlerInterceptor;
import com.faker.project.filter.LoginUserInfoInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * Web Mvc 配置
 * */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    /** 添加拦截器配置 */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        // 添加用户身份统一登录拦截的拦截器
        registry.addInterceptor(new LoginUserInfoInterceptor())
                .addPathPatterns("/**").order(0);

        // 添加拦截器, seata 传递 xid 事务 id 给其他的微服务. 其他的服务会写 undo_log, 才能够实现分布式事务的回滚
        registry.addInterceptor(new SeataHandlerInterceptor()).addPathPatterns("/**");
    }

    /**
     * 让 MVC 加载 Swagger 的静态资源
     * */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/**").
                addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        super.addResourceHandlers(registry);
    }
}