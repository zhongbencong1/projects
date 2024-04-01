package com.imooc.ecommerce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.ecommerce.entity.EcommerceUser;
import com.imooc.ecommerce.vo.UserNameAndPassword;

/**
 * jwt 相关服务接口定义
 */
public interface IJWTService extends IService<EcommerceUser> {

    String generateToken(String username, String password) throws Exception;

    String generateToken(String username, String password, int expire) throws Exception;

    String registerUserAndGenerateToken(UserNameAndPassword usernameAndPassword) throws Exception;
}
