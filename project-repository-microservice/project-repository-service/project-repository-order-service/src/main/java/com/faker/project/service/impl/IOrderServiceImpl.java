package com.faker.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.faker.project.entity.PageSimpleOrderDetail;
import com.faker.project.entity.ProjectOrder;
import com.faker.project.mapper.OrderServiceMapper;
import com.faker.project.order.OrderInfo;
import com.faker.project.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * order实现类
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class IOrderServiceImpl extends ServiceImpl<OrderServiceMapper, ProjectOrder> implements IOrderService {

    @Override
    public Page<ProjectOrder> findAllByUserId(Long userId, Integer offset, Integer limit, Boolean order) {
        log.info("findAllByUserId input param: userId:{}, offset:{}, limit:{}, order:{}.", userId, offset, limit, order);
        LambdaQueryWrapper<ProjectOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProjectOrder::getUserId, userId);
        queryWrapper.orderBy(Boolean.TRUE, order, ProjectOrder::getUserId);
        Page<ProjectOrder> pageInfo = new Page<>(offset, limit);
        Page<ProjectOrder> result = this.page(pageInfo, queryWrapper);
        log.info("findAllByUserId output param: result.size:{}", result.getSize());
        return result;
    }

    @Override
    public Long createOrder(OrderInfo orderInfo) {
        return null;
    }

    @Override
    public PageSimpleOrderDetail getOrderListByPage(Integer offset, Integer limit, Boolean order) {
        return null;
    }
}
