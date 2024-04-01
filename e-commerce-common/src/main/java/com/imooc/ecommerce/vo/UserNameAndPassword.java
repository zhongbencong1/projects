package com.imooc.ecommerce.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户名和密码
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNameAndPassword {
    private String username;

    private String password;
}
