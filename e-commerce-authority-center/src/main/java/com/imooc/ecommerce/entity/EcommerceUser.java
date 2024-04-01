package com.imooc.ecommerce.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName(value = "t_ecommerce_user")
public class EcommerceUser implements Serializable {

    @TableId
    private Long id;

    @TableField("username")
    private String username;

    /* md5 密码*/
    @TableField("password")
    private String password;

    @TableField("extra_info")
    private String extraInfo;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;
}
