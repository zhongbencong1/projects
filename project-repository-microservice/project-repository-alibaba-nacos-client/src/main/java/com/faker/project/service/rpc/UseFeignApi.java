package com.faker.project.service.rpc;

import com.faker.project.vo.JwtToken;
import com.faker.project.vo.UserNameAndPassword;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Random;

/**
 * 使用 Feign 的原生 Api使用, 而不是 OpenFeign = Feign + Ribbon
 * */
@Slf4j
@Service
public class UseFeignApi {

    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * 使用 Feign 原生 api 调用远端服务
     * Feign 默认配置初始化、设置自定义配置、生成代理对象
     * */
    public JwtToken nativeFeign(UserNameAndPassword usernameAndPassword) {
        // 通过反射去拿 serviceId (AuthorityFeignClient类注解中的value值)
        String serviceId = null;
        Annotation[] annotations = AuthorityFeignClient.class.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(FeignClient.class)) {
                serviceId = ((FeignClient) annotation).value();
                log.info("get service id from AuthorityFeignClient: [{}]", serviceId);
                break;
            }
        }

        // 如果服务 id 不存在, 直接抛异常
        if (StringUtils.isEmpty(serviceId)) {
            throw new RuntimeException("can not get serviceId, check FeignClient value config");
        }
        // 通过 serviceId 去拿可用服务实例
        List<ServiceInstance> targetInstances = discoveryClient.getInstances(serviceId);
        if (CollectionUtils.isEmpty(targetInstances)) {
            throw new RuntimeException("can not get target instance from serviceId: " + serviceId);
        }

        // 随机选择一个服务实例: 负载均衡
        ServiceInstance randomInstance = targetInstances.get(new Random().nextInt(targetInstances.size()));
        log.info("choose service instance: [{}], [{}], [{}]", serviceId, randomInstance.getHost(), randomInstance.getPort());

        // Feign 客户端初始化, 必须要配置 encoder、decoder、contract
        AuthorityFeignClient feignClient = Feign.builder()  // 1. Feign 默认配置初始化
                .encoder(new GsonEncoder())                 // 2.1 设置编码配置
                .decoder(new GsonDecoder())                 // 2.2 设置解码配置
                .logLevel(Logger.Level.FULL)                // 2.3 设置日志配置
                .contract(new SpringMvcContract())
                // 3 生成 AuthorityFeignClient接口的 代理对象, 通过代理对象发起请求
                .target(AuthorityFeignClient.class,
                        String.format("http://%s:%s", randomInstance.getHost(), randomInstance.getPort()));

        return feignClient.getTokenByFeign(usernameAndPassword);
    }
}
