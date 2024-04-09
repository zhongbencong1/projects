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

    // 与MyMetaObjectHandler类 实现自动填充 创建时间create_time 和 修改时间update_time
    @TableField(value = "create_time", fill = FieldFill.INSERT_UPDATE)
    private Long createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT)
    private Long updateTime;
}
