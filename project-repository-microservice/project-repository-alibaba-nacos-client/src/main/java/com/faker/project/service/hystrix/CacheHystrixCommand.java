package com.faker.project.service.hystrix;

import com.faker.project.service.NacosClientService;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;

import java.util.Collections;
import java.util.List;

/**
 * 带有缓存功能的 Hystrix
 * */
@Slf4j
public class CacheHystrixCommand extends HystrixCommand<List<ServiceInstance>> {

    /** 需要保护的服务 */
    private final NacosClientService nacosClientService;

    /** 方法需要传递的参数 */
    private final String serviceId;

    private static final HystrixCommandKey CACHED_KEY = HystrixCommandKey.Factory.asKey("CacheHystrixCommand");

    public CacheHystrixCommand(NacosClientService nacosClientService, String serviceId) {
        super(
                Setter.withGroupKey(HystrixCommandGroupKey.Factory.
                        asKey("CacheHystrixCommandGroup")).andCommandKey(CACHED_KEY));
        this.nacosClientService = nacosClientService;
        this.serviceId = serviceId;
    }

    /** 被保护方法 */
    @Override
    protected List<ServiceInstance> run() throws Exception {
        log.info("CacheHystrixCommand In Hystrix Command to get service instance: [{}], [{}]",
                this.serviceId, Thread.currentThread().getName());
        return this.nacosClientService.getNacosClientInfo(this.serviceId);
    }

    /** Hystrix 缓存请求的key (标识唯一的值)
     * 1.减少重复的请求数 降低并发
     * 2.同一个上下文中, 数据幂等
     */
    @Override
    protected String getCacheKey() {
        return serviceId;
    }

    /** Hystrix降级 */
    @Override
    protected List<ServiceInstance> getFallback() {
        return Collections.emptyList();
    }

    /** 根据 缓存key 清理 在一次Hystrix请求上下文中的缓存 (缓存异常时需要清理) */
    public static void clearRequestCache(String serviceId) {
        HystrixRequestCache.getInstance(CACHED_KEY, HystrixConcurrencyStrategyDefault.getInstance()).clear(serviceId);
        log.info("flush request cache in hystrix command: [{}], [{}]", serviceId, Thread.currentThread().getName());
    }
}
