package com.faker.project.controller;

import com.faker.project.transactional.TransactionalLose;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试 事务失效场景
 */
@Slf4j
@RestController
@RequestMapping("/lose-transaction")
public class TestTransactionLose {

    @Autowired
    private TransactionalLose transactionalLose;

    @GetMapping("/wrong-back-for")
    public void wrongRollbackFor() throws Exception {
        log.info("wrong rollback for");
        transactionalLose.wrongRollbackFor();
    }
}
