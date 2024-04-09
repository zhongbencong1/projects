package com.faker.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.faker.project.entity.ProjectRepositoryAddress;
import org.apache.ibatis.annotations.Mapper;

/**
 * EcommerceAddress Dao 接口定义
 * */
@Mapper
public interface ProjectAddressMapper extends BaseMapper<ProjectRepositoryAddress> {
}
