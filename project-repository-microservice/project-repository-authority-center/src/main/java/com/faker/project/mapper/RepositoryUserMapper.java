package com.faker.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.faker.project.entity.RepositoryUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RepositoryUserMapper extends BaseMapper<RepositoryUser> {
}
