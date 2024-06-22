package com.faker.project.controller;

import com.alibaba.fastjson.JSON;
import com.faker.project.vo.JwtToken;
import com.faker.project.vo.UserNameAndPassword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 使用 sentinel 保护 restTemplate 微服务间调用
 */
@Slf4j
@RestController
@RequestMapping("/sentinel-rest-template")
public class SentinelRestTemplateController {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 微服务间调用: 从授权服务中获取 JwtToken
     *  1.流控降级: 是针对于簇点链路中的url接口Api生效: http://127.0.0.1:7000/project-repository-authority-center/authority/generate_token
     *  2.容错降级: Api调用方法抛出异常(BlockException)后生效; 但对于服务不可用(异常为IllegalSate异常)时不能生效(容错降级不生效),如ip+port不能联通
     *            对于接口本身抛出的异常可以容错降级, 而服务不可用时无法进行容错降级
     * */
    @PostMapping("/get-token")
    public JwtToken getTokenFromAuthorityService(@RequestBody UserNameAndPassword usernameAndPassword) {
        String requestUrl = "http://127.0.0.1:7000/project-repository-authority-center/authority/generate_token";
        log.info("RestTemplate request url and body: {}, {}", requestUrl, JSON.toJSONString(usernameAndPassword));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return restTemplate.postForObject(requestUrl, new HttpEntity<>(JSON.toJSONString(usernameAndPassword), headers), JwtToken.class);
    }
}