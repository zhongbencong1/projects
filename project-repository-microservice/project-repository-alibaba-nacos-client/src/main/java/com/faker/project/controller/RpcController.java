package com.faker.project.controller;

import com.faker.project.service.rpc.AuthorityFeignClient;
import com.faker.project.service.rpc.UseFeignApi;
import com.faker.project.service.rpc.UseRestTemplateService;
import com.faker.project.service.rpc.UseRibbonService;
import com.faker.project.vo.JwtToken;
import com.faker.project.vo.UserNameAndPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微服务通信 rpc Controller
 * */
@RestController
@RequestMapping("/rpc")
public class RpcController {

    @Autowired
    private UseRestTemplateService restTemplateService;

    @Autowired
    private UseRibbonService ribbonService;

    @Autowired
    private AuthorityFeignClient feignClient;

    @Autowired
    private UseFeignApi useFeignApi;

    @PostMapping("/rest-template")
    public JwtToken getTokenFromAuthorityService(@RequestBody UserNameAndPassword usernameAndPassword) {
        return restTemplateService.getTokenFromAuthorityService(usernameAndPassword);
    }

    @PostMapping("/rest-template-load-balancer")
    public JwtToken getTokenFromAuthorityServiceWithLoadBalancer(@RequestBody UserNameAndPassword usernameAndPassword) {
        return restTemplateService.getTokenFromAuthorityServiceWithLoadBalancer(usernameAndPassword);
    }

    @PostMapping("/ribbon")
    public JwtToken getTokenFromAuthorityServiceByRibbon(@RequestBody UserNameAndPassword usernameAndPassword) {
        return ribbonService.getTokenFromAuthorityServiceByRibbon(usernameAndPassword);
    }

    @PostMapping("/native-ribbon")
    public JwtToken thinkingInRibbon(@RequestBody UserNameAndPassword usernameAndPassword) {
        return ribbonService.nativeRibbon(usernameAndPassword);
    }

    @PostMapping("/token-by-feign")
    public JwtToken getTokenByFeign(@RequestBody UserNameAndPassword usernameAndPassword) {
        return feignClient.getTokenByFeign(usernameAndPassword);
    }

    @PostMapping("/native-feign")
    public JwtToken thinkingInFeign(@RequestBody UserNameAndPassword usernameAndPassword) {
        return useFeignApi.nativeFeign(usernameAndPassword);
    }
}
