package com.faker.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.faker.project.entity.ProjectOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderServiceMapper extends BaseMapper<ProjectOrder> {
}
