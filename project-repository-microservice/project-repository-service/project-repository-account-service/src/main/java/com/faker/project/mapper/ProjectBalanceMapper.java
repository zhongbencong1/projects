package com.faker.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.faker.project.entity.ProjectRepositoryBalance;
import org.apache.ibatis.annotations.Mapper;

/**
 * <h1>EcommerceBalance Dao 接口定义</h1>
 * */
@Mapper
public interface ProjectBalanceMapper extends BaseMapper<ProjectRepositoryBalance> {

    /** 根据 userId 查询 EcommerceBalance 对象 */
    ProjectRepositoryBalance findByUserId(Long userId);
}
