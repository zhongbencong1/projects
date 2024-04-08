package com.faker.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.faker.project.entity.EcommerceUser;
import com.faker.project.vo.UserNameAndPassword;

/**
 * jwt 相关服务接口定义
 */
public interface IJWTService extends IService<EcommerceUser> {

    String generateToken(String username, String password) throws Exception;

    String generateToken(String username, String password, int expire) throws Exception;

    String registerUserAndGenerateToken(UserNameAndPassword usernameAndPassword) throws Exception;
}
