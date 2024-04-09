package com.faker.project.service;

import com.alibaba.fastjson.JSON;
import com.faker.project.util.TokenParseUtil;
import com.faker.project.vo.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * JWT 相关服务测试类
 * */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class JWTServiceTest {

    @Autowired
    private IJWTService ijwtService;

    @Test
    public void testGenerateAndParseToken() throws Exception {

        String jwtToken = ijwtService.generateToken(
                "faker",
                "25d55ad283aa400af464c76d713c07ad"
        );
        log.info("jwt token is: [{}]", jwtToken);

        LoginUserInfo userInfo = TokenParseUtil.parseLoginUserInfoFromToken(jwtToken);
        log.info("parse token: [{}]", JSON.toJSONString(userInfo));
    }
}
