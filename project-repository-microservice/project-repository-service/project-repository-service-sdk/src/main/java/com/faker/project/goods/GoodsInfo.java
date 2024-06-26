package com.faker.project.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 商品信息
 * */
@ApiModel(description = "详细的商品信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsInfo {

    @ApiModelProperty(value = "商品表主键 id")
    private Long id;

    @ApiModelProperty(value = "商品类别编码")
    private String goodsCategory;

    @ApiModelProperty(value = "品牌分类编码")
    private String brandCategory;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品图片")
    private String goodsPic;

    @ApiModelProperty(value = "商品描述信息")
    private String goodsDescription;

    @ApiModelProperty(value = "商品状态")
    private String goodsStatus;

    @ApiModelProperty(value = "商品价格, 单位: 分")
    private Integer price;

    @ApiModelProperty(value = "商品属性")
    private GoodsProperty goodsProperty;

    @ApiModelProperty(value = "供应量")
    private Long supply;

    @ApiModelProperty(value = "库存")
    private Long inventory;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /** 商品属性 */
    @ApiModel(description = "商品属性对象")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoodsProperty {

        @ApiModelProperty(value = "尺寸")
        private String size;

        @ApiModelProperty(value = "颜色")
        private String color;

        @ApiModelProperty(value = "材质")
        private String material;

        @ApiModelProperty(value = "图案")
        private String pattern;
    }
}
