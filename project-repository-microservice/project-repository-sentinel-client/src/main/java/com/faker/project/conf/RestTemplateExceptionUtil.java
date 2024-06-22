package com.faker.project.conf;

import com.alibaba.cloud.sentinel.rest.SentinelClientHttpResponse;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.faker.project.vo.JwtToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;

/**
 * restTemplate 在限流或异常的 操作
 * 该Util保护获取用户token的微服务功能(见SentinelRestTemplateController), 故设置 new SentinelClientHttpResponse(JSON.toJSONString(new JwtToken("***")))
 */
@Slf4j
@SuppressWarnings("unused")
public class RestTemplateExceptionUtil {

    /** 限流后的操作
     * @Param httpExecution 请求的链路逻辑
     * @Param blockException 限流处理方法
     * @Param blockException 异常的信息
     */
    public static SentinelClientHttpResponse handleBlock(HttpRequest req, byte[] body,
                                                         ClientHttpRequestExecution httpExecution, BlockException blockException) {
        log.error("Handle RestTemplate Block Exception, happen in url:{}, exception name:{}", req.getURI().getPath(), blockException.getClass().getCanonicalName());
        return new SentinelClientHttpResponse(JSON.toJSONString(new JwtToken("project-block")));
    }

    /** 异常降级后的操作 */
    public static SentinelClientHttpResponse handleFallback(HttpRequest req, byte[] body,
                                                            ClientHttpRequestExecution execution, BlockException ex) {
        log.error("Handle RestTemplate Fallback Exception, happen in url:{}, exception name:{}", req.getURI().getPath(), ex.getClass().getCanonicalName());
        return new SentinelClientHttpResponse(JSON.toJSONString(new JwtToken("project-block")));
    }
}