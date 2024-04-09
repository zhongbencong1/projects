package com.faker.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.faker.project.account.BalanceInfo;
import com.faker.project.entity.ProjectRepositoryBalance;
import com.faker.project.filter.AccessContext;
import com.faker.project.mapper.ProjectBalanceMapper;
import com.faker.project.service.IBalanceService;
import com.faker.project.vo.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 用户地址相关服务接口实现
 * */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class IBalanceServiceImpl extends ServiceImpl<ProjectBalanceMapper, ProjectRepositoryBalance> implements IBalanceService {

    @Override
    public BalanceInfo getCurrentUserBalanceInfo() {
        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();
        BalanceInfo result = new BalanceInfo(loginUserInfo.getId(), 0L);
        ProjectRepositoryBalance balance = this.getById(loginUserInfo.getId());

        if (null != balance) {
            result.setBalance(balance.getBalance());
        } else {
            // 如果还没有用户余额记录, 这里创建出来，余额设定为0即可
            ProjectRepositoryBalance newBalance = new ProjectRepositoryBalance();
            newBalance.setUserId(loginUserInfo.getId());
            newBalance.setBalance(0L);
            log.info("init user balance save result: [{}]", this.saveOrUpdate(newBalance));
        }
        return result;
    }

    @Override
    public BalanceInfo deductBalance(BalanceInfo balanceInfo) {
        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();
        // 扣减用户余额的一个基本原则: 扣减额 <= 当前用户余额
        LambdaQueryWrapper<ProjectRepositoryBalance> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProjectRepositoryBalance::getUserId, loginUserInfo.getId());
        ProjectRepositoryBalance byUserId = this.getOne(queryWrapper);
        if (Objects.isNull(byUserId) || byUserId.getBalance() - balanceInfo.getBalance() < 0) {
            throw new RuntimeException("user balance is not enough!");
        }
        // 扣减用户余额
        byUserId.setBalance(byUserId.getBalance() - balanceInfo.getBalance());
        log.info("deduct balance: [{}], result [{}]", balanceInfo.getBalance(), this.saveOrUpdate(byUserId));
        return new BalanceInfo(byUserId.getUserId(), byUserId.getBalance());
    }
}
