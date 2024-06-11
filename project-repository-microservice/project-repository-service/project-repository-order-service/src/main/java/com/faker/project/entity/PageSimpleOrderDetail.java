package com.faker.project.entity;

import com.faker.project.account.UserAddress;
import com.faker.project.goods.SimpleGoodsInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <h1>订单详情</h1>
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageSimpleOrderDetail {

    /** 订单详情 */
    private List<SingleOrderItem> orderItems;

    /** 是否有更多的订单(分页) */
    private Boolean hasMore;

    /** 单个订单信息 */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SingleOrderItem {

        /** 订单表主键 id */
        private Long id;

        /** 用户地址信息 */
        private UserAddress userAddress;

        /** 订单商品信息 */
        private List<SingleOrderGoodsItem> goodsItems;
    }

    /** 单个订单中的单项商品信息 */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SingleOrderGoodsItem {

        /** 简单商品信息 */
        private SimpleGoodsInfo simpleGoodsInfo;

        /** 商品个数 */
        private Integer count;
    }
}
