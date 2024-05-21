package com.faker.project;

import com.faker.project.transactional.User;
import com.faker.project.transactional.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * spring事务管理 测试
 */
@Slf4j
@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
public class TransactionTest {

    @Autowired
    private UserMapper userMapper;

    /**
     * 只有 @Test 注解, 执行完后会新增数据, 不会回滚;
     * 加上@Transactional, 执行完成后, 对数据库无影响, 会回滚插入的数据;
     * 注解@Transactional 在类上, 但是不会滚某个单元测试, 在单元测试方法上加上 @Rollback(value = false)
     */
    @Transactional
    @Test
    public void testCreateUser() {
        log.info("----- testCreateUser method test ------");
        User user = new User();
        user.setName("faker");
        int insert = userMapper.insert(user);
        log.info("userMapper insert result: {}", insert);
    }

    /**
     * 注解@Transactional 在类上, 但是不会滚某个单元测试, 在单元测试方法上加上 @Rollback(value = false)
     */
    @Rollback(value = false)
    public void testCreateUser2() {
        log.info("----- testCreateUser2 method test ------");
        User user = new User();
        user.setName("faker2");
        int insert = userMapper.insert(user);
        log.info("userMapper2 insert result: {}", insert);
    }
}
