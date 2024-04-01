package com.imooc.ecommerce.service;

import cn.hutool.crypto.digest.MD5;
import com.imooc.ecommerce.mapper.EcommerceUserMapper;
import com.imooc.ecommerce.entity.EcommerceUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
