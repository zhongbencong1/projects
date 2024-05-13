package com.faker.project.service.rpc.hystrix;

import com.faker.project.service.rpc.AuthorityFeignClient;
import com.faker.project.vo.JwtToken;
import com.faker.project.vo.UserNameAndPassword;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * OpenFeign 集成 Hystrix 的另一种模式, 相比 fallback 则可以打印错误信息,
 */
@Slf4j
@Component
public class AuthorityFeignClientFallbackFactory implements FallbackFactory<AuthorityFeignClient> {
    @Override
    public AuthorityFeignClient create(Throwable throwable) {
        log.warn("authority feign client get token by feign request error " +
                "(Hystrix FallbackFactory): [{}]", throwable.getMessage(), throwable);

        return usernameAndPassword -> new JwtToken("xxx-factory");
    }
}
