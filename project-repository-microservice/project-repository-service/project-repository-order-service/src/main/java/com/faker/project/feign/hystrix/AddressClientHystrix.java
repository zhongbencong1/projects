package com.faker.project.feign.hystrix;

import com.alibaba.fastjson.JSON;
import com.faker.project.account.AddressInfo;
import com.faker.project.common.TableId;
import com.faker.project.feign.SecureAddressClient;
import com.faker.project.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * 账户服务熔断降级兜底策略
 * */
@Slf4j
@Component
public class AddressClientHystrix implements SecureAddressClient {

    @Override
    public CommonResponse<AddressInfo> getAddressInfoByTablesId(TableId tableId) {
        log.error("account client feign request error in order service get address info error: {}", JSON.toJSONString(tableId));
        return new CommonResponse<>(-1, "account client feign request error in order service",
                new AddressInfo(-1L, Collections.emptyList())
        );
    }
}
