package com.faker.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.faker.project.entity.ProjectOrder;

/**
 * order接口
 */
public interface IOrderService {

    /** 根据用户id获取所有订单 */
    Page<ProjectOrder> findAllByUserId(Long userId, Integer offset, Integer limit, Boolean order);
}
