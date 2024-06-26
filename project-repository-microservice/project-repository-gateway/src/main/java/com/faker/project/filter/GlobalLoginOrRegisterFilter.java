package com.faker.project.filter;

import com.alibaba.fastjson.JSON;
import com.faker.project.constant.CommonConstant;
import com.faker.project.util.TokenParseUtil;
import com.faker.project.vo.JwtToken;
import com.faker.project.vo.LoginUserInfo;
import com.faker.project.vo.UserNameAndPassword;
import com.faker.project.constant.GatewayConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 全局登录鉴权过滤器
 * */
@Slf4j
@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE + 2)
public class GlobalLoginOrRegisterFilter implements GlobalFilter {

    /** 注册中心客户端, 可以从注册中心中获取服务实例信息 */
    private final LoadBalancerClient loadBalancerClient;
    private final RestTemplate restTemplate;

    public GlobalLoginOrRegisterFilter(LoadBalancerClient loadBalancerClient, RestTemplate restTemplate) {
        this.loadBalancerClient = loadBalancerClient;
        this.restTemplate = restTemplate;
    }

    /**
     * 登录、注册、鉴权
     * 1. 如果是登录或注册, 则去授权中心拿到 Token 并返回给客户端
     * 2. 如果是访问其他的服务, 则鉴权, 没有权限返回 401
     * */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();
        // 1. 如果是登录
        if (path.contains(GatewayConstant.LOGIN_URI)) {
            // 去授权中心拿 token
            String token = getTokenFromAuthorityCenter(request, GatewayConstant.AUTHORITY_CENTER_TOKEN_URL_FORMAT);
            // header 中不能设置 null
            response.getHeaders().add(CommonConstant.JWT_USER_INFO_KEY, null == token ? "null" : token);
            response.setStatusCode(HttpStatus.OK);
            return response.setComplete();
        }

        // 2. 如果是注册
        if (path.contains(GatewayConstant.REGISTER_URI)) {
            // 去授权中心拿 token: 先创建用户, 再返回 Token
            String token = getTokenFromAuthorityCenter(request, GatewayConstant.AUTHORITY_CENTER_REGISTER_URL_FORMAT);
            response.getHeaders().add(CommonConstant.JWT_USER_INFO_KEY, null == token ? "null" : token);
            response.setStatusCode(HttpStatus.OK);
            return response.setComplete();
        }

        // 3. 访问其他的服务, 则鉴权, 校验是否能够从 Token 中解析出用户信息
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst(CommonConstant.JWT_USER_INFO_KEY);
        LoginUserInfo loginUserInfo = null;

        try {
            loginUserInfo = TokenParseUtil.parseLoginUserInfoFromToken(token);
        } catch (Exception ex) {
            log.error("parse user info from token error: [{}]", ex.getMessage(), ex);
        }

        // 获取不到登录用户信息, 返回 401
        if (null == loginUserInfo) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        // 解析通过, 则放行
        return chain.filter(exchange);
    }

    /** 从授权中心获取 Token */
    private String getTokenFromAuthorityCenter(ServerHttpRequest request, String uriFormat) {
        // service id 就是服务名字, 负载均衡
        ServiceInstance serviceInstance = loadBalancerClient.choose(CommonConstant.AUTHORITY_CENTER_SERVICE_ID);
        log.info("Nacos Client Info: [{}]", JSON.toJSONString(serviceInstance.getMetadata()));

        String requestUrl = String.format(uriFormat, serviceInstance.getHost(), serviceInstance.getPort());
        UserNameAndPassword requestBody = JSON.parseObject(parseBodyFromRequest(request), UserNameAndPassword.class);
        log.info("login request url and body: [{}], [{}]", requestUrl, JSON.toJSONString(requestBody));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JwtToken token = restTemplate.postForObject(
                requestUrl,
                new HttpEntity<>(JSON.toJSONString(requestBody), headers),
                JwtToken.class
        );
        return (null != token) ? token.getToken() : null;
    }

    /** 从 Post 请求中获取到请求数据 */
    private String parseBodyFromRequest(ServerHttpRequest request) {
        // 获取 GlobalCacheRequestBodyFilter 緩存的 请求体
        Flux<DataBuffer> body = request.getBody();
        AtomicReference<String> bodyRef = new AtomicReference<>();

        // 订阅缓冲区去消费请求体中的数据
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            // 一定要使用 DataBufferUtils.release 释放掉, 否则, 会出现内存泄露(GlobalCacheRequestBodyFilter DataBufferUtils.retain(dataBuffer) 一直会占用内存存储)
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });

        // 获取 request body 的string
        return bodyRef.get();
    }
}
