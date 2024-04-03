package com.faker.project.filter;

import com.faker.project.constant.GatewayConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 缓存请求 body 方便后续filter获取 的全局过滤器
 * Spring WebFlux
 * */
@Slf4j
@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE + 1)
@SuppressWarnings("all")
public class GlobalCacheRequestBodyFilter implements GlobalFilter{

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 没有请求头或非授权接口 放行该过滤器
        boolean isloginOrRegister = exchange.getRequest().getURI().getPath().contains(GatewayConstant.LOGIN_URI)
                || exchange.getRequest().getURI().getPath().contains(GatewayConstant.REGISTER_URI);
        if (null == exchange.getRequest().getHeaders().getContentType() || !isloginOrRegister) {
            return chain.filter(exchange);
        }

        // DataBufferUtils.join 拿到请求中的数据 --> DataBufferUtils.join(exchange.getRequest().getBody())
        return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
            // 确保数据缓冲区不被释放： DataBufferUtils.retain
            DataBufferUtils.retain(dataBuffer);
            // defer just 都是去创建数据源, 得到当前请求数据的副本
            Flux<DataBuffer> cachedFlux = Flux.defer(() ->
                    Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
            // 重新包装 ServerHttpRequest, 重写 getBody 方法, 能够返回请求数据
            ServerHttpRequest mutatedRequest =
                    new ServerHttpRequestDecorator(exchange.getRequest()) {
                        @Override
                        public Flux<DataBuffer> getBody() {
                            return cachedFlux;
                        }
                    };
            // 将包装之后的 ServerHttpRequest 向下继续传递
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        });
    }
}
