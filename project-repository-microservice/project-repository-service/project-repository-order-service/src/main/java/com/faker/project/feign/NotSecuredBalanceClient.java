package com.faker.project.feign;

import com.faker.project.account.BalanceInfo;
import com.faker.project.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户账户服务 Feign 接口: contextId:client唯一id, value:调用的微服务的name.
 * NotSecuredBalanceClient: 不安全的调用, 调用失败需要抛出异常而非吞掉异常
 * */
@FeignClient(
        contextId = "NotSecuredBalanceClient",
        value = "project-repository-account-service"
)
public interface NotSecuredBalanceClient {

    @RequestMapping(
            value = "/project-account-service/balance/deduct-balance",
            method = RequestMethod.PUT
    )
    CommonResponse<BalanceInfo> deductBalance(@RequestBody BalanceInfo balanceInfo);
}
