package com.faker.project.service.rpc.hystrix;

import com.alibaba.fastjson.JSON;
import com.faker.project.service.rpc.AuthorityFeignClient;
import com.faker.project.vo.JwtToken;
import com.faker.project.vo.UserNameAndPassword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * AuthorityFeignClient fallback 后备策略返回 new JwtToken("xxx");
 */
@Slf4j
@Component
public class AuthorityFeignClientFallback implements AuthorityFeignClient {

    @Override
    public JwtToken getTokenByFeign(UserNameAndPassword usernameAndPassword) {
        log.info("authority feign client get token by feign request error (Hystrix Fallback): [{}]",
                JSON.toJSONString(usernameAndPassword));
        return new JwtToken("xxx");
    }
}
