package com.faker.project.feign;

import com.faker.project.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 通过 sentinel 对 openFeign 实现熔断降级
 */
@FeignClient(value = "project-xxx", fallback = SentinelFeignClientFallback.class) // 设置project-xxx, 则返回服务不存在, 引起sentinel进行处理
public interface SentinelFeignClient {

    @RequestMapping(value = "faker", method = RequestMethod.GET)
    CommonResponse<String> getResultByFeign(@RequestParam Integer code);
}