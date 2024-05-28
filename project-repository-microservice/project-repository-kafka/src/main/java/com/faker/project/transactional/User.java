package com.faker.project.transactional;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("transaction_user")
public class User {

    /** 自增主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;
}