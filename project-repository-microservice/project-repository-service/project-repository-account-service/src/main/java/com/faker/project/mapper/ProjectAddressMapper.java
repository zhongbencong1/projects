package com.faker.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.faker.project.entity.ProjectRepositoryAddress;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * EcommerceAddress Dao 接口定义
 * */
@Mapper
public interface ProjectAddressMapper extends BaseMapper<ProjectRepositoryAddress> {

    /** 根据 用户 id 查询地址信息 */
    List<ProjectRepositoryAddress> findAllByUserId(Long userId);
}
