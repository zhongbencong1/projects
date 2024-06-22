package com.faker.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.faker.project.entity.LogisticsInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogisticsServiceMapper extends BaseMapper<LogisticsInfo> {
}
