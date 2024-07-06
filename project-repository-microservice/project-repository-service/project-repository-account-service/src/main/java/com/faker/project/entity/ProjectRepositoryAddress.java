package com.faker.project.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.faker.project.account.AddressInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * 用户地址表实体类定义
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("project_repository_address")
public class ProjectRepositoryAddress {

    /** 自增主键 */
    @TableId("id")
    private Long id;

    /** 用户 id */
    @TableField("user_id")
    private Long userId;

    /** 用户名 */
    @TableField("username")
    private String username;

    /** 电话 */
    @TableField("phone")
    private String phone;

    /** 省 */
    @TableField("province")
    private String province;

    /** 市 */
    @TableField("city")
    private String city;

    /** 详细地址 */
    @TableField("address_detail")
    private String addressDetail;

    /** 创建时间 */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /** 更新时间 */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private Date updateTime;

    /** 根据 userId + AddressItem 得到 ProjectRepositoryAddress */
    public static ProjectRepositoryAddress to(Long userId, AddressInfo.AddressItem addressItem) {

        ProjectRepositoryAddress ecommerceAddress = new ProjectRepositoryAddress();

        ecommerceAddress.setUserId(userId);
        ecommerceAddress.setUsername(addressItem.getUsername());
        ecommerceAddress.setPhone(addressItem.getPhone());
        ecommerceAddress.setProvince(addressItem.getProvince());
        ecommerceAddress.setCity(addressItem.getCity());
        ecommerceAddress.setAddressDetail(addressItem.getAddressDetail());

        return ecommerceAddress;
    }

    /** 将 ProjectRepositoryAddress 对象转成 AddressInfo */
    public AddressInfo.AddressItem toAddressItem() {

        AddressInfo.AddressItem addressItem = new AddressInfo.AddressItem();

        addressItem.setId(this.id);
        addressItem.setUsername(this.username);
        addressItem.setPhone(this.phone);
        addressItem.setProvince(this.province);
        addressItem.setCity(this.city);
        addressItem.setAddressDetail(this.addressDetail);
        addressItem.setCreateTime(this.createTime);
        addressItem.setUpdateTime(this.updateTime);

        return addressItem;
    }
}
