package com.faker.project.service;

import com.alibaba.fastjson.JSON;
import com.faker.project.account.BalanceInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用于余额相关服务测试
 * */
@Slf4j
public class BalanceServiceTest extends BaseTest {

    @Autowired
    private IBalanceService balanceService;

    /** 测试获取当前用户的余额信息 */
    @Test
    public void testGetCurrentUserBalanceInfo() {
        log.info("test get current user balance info: [{}]", JSON.toJSONString(balanceService.getCurrentUserBalanceInfo()));
    }

    /** 测试扣减余额 */
    @Test
    public void testDeductBalance() {
        BalanceInfo balanceInfo = new BalanceInfo();
        balanceInfo.setUserId(loginUserInfo.getId());
        balanceInfo.setBalance(1000L);
        log.info("test deduct balance: [{}]", JSON.toJSONString(balanceService.deductBalance(balanceInfo)));
    }
}
