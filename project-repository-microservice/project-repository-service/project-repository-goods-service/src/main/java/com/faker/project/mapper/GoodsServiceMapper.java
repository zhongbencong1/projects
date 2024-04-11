package com.faker.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.faker.project.entity.RepositoryGoods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsServiceMapper extends BaseMapper<RepositoryGoods> {
}
