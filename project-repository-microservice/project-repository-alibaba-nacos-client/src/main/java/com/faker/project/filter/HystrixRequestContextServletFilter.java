package com.faker.project.filter;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 初始化 Hystrix 请求上下文环境
 * */
@Slf4j
@Component
@WebFilter(filterName = "HystrixRequestContextServletFilter", urlPatterns = "/*", asyncSupported = true)
public class HystrixRequestContextServletFilter implements Filter {

    /** 初始化 Hystrix */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 初始化 Hystrix 请求上下文context， 在不同的 context 中缓存是不共享的，这个初始化是必须的
        HystrixRequestContext context = HystrixRequestContext.initializeContext();

        // 配置 hystrix的并发缓冲策略配置项
        try {
            hystrixConcurrencyStrategyConfig();
            chain.doFilter(request, response); // 请求正常通过
        } finally {
            // 关闭 Hystrix 请求上下文
            context.shutdown();
        }
    }

    /** 配置 Hystrix 的并发策略 (项目引入sleuth的话 会导致默认并发策略被修改, 所以需要配置) */
    public void hystrixConcurrencyStrategyConfig() {
        try {
            HystrixConcurrencyStrategy target = HystrixConcurrencyStrategyDefault.getInstance(); // 默认并发策略
            HystrixConcurrencyStrategy strategy = HystrixPlugins.getInstance().getConcurrencyStrategy(); // 当前并发策略

            // 项目引入sleuth的话 会导致默认并发策略被修改，所以判断当前并发策略是否与默认策略相同
            if (strategy instanceof HystrixConcurrencyStrategyDefault) {
                // 如果是需要的配置的默认并发策略，return即可
                return;
            }

            // 将原来其他的配置保存下来
            HystrixCommandExecutionHook commandExecutionHook = HystrixPlugins.getInstance().getCommandExecutionHook();
            HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance().getEventNotifier(); // 事件通知器
            HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance().getMetricsPublisher(); // 统计发布器
            HystrixPropertiesStrategy propertiesStrategy = HystrixPlugins.getInstance().getPropertiesStrategy(); // 属性相关

            // 先重置, 再把我们自定义的配置与原来的配置写回去
            HystrixPlugins.reset(); // 重置
            HystrixPlugins.getInstance().registerConcurrencyStrategy(target); // 重新设置并发策略(原来默认的策略)
            HystrixPlugins.getInstance().registerCommandExecutionHook(commandExecutionHook);
            HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
            HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
            HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);

            log.info("config hystrix concurrency strategy success");
        } catch (Exception ex) {
            log.error("Failed to register Hystrix Concurrency Strategy: [{}]", ex.getMessage(), ex);
        }
    }
}
