package com.faker.project.feign;

import com.faker.project.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * sentinel 对 openFeign 接口的降级策略
 */
@Slf4j
@Component
public class SentinelFeignClientFallback implements SentinelFeignClient {

    @Override
    public CommonResponse<String> getResultByFeign(Integer code) {
        log.error("SentinelFeignClientFallback getResultByFeign has some error: {}", code);
        return new CommonResponse<>(-1, "sentinel feign fallback", "input param code: "+ code);
    }
}