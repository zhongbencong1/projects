package com.faker.project.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
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

    /** api分组常量 */
    public static final String API_GROUP_ONE = "nacos-client-api-1";
    public static final String API_GROUP_TWO = "nacos-client-api-2";

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
        // 加载自定义限流异常处理器
        initBlockHandler();
        log.info("SentinelGatewayConfiguration doInit finish");
    }

    /** 硬编码网关限流规则 */
    private void initGatewayRules() {
        // 流控规则
        GatewayFlowRule rule = new GatewayFlowRule()
            // 指定限流模式, 根据 route_id 做限流, 默认的模式
            .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_ROUTE_ID)
            // 指定 route_id -> serviceId: 微服务name 即将整个微服务的接口添加限流
            .setResource("project-repository-nacos-client")
            // 按照 QPS 限流
            .setGrade(RuleConstant.FLOW_GRADE_QPS)
            // 统计窗口和限流阈值: 60秒内只允许通过3个访问微服务(project-repository-nacos-client)的请求
            .setIntervalSec(60)
            .setCount(3);

        Set<GatewayFlowRule> rules = new HashSet<>();
//        rules.add(rule);

        // 限流分组, Sentinel 先去找规则定义(GatewayFlowRule实例), 再去找规则中定义的分组: 根据分组名称(resource属性)找分组(ApiDefinition实例)
        rules.add(new GatewayFlowRule(API_GROUP_ONE).setCount(3).setIntervalSec(60));
        rules.add(new GatewayFlowRule(API_GROUP_TWO).setCount(1).setIntervalSec(60));

        // 加载到网关中
        GatewayRuleManager.loadRules(rules);
        // 加载限流分组
        initCustomizedApis();
    }

    /** 自定义限流异常处理器 */
    private void initBlockHandler() {
        // 自定义 BlockRequestHandler
        BlockRequestHandler blockRequestHandler = (serverWebExchange, throwable) -> {
            log.error("SentinelGatewayConfiguration initBlockHandler trigger gateway sentinel rule");
            JSONObject result = new JSONObject();
            result.put("code", String.valueOf(HttpStatus.TOO_MANY_REQUESTS.value()));
            result.put("message", HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
            result.put("route", "project-repository-nacos-client");

            return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                    .contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(result));
        };

        // 设置自定义限流异常处理器
        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }

    /**
     * 硬编码网关限流分组
     * 1. 最大限制 == 微服务级别的限制(相当于没有分组)
     * 2. 具体的分组
     */
    private void initCustomizedApis() {
        Set<ApiDefinition> definitions = new HashSet<>();

        // nacos-client-api 组, 最大的限制== 微服务级别的限制(相当于没有分组)
        ApiDefinition api = new ApiDefinition("nacos-client-api").setPredicateItems(
                new HashSet<ApiPredicateItem>() {
                    {
                        // 模糊匹配 /repository/project-repository-nacos-client 及其子路径的所有请求; 匹配方式:前缀匹配
                        add(new ApiPathPredicateItem().setPattern("/repository/project-repository-nacos-client/**")
                                .setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
                    }
                });

        // API分组1
        ApiDefinition api1 = new ApiDefinition(API_GROUP_ONE).setPredicateItems(
                new HashSet<ApiPredicateItem>() {
                    {
                        // 精确匹配url: /repository/project-repository-nacos-client/nacos-client/service_instance
                        add(new ApiPathPredicateItem().setPattern("/repository/project-repository-nacos-client/nacos-client/service_instance"));
                    }
                });

        // API分组2
        ApiDefinition api2 = new ApiDefinition(API_GROUP_TWO).setPredicateItems(
                new HashSet<ApiPredicateItem>() {
                    {
                        // 精确匹配url: /repository/project-repository-nacos-client/nacos-client/project-config
                        add(new ApiPathPredicateItem().setPattern("/repository/project-repository-nacos-client/nacos-client/project-config"));
                    }
                });

        definitions.add(api1);
        definitions.add(api2);

        // 加载限流分组
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }
}
