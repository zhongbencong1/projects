package com.faker.project.service;

import com.faker.project.account.BalanceInfo;

/**
 * 用于余额相关的服务接口定义
 * */
public interface IBalanceService {

    /** 获取当前用户余额信息 (threadlocal中获取用户信息) */
    BalanceInfo getCurrentUserBalanceInfo();

    /** 扣减用户余额, balanceInfo代表扣减的余额 */
    BalanceInfo deductBalance(BalanceInfo balanceInfo);
}
