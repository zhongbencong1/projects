package com.faker.project.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 模板代码: gateway 集成 sentinel 实现限流
 * */
@Slf4j
@Configuration
public class SentinelGatewayConfiguration {

    /** 视图解析器 */
    private final List<ViewResolver> viewResolvers;

    /** HTTP 请求和响应数据的编解码配置 */
    private final ServerCodecConfigurer serverCodecConfigurer;

    /** 构造方法 */
    public SentinelGatewayConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider, ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    /** 限流异常处理器, 出现限流异常时, 执行到这个 handler */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
        // 默认会返回错误 message, code 429
        return new SentinelGatewayBlockExceptionHandler(this.viewResolvers, this.serverCodecConfigurer);
    }

    /** 限流过滤器, 是 gateway 全局过滤器, 优先级定义为最高 */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }

    /** 初始化限流规则 */
    @PostConstruct
    public void doInit() {
        log.info("SentinelGatewayConfiguration doInit start");
        // 加载网关限流规则
        initGatewayRules();
        log.info("SentinelGatewayConfiguration doInit finish");
    }

    /** 硬编码网关限流规则 */
    private void initGatewayRules() {
        // 流控规则
        GatewayFlowRule rule = new GatewayFlowRule()
            // 指定限流模式, 根据 route_id 做限流, 默认的模式
            .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_ROUTE_ID)
            // 指定 route_id -> serviceId: 微服务name
            .setResource("project-repository-nacos-client")
            // 按照 QPS 限流
            .setGrade(RuleConstant.FLOW_GRADE_QPS)
            // 统计窗口和限流阈值: 60秒内只允许通过3个访问微服务(project-repository-nacos-client)的请求
            .setIntervalSec(60)
            .setCount(3);

        Set<GatewayFlowRule> rules = new HashSet<>();
        rules.add(rule);

        // 加载到网关中
        GatewayRuleManager.loadRules(rules);
    }


}
