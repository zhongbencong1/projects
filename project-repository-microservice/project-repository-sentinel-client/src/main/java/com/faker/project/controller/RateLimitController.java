package com.faker.project.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.faker.project.block_handler.BlockHandler;
import com.faker.project.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基于 sentinel 控制台配置流控规则
 * sentinel 是懒加载的, 需要先进行API访问, 则在sentinel dashboard可以看到
 */
@Slf4j
@RestController
@RequestMapping("/dashboard")
public class RateLimitController {

    /** 在 dashboard 中 "流控规则" 中按照 '资源名称' 新增 '流控规则' */
    @GetMapping("/by-resource")
    // value = "byResource" 该处是指 以'byResource'方法为资源
    @SentinelResource(value = "byResource", blockHandler = "HandleBlockException", blockHandlerClass = BlockHandler.class)
    public CommonResponse<String> byResource() {
        log.info("coming in rate limit controller by resource");
        return new CommonResponse<>(0, "", "byResource");
    }

    /** 在 "簇点链路" 中给 url 添加流控规则
     *  簇点链路:
     * 	当请求进入微服务时, 首先会访问DispatcherServlet, 然后进入Controller -> Service -> Mapper, 这样的一个调用链就叫做簇点链路; 簇点链路中被监控的每一个接口就是一个资源;
     * 	默认情况下sentinel会监控SpringMVC的每一个端点(Endpoint,也就是controller中的方法),因此SpringMVC的每一个端点(Endpoint)就是调用链路中的一个资源;
     */
    @GetMapping("/by-url")
    @SentinelResource(value = "byUrl") // value = "byUrl" 该处是指 以'byUrl'方法为资源
    public CommonResponse<String> byUrl() {
        log.info("coming in rate limit controller by url");
        return new CommonResponse<>(0, "", "byUrl");
    }
}