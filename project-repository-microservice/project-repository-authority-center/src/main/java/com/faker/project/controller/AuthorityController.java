package com.faker.project.controller;

import com.alibaba.fastjson.JSON;
import com.faker.project.annotation.IgnoreResponseAdvice;
import com.faker.project.service.IJWTService;
import com.faker.project.vo.JwtToken;
import com.faker.project.vo.UserNameAndPassword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对外暴露的授权服务接口
 */
@Slf4j
@RestController
@IgnoreResponseAdvice
@RequestMapping(value = "/authority")
public class AuthorityController {

    private final IJWTService iJwTService;

    public AuthorityController(IJWTService iJwTService) {
        this.iJwTService = iJwTService;
    }

    @PostMapping(value = "/generate_token")
    public JwtToken getToken(@RequestBody UserNameAndPassword uap) throws Exception {
        log.info("request to get token with param: [{}]", JSON.toJSONString(uap));
        return new JwtToken(iJwTService.generateToken(uap.getUsername(), uap.getPassword()));
    }

    /* 注册用户并生成token返回 */
    @RequestMapping(value = "/register_token")
    public JwtToken registerAndGetToken(@RequestBody UserNameAndPassword uap) throws Exception {
        return new JwtToken(iJwTService.registerUserAndGenerateToken(uap));
    }

}
