package com.faker.project.service.rpc;

import com.alibaba.fastjson.JSON;
import com.faker.project.constant.CommonConstant;
import com.faker.project.vo.JwtToken;
import com.faker.project.vo.UserNameAndPassword;
import com.netflix.loadbalancer.*;
import com.netflix.loadbalancer.reactive.LoadBalancerCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用 Ribbon 实现微服务通信
 * */
@Slf4j
@Service
public class UseRibbonService {

    private final RestTemplate restTemplate; // ribbonconfig类中有配置
    private final DiscoveryClient discoveryClient;

    public UseRibbonService(RestTemplate restTemplate, DiscoveryClient discoveryClient) {
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
    }

    /** 通过 Ribbon 调用 Authority 服务获取 Token */
    public JwtToken getTokenFromAuthorityServiceByRibbon(UserNameAndPassword usernameAndPassword) {
        // 注意到 url 中的 ip 和端口换成了服务名称
        String requestUrl = String.format("http://%s/project-repository-authority-center/authority/generate_token",
                CommonConstant.AUTHORITY_CENTER_SERVICE_ID);
        log.info("login request url and body: [{}], [{}]", requestUrl, JSON.toJSONString(usernameAndPassword));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 这里一定要使用自己注入的 RestTemplate
        return restTemplate.postForObject(requestUrl,
                new HttpEntity<>(JSON.toJSONString(usernameAndPassword), headers), JwtToken.class);
    }

    /** 使用原生的 Ribbon Api, 看看 Ribbon 是如何完成: 服务调用 + 负载均衡 */
    public JwtToken nativeRibbon(UserNameAndPassword usernameAndPassword) {
        String urlFormat = "http://%s/project-repository-authority-center/authority/generate_token";
        // 1. 找到服务提供方的地址和端口号
        List<ServiceInstance> targetInstances = discoveryClient.getInstances(CommonConstant.AUTHORITY_CENTER_SERVICE_ID);

        // 构造 Ribbon 服务列表
        List<Server> servers = new ArrayList<>(targetInstances.size());
        targetInstances.forEach(i -> {
            servers.add(new Server(i.getHost(), i.getPort()));
            log.info("found target instance: [{}] -> [{}]", i.getHost(), i.getPort());
        });

        // 2. 使用负载均衡策略实现远端服务调用
        // 构建 Ribbon 负载实例
        BaseLoadBalancer loadBalancer = LoadBalancerBuilder.newBuilder().buildFixedServerListLoadBalancer(servers);
        // 设置负载均衡策略
        loadBalancer.setRule(new RetryRule(new RandomRule(), 300));

        // 服务选择ip port 并远程调用
        String result = LoadBalancerCommand.builder().withLoadBalancer(loadBalancer)
                .build().submit(server -> {
                    String targetUrl = String.format(urlFormat, String.format("%s:%s", server.getHost(), server.getPort()));
                    log.info("target request url: [{}]", targetUrl);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    String tokenStr = new RestTemplate().postForObject(targetUrl,
                            new HttpEntity<>(JSON.toJSONString(usernameAndPassword), headers), String.class);

                    return Observable.just(tokenStr);
                }).toBlocking().first().toString();

        return JSON.parseObject(result, JwtToken.class);
    }
}
