package com.faker.project.transactional.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.faker.project.transactional.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 在启动类上加 @MapperScan() 指定该userMapper, 但是不生效: 测试类会报找不到 bean: userMapper
 * 但是在该接口上加Mapper注解, 测试类可以正常运行, 不清楚原因是什么
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}