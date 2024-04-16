package com.faker.project.service.hystrix;

import com.faker.project.service.NacosClientService;
import com.netflix.hystrix.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;

import java.util.Collections;
import java.util.List;

import static com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy.THREAD;

/**
 * 给 NacosClientService 包装 实现熔断降级...
 * Hystrix 舱壁模式的隔离策略:
 *  1. 线程池
 *  2. 信号量: 算法 + 数据结构, 有限状态机
 *  HystrixCommand, 隔离策略是基于信号量实现的
 * */
@Slf4j
public class NacosClientHystrixCommand extends HystrixCommand<List<ServiceInstance>> {

    /** 需要保护的服务 */
    private final NacosClientService nacosClientService;

    /** 方法需要传递的参数 */
    private final String serviceId;

    public NacosClientHystrixCommand(NacosClientService nacosClientService, String serviceId) {
        // 开启线程池隔离策略 (Hystrix默认使用线程池)
        super(
                Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("NacosClientService"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("NacosClientHystrixCommand"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("NacosClientPool"))
                // 线程池 key 配置
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                        .withExecutionIsolationStrategy(THREAD) // 线程池隔离策略
                        .withFallbackEnabled(true)  // 开启降级 会调用重写后的getFallback方法
                        .withCircuitBreakerEnabled(true)    // 开启熔断器
                )
        );

        // 开启信号量隔离策略
//        Setter semaphore =
//                Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("NacosClientService"))
//                .andCommandKey(HystrixCommandKey.Factory.asKey("NacosClientHystrixCommand"))
//                .andCommandPropertiesDefaults(
//                        HystrixCommandProperties.Setter()
//                        .withCircuitBreakerRequestVolumeThreshold(10)
//                        .withCircuitBreakerSleepWindowInMilliseconds(5000)
//                        .withCircuitBreakerErrorThresholdPercentage(50)
//                        .withExecutionIsolationStrategy(SEMAPHORE)  // 指定使用信号量隔离
//                        //.....
//                );

        this.nacosClientService = nacosClientService;
        this.serviceId = serviceId;
    }

    /** Hystrix要保护的方法调用写在 run 方法中 */
    @Override
    protected List<ServiceInstance> run() throws Exception {
        log.info("NacosClientService In Hystrix Command to Get Service Instance: [{}], [{}]", this.serviceId, Thread.currentThread().getName());
        return this.nacosClientService.getNacosClientInfo(this.serviceId);
    }

    /** 降级处理策略 重写getFallback方法 */
    @Override
    protected List<ServiceInstance> getFallback() {
        log.warn("NacosClientService run error: [{}], [{}]", this.serviceId, Thread.currentThread().getName());
        return Collections.emptyList();
    }
}
