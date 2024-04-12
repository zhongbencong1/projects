package com.faker.project.service.rpc;

import com.alibaba.fastjson.JSON;
import com.faker.project.constant.CommonConstant;
import com.faker.project.vo.JwtToken;
import com.faker.project.vo.UserNameAndPassword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 使用 RestTemplate 实现微服务通信
 * */
@Slf4j
@Service
public class UseRestTemplateService {

    private final LoadBalancerClient loadBalancerClient;

    public UseRestTemplateService(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

    /** 从授权服务中获取 JwtToken */
    public JwtToken getTokenFromAuthorityService(UserNameAndPassword usernameAndPassword) {
        // 第一种方式: 写死 url
        String requestUrl = "http://127.0.0.1:7000/project-repository-authority-center/authority/generate_token";
        log.info("RestTemplate request url and body: [{}], [{}]", requestUrl, JSON.toJSONString(usernameAndPassword));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new RestTemplate().postForObject(requestUrl,
                new HttpEntity<>(JSON.toJSONString(usernameAndPassword), headers), JwtToken.class);
    }

    /** 从授权服务中获取 JwtToken, 且带有负载均衡 */
    public JwtToken getTokenFromAuthorityServiceWithLoadBalancer(UserNameAndPassword usernameAndPassword) {
        // 第二种方式: 通过注册中心拿到服务的信息(是所有的实例), 再去发起调用
        ServiceInstance serviceInstance = loadBalancerClient.choose(CommonConstant.AUTHORITY_CENTER_SERVICE_ID);
        log.info("Nacos Client Info: [{}], [{}], [{}]", serviceInstance.getServiceId(),
                serviceInstance.getInstanceId(), JSON.toJSONString(serviceInstance.getMetadata()));

        String requestUrl = String.format("http://%s:%s/project-repository-authority-center/authority/generate_token",
                serviceInstance.getHost(), serviceInstance.getPort());
        log.info("login request url and body: [{}], [{}]", requestUrl, JSON.toJSONString(usernameAndPassword));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new RestTemplate().postForObject(requestUrl,
                new HttpEntity<>(JSON.toJSONString(usernameAndPassword), headers), JwtToken.class);
    }
}
