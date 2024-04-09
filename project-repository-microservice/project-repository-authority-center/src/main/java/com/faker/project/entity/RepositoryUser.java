package com.faker.project.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表实体类定义
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain=true)
@TableName(value = "repository_user")
public class RepositoryUser implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("username")
    private String username;

    /* md5 密码*/
    @TableField("password")
    private String password;

    @TableField("extra_info")
    private String extraInfo;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;
}
