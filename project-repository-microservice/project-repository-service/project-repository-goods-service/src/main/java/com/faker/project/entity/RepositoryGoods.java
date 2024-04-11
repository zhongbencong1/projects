package com.faker.project.entity;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.faker.project.constant.GoodsStatus;
import com.faker.project.goods.GoodsInfo;
import com.faker.project.goods.SimpleGoodsInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

/**
 * 商品表实体类定义
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("repository_goods")
public class RepositoryGoods {

    /** 自增主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 商品类型 */
    @TableField("goods_category")
    private String goodsCategory;

    /** 品牌分类 */
    @TableField("brand_category")
    private String brandCategory;

    /** 商品名称 */
    @TableField("goods_name")
    private String goodsName;

    /** 商品名称 */
    @TableField("goods_pic")
    private String goodsPic;

    /** 商品描述信息 */
    @TableField("goods_description")
    private String goodsDescription;

    /** 商品状态 */
    @TableField("goods_status")
    private String goodsStatus;

    /** 商品价格: 单位: 分、厘 */
    @TableField("price")
    private Integer price;

    /** 总供应量 */
    @TableField("supply")
    private Long supply;

    /** 库存 */
    @TableField("inventory")
    private Long inventory;

    /** 商品属性, json 字符串存储 */
    @TableField("goods_property")
    private String goodsProperty;

    /** 创建时间 */
    @TableField("create_time")
    private Date createTime;

    /** 更新时间 */
    @LastModifiedDate
    @TableField("update_time")
    private Date updateTime;

    /** 将 GoodsInfo 转成实体对象 (todo 使用mapstruct进行对象转换) */
    public static RepositoryGoods infoToGoods(GoodsInfo goodsInfo) {
        RepositoryGoods repositoryGoods = new RepositoryGoods();
        repositoryGoods.setGoodsCategory(goodsInfo.getGoodsCategory());
        repositoryGoods.setBrandCategory(goodsInfo.getBrandCategory());
        repositoryGoods.setGoodsName(goodsInfo.getGoodsName());
        repositoryGoods.setGoodsPic(goodsInfo.getGoodsPic());
        repositoryGoods.setGoodsDescription(goodsInfo.getGoodsDescription());
        repositoryGoods.setGoodsStatus(GoodsStatus.ONLINE.getStatus());  // 可以增加一个审核的过程 审核之后可以上线
        repositoryGoods.setPrice(goodsInfo.getPrice());
        repositoryGoods.setSupply(goodsInfo.getSupply());
        repositoryGoods.setInventory(goodsInfo.getSupply());
        repositoryGoods.setGoodsProperty(
                JSON.toJSONString(goodsInfo.getGoodsProperty())
        );

        return repositoryGoods;
    }

    /** 将实体对象转成 GoodsInfo 对象 (todo 使用mapstruct进行对象转换)  */
    public GoodsInfo goodsToInfo() {
        GoodsInfo goodsInfo = new GoodsInfo();
        goodsInfo.setId(this.id);
        goodsInfo.setGoodsCategory(this.goodsCategory);
        goodsInfo.setBrandCategory(this.brandCategory);
        goodsInfo.setGoodsName(this.goodsName);
        goodsInfo.setGoodsPic(this.goodsPic);
        goodsInfo.setGoodsDescription(this.goodsDescription);
        goodsInfo.setGoodsStatus(this.getGoodsStatus());
        goodsInfo.setPrice(this.price);
        goodsInfo.setGoodsProperty(JSON.parseObject(this.goodsProperty, GoodsInfo.GoodsProperty.class));
        goodsInfo.setSupply(this.supply);
        goodsInfo.setInventory(this.inventory);
        goodsInfo.setCreateTime(this.createTime);
        goodsInfo.setUpdateTime(this.updateTime);
        return goodsInfo;
    }

    /** 将实体对象转成 SimpleGoodsInfo 对象 */
    public SimpleGoodsInfo goodsToSimple() {
        SimpleGoodsInfo goodsInfo = new SimpleGoodsInfo();
        goodsInfo.setId(this.id);
        goodsInfo.setGoodsName(this.goodsName);
        goodsInfo.setGoodsPic(this.goodsPic);
        goodsInfo.setPrice(this.price);
        return goodsInfo;
    }
}
