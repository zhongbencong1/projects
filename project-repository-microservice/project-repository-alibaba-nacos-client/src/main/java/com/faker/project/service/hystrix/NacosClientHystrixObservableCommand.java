package com.faker.project.service.hystrix;

import com.alibaba.fastjson.JSON;
import com.faker.project.service.NacosClientService;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import rx.Observable;
import rx.Subscriber;

import java.util.Collections;
import java.util.List;

/**
 * Hystrix 舱壁模式的隔离策略:
 *  1. 线程池
 *  2. 信号量: 算法 + 数据结构, 有限状态机
 * HystrixObservableCommand, 隔离策略是基于信号量实现的
 * */
@Slf4j
public class NacosClientHystrixObservableCommand extends HystrixObservableCommand<List<ServiceInstance>> {

    /** 要保护的服务 */
    private final NacosClientService nacosClientService;

    /** 方法需要传递的参数 */
    private final List<String> serviceIds;

    public NacosClientHystrixObservableCommand(NacosClientService nacosClientService, List<String> serviceIds) {
        super(
                Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("NacosClientService"))
                        .andCommandKey(HystrixCommandKey.Factory.asKey("NacosClientHystrixObservableCommand"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withFallbackEnabled(true)          // 开启降级
                        .withCircuitBreakerEnabled(true)    // 开启熔断器
                )
        );

        this.nacosClientService = nacosClientService;
        this.serviceIds = serviceIds;
    }

    /** Hystrix要保护的方法调用写在 construct() */
    @Override
    protected Observable<List<ServiceInstance>> construct() {

        return Observable.create(new Observable.OnSubscribe<List<ServiceInstance>>() {
            // Observable 有三个关键的事件方法, 分别是 onNext(发起服务调用)、onCompleted(服务调用完成)、onError(服务调用异常)
            @Override
            public void call(Subscriber<? super List<ServiceInstance>> subscriber) {
                try {
                    if (!subscriber.isUnsubscribed()) {
                        log.info("subscriber command task: [{}], [{}]", JSON.toJSONString(serviceIds), Thread.currentThread().getName());
                        serviceIds.forEach(s -> subscriber.onNext(nacosClientService.getNacosClientInfo(s)));
                        subscriber.onCompleted();
                        log.info("command task completed: [{}], [{}]", JSON.toJSONString(serviceIds), Thread.currentThread().getName());
                    }
                } catch (Exception ex) {
                    subscriber.onError(ex);
                }
            }
        });
    }

    /** 服务降级 */
    @Override
    protected Observable<List<ServiceInstance>> resumeWithFallback() {
        return Observable.create(subscriber -> {
            try {
                if (!subscriber.isUnsubscribed()) {
                    log.info("(fallback) subscriber command task: [{}], [{}]", JSON.toJSONString(serviceIds), Thread.currentThread().getName());
                    subscriber.onNext(Collections.emptyList());
                    subscriber.onCompleted();
                    log.info("(fallback) command task completed: [{}], [{}]", JSON.toJSONString(serviceIds), Thread.currentThread().getName());
                }
            } catch (Exception ex) {
                subscriber.onError(ex);
            }
        });
    }
}
