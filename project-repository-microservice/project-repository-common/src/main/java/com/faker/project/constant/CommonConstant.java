package com.faker.project.constant;

/**
 * 通用模块常量定义
 */
public interface CommonConstant {

    /** RSA 公钥, 通过 RSATest 类生成 */
    String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsjd2eK0V1DU4ik5dozEAgviEZLjOc2YdHWdsXo3VA80Ld3wZc/LqzkSVLNYn" +
            "J7g+m5lO89sSoxoycchLAtMbH44tYKQf4JQ9JghhTIPlrV0W8g8PoUYJookpdQIF8JuiQe4QAqEAzuPYmtaH5aL0F4WP6MJN6udFnZY1WKoLBD0nEvPH4" +
            "dF7xcRN5xGKGDJJryFAsKMUrHkUdjm5GWWBuwJGIznKuHauvlj0/jl+PWJzSxvhbdZQKUoq8cPol0oiKJvJEEfviCWfMJzwMYWxUGlKjXbYL0tYBaP3oE" +
            "fv2n/ZOAQngnnevEpyd3YBRjXB5LWZKk+qqML3zhzqijFAjQIDAQAB";

    /** JWT 中存储用户信息的 key */
    String JWT_USER_INFO_KEY = "repository-user";

    /** 授权中心的 service-id */
    String AUTHORITY_CENTER_SERVICE_ID = "project-repository-authority-center";

}
