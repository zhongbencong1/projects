package com.faker.project.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("project_order")
public class ProjectOrder {

    /** 自增主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户id */
    @TableField("user_id")
    private Long userId;

    /** 用户地址id */
    @TableField("address_id")
    private Long addressId;

    /** 订单详情 json存储 */
    @TableField("order_detail")
    private String orderDetail;

    /** 创建时间 */
    @TableField(value = "create_time", fill = FieldFill.INSERT_UPDATE)
    private Long createTime;

    /** 更新时间 */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;

    public ProjectOrder(Long userId, Long addressId, String orderDetail) {
        this.userId = userId;
        this.addressId = addressId;
        this.orderDetail = orderDetail;
    }
}
