package com.faker.project.service;

import cn.hutool.crypto.digest.MD5;
import com.faker.project.entity.EcommerceUser;
import com.faker.project.mapper.EcommerceUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class EcommerceUserTest {

    @Autowired
    private EcommerceUserMapper ecommerceUserMapper;

    @Test
    public void createEcommerceUser() {
        EcommerceUser ecommerceUser = new EcommerceUser();
        ecommerceUser.setUsername("rd");
        ecommerceUser.setUsername(MD5.create().digestHex("12345678"));
        ecommerceUser.setExtraInfo("extra");
        int insert = ecommerceUserMapper.insert(ecommerceUser);
        log.info("save user result is [{}]", insert);

    }
}
