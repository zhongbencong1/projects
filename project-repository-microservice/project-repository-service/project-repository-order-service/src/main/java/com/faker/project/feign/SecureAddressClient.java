package com.faker.project.feign;

import com.faker.project.account.AddressInfo;
import com.faker.project.common.TableId;
import com.faker.project.feign.hystrix.AddressClientHystrix;
import com.faker.project.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户账户服务 Feign 接口(安全)
 * */
@FeignClient(
        contextId = "AddressClient",
        value = "project-repository-account-service",
        fallback = AddressClientHystrix.class
)
public interface SecureAddressClient {

    /** 根据 id 查询地址信息 */
    @RequestMapping(
            value = "/project-account-service/address/address-info",
            method = RequestMethod.POST
    )
    CommonResponse<AddressInfo> getAddressInfoByTablesId(@RequestBody TableId tableId);
}
