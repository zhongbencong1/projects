package com.faker.project.constant;

/**
 * 通用模块常量定义
 */
public interface CommonConstant {

    /** RSA 公钥 */
    String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnV+iGlE1e8Z825G+ChIwRJ2H2jOMCBu" +
            "HV7BPrUE8dAGjqAlRtCaxMyJw7NV9NIUl/rY7RWBUQwelkGmGuQomnUAFIgN9f8UxSC6G935lo1ZoBVJWYmfs5ToXLz+fQugmqHZvF+Vc5l" +
            "UEo1YapeiaymkOxDORMGjzQBoxoBt316IAwNEPIvcV+F6T+WNFJX/p5Xj48Z1rtmbOQ8ffF+pEWKZGsYg/9b+pKiqFJtuyHqwj/9oxFBE98" +
            "MCu5RfK6M7Ff9/1dyNen1HKjI7Awj8ZnSceVUldcXEdnP89YagevbhtSl/+CvCsKwHq5+ZLkcuONSxE4dIFWTjxA92wJjYf9wIDAQAB";

    /** JWT 中存储用户信息的 key */
    String JWT_USER_INFO_KEY = "e-commerce-user";

    /** 授权中心的 service-id */
    String AUTHORITY_CENTER_SERVICE_ID = "e-commerce-authority-center";

}
