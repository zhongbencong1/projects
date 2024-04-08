package com.faker.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.faker.project.entity.ProjectRepositoryBalance;

/**
 * <h1>EcommerceBalance Dao 接口定义</h1>
 * */
public interface ProjectBalanceMapper extends BaseMapper<ProjectRepositoryBalance> {

    /** 根据 userId 查询 EcommerceBalance 对象 */
    ProjectRepositoryBalance findByUserId(Long userId);
}
