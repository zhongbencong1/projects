package com.faker.project.controller;

import com.faker.project.feign.SentinelFeignClient;
import com.faker.project.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 'openFeign'集成'sentinel'实现熔断降级, 类似于hystrix, 类似底线策略
 * */
@Slf4j
@RestController
@RequestMapping("/sentinel-feign")
@SuppressWarnings("all")
public class SentinelFeignController {

    @Autowired
    private SentinelFeignClient sentinelFeignClient;

    /** 通过 feign 进行微服务间Api调用, 获取结果 */
    @GetMapping("/result-by-feign")
    public CommonResponse<String> getResultByFeign(@RequestParam Integer code) {
        log.info("coming in get result by feign: {}", code);
        return sentinelFeignClient.getResultByFeign(code);
    }
}