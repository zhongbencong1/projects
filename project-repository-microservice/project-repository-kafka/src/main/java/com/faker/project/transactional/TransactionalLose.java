package com.faker.project.transactional;

import com.faker.project.transactional.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * 事务失效场景: 1.异常捕获类型错误; 2.同一个类中方法调用; 3.try catch吞掉异常;
 */
@Slf4j
@Service
public class TransactionalLose {

    @Autowired
    private UserMapper userMapper;

//    @Transactional // @Transactional注解默认捕获运行时异常(非受查异常)
    @Transactional(rollbackFor = Exception.class)  // @Transactional 会捕获参数异常类及其子类异常
    public void wrongRollbackFor() throws Exception {
        User user = new User(1L, "faker_wrong");
        userMapper.insert(user);

        // 执行后续代码时, 抛出异常, @Transactional注解默认捕获运行时异常, 该处抛出 非运行时异常(受查异常)IOException
        throw new IOException("some IOException");
    }
}
