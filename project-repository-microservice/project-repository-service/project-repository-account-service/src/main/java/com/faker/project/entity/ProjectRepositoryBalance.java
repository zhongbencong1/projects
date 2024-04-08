package com.faker.project.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * 用户账户余额表实体类定义
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("project_repository_balance")
public class ProjectRepositoryBalance {

    /** 自增主键 */
    @TableField("id")
    private Long id;

    /** 用户 id */
    @TableField("user_id")
    private Long userId;

    /** 账户余额 */
    @TableField("balance")
    private Long balance;

    /** 创建时间 */
    @TableField("create_time")
    private Date createTime;

    /** 更新时间 */
    @TableField("update_time")
    private Date updateTime;
}
