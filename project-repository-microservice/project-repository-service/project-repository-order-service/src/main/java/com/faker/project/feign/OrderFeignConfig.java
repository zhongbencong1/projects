package com.faker.project.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Objects;

/**
 * Feign调用时, 把Header也传递到服务提供方
 * */
@Slf4j
@Configuration
public class OrderFeignConfig {

    /** Feign配置请求拦截器: RequestInterceptor提供open-feign的请求拦截器, 把Header信息传递下去 */
    @Bean
    public RequestInterceptor headerInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (Objects.nonNull(attributes)) {
                HttpServletRequest request = attributes.getRequest();
                Enumeration<String> headerNames = request.getHeaderNames();
                if (Objects.nonNull(headerNames)) {
                    while (headerNames.hasMoreElements()) {
                        // 获取请求头的key
                        String name = headerNames.nextElement();
                        // 禁止把当前请求的 content-length 传递到下游的服务提供方; 因为返回长度极有可能不一致而可能导致: 请求一直返回不了,或者是请求响应数据被截断
                        if (!name.equalsIgnoreCase("content-length")) {
                            String values = request.getHeader(name);
                            // 这里的 template 就是 RestTemplate
                            log.info("headerInterceptor put header name:{}, values:{}", name, values);
                            requestTemplate.header(name, values);
                        }
                    }
                }
            }
        };
    }
}
