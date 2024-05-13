package com.faker.project.service.rpc;

import com.faker.project.service.rpc.hystrix.AuthorityFeignClientFallback;
import com.faker.project.service.rpc.hystrix.AuthorityFeignClientFallbackFactory;
import com.faker.project.vo.JwtToken;
import com.faker.project.vo.UserNameAndPassword;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * contextId是每个Feign Client的唯一标识, 通过value的服务在注册中心(例如nacos)中找到对应的ip port
 * 与 Authority 服务通信的 Feign Client 接口定义
 * AuthorityFeignClientFallback feign集成hystrix实现快速失败 fallback: feign失败时不会返回错误或抛出异常 而是使用后备策略返回指定结果
 * */
@FeignClient(contextId = "AuthorityFeignClient", value = "project-repository-authority-center",
        // fallback 与 fallbackFactory 不能同时使用, 只能选其一
//        fallback = AuthorityFeignClientFallback.class
        fallbackFactory = AuthorityFeignClientFallbackFactory.class  //相比 fallback 则可以处理错误信息 (打印错误信息)
)
public interface AuthorityFeignClient {

    /** 通过 OpenFeign 访问 Authority 获取 Token */
    @RequestMapping(value = "/project-repository-authority-center/authority/generate_token", method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    JwtToken getTokenByFeign(@RequestBody UserNameAndPassword usernameAndPassword);
}
