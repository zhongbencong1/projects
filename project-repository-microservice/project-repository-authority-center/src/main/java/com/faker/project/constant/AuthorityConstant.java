package com.faker.project.constant;

/**
 * 授权使用的常量信息
 */
public interface AuthorityConstant {

    /* RSA私钥 仅授权中心使用 不暴露给任何客户端, 通过 RSATest 类生成 */
    String PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCyN3Z4rRXUNTiKTl2jMQCC+IRkuM5zZh0dZ2xejdUDzQt3fBlz8urOR" +
            "JUs1icnuD6bmU7z2xKjGjJxyEsC0xsfji1gpB/glD0mCGFMg+WtXRbyDw+hRgmiiSl1AgXwm6JB7hACoQDO49ia1oflovQXhY/owk3q50WdljVYqgsEPSc" +
            "S88fh0XvFxE3nEYoYMkmvIUCwoxSseRR2ObkZZYG7AkYjOcq4dq6+WPT+OX49YnNLG+Ft1lApSirxw+iXSiIom8kQR++IJZ8wnPAxhbFQaUqNdtgvS1gFo/" +
            "egR+/af9k4BCeCed68SnJ3dgFGNcHktZkqT6qowvfOHOqKMUCNAgMBAAECggEAXUG0ltVL1O28lnMHgLvFnsNY1DP4pF5M4fQauyfQzdpgI7gmwYvd3u34e" +
            "avedWRuLpH8xJJgoES3HN9IkUPSr6KAkE+R9rzbeqlr8NQcPam2the3gBbcN9c6gGX8oblvILzoV9fUSm7kC7IzDrFEtYJIme4DQ+adgSb251yjwyU1VweYWs" +
            "44GgytBs2wLxMTnaA3tI6iE9lFuNSfw3Iu5fx07I7GgXtMLA8+MDcEVMh5mqRq0ZTIlAMMXXYCJd7I3TYY3qUyl20996qk7F1cQqN3xnYgYHJCxQmzRQGqgIR" +
            "Bv5tbmM5t77Y3+X9ywwHSQNUdrYFMkUjbV3s+f5WRwQKBgQDZaiZaY/Mm1UtJCDuIxSv16Nurk5ruWLPCvR9QJOMUtSoG583ffexpMr6gL5UaQuvZd/5riyEO" +
            "wv/XC3CCAK4T7KUDwo5p++M44eBsWicGbFW6EXdwbd7yQG5qFx4qw5E1ZmZsuqUaCMtGT9Hwx/hgTsLCOK+/6gxXFOQU+f5YqQKBgQDR2GvaA0qJIbSFWDlm" +
            "YMAq1PqUnWzJSBXtkt2U89aJkTQeI+jT84krYNk2o2WMGJXgMe8A0lVFZRGjAi6OnaZDFeLPHzAwVttjmTdaNa3CtC8TqE9RXyLjsWEGo/R9RrdxV6lwwf5P70" +
            "oGZlH2xAv7+Bnvz+fzzY7OOg+of8pjRQKBgQCkjSTOEQv9fj3m3qZAlAr7qexo5eTDNLuJTp6eh+yexaczZsp4ttPxowiIMLtHDxN2ms0jFEBG7eu5FN1oZ59Z" +
            "UShruhsTuCN37+1t1KXoxKH1nDasD1xIOd/Xx/t7Hl2O7xviezluhO1lIBCjjpY8PiBF46WROVFxr0wZXETXUQKBgQCw52NcwmOGobx+oXaaYTv/n9azjNdWBIg" +
            "/5Q0Z8kpNZPTr59fzPp7Hy/iyQCNubJeFDnNvXOH4A5FPu5omLq8oZP31IFTOm5qOJ29rhE89EguR5dUgQzS1gSbLo1KMfh5N1doJLfHHz3G3XXEBuBpmS60J5" +
            "1qixQr7viTPJxQmhQKBgQCxwYXEWp/mSv8upMDfYNb5J7qE26rAJuOJRGXHVhWJ2wVYIooUyWtrJPW1QBAl/pV+lt38q3KjLl+hTB8vEbZeXe6G+c9jyk0UBSj" +
            "PQzVZIPjhuVM1wBL9n9F+Y9Z9cgh+PaSWmEsIKevHJIuBndfoy1UWHlUyk1ERNPONAOTpTg==";

    /* 默认的token 过期时间*/
    Integer DEFAULT_EXPIRE_DAY = 1;
}
