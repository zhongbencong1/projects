package com.faker.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录的用户信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserInfo {

    private Long id;

    private String name;
}
