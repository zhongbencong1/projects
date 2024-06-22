package com.faker.project.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * 物流信息表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("repository_logistics")
public class LogisticsInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableId(type = IdType.AUTO, value = "user_id")
    private Long userId;

    @TableId(type = IdType.AUTO, value = "order_id")
    private Long orderId;

    @TableId(type = IdType.AUTO, value = "address_id")
    private Long addressId;

    @TableField("extra_info")
    private String extraInfo;

    // 与MyMetaObjectHandler类 实现自动填充 创建时间create_time 和 修改时间update_time
    @TableField(value = "create_time", fill = FieldFill.INSERT_UPDATE)
    private Long createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT)
    private Long updateTime;

    public LogisticsInfo(Long userId, Long orderId, Long addressId, String extraInfo) {
        this.userId = userId;
        this.orderId = orderId;
        this.addressId = addressId;
        this.extraInfo = StringUtils.isNotEmpty(extraInfo) ? extraInfo : "{}";
    }
}
