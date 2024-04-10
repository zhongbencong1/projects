package com.faker.project.constant;

/**
 * 网关常量定义
 * */
public interface GatewayConstant {

    /** 登录 uri */
    String LOGIN_URI = "/project-repository/login";

    /** 注册 uri */
    String REGISTER_URI = "/project-repository/register";

    /** 去授权中心拿到登录 token 的 uri 格式化接口 */
    String AUTHORITY_CENTER_TOKEN_URL_FORMAT =
            "http://%s:%s/project-repository-authority-center/authority/generate_token";

    /** 去授权中心注册并拿到 token 的 uri 格式化接口 */
    String AUTHORITY_CENTER_REGISTER_URL_FORMAT =
            "http://%s:%s/project-repository-authority-center/authority/register_token";
}
