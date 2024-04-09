package com.faker.project.service;

import cn.hutool.crypto.digest.MD5;
import com.faker.project.entity.RepositoryUser;
import com.faker.project.mapper.EcommerceUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class RepositoryUserTest {

    @Autowired
    private EcommerceUserMapper ecommerceUserMapper;

    @Test
    public void createRepositoryUser() {
        RepositoryUser repositoryUser = new RepositoryUser();
        repositoryUser.setUsername("rd");
        repositoryUser.setPassword(MD5.create().digestHex("12345678"));
        repositoryUser.setExtraInfo("extra");
        int insert = ecommerceUserMapper.insert(repositoryUser);
        log.info("save user result is [{}]", insert);

    }
}
