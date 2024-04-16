package com.faker.project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class NacosClientService {

    private final DiscoveryClient discoveryClient;

    public NacosClientService(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public List<ServiceInstance> getNacosClientInfo(String serviceId) {
////        测试 UseHystrixCommandAnnotation 的超时
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException ex) {
//            //
//        }
        // 测试 NacosClientHystrixCommand 熔断
        throw new RuntimeException("has some error");
//        log.info("request nacos client to get service instance info: [{}]", serviceId);
//        return discoveryClient.getInstances(serviceId);
    }
}
