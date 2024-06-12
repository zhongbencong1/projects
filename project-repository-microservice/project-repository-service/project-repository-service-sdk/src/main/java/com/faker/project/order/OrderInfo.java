package com.faker.project.order;

import com.faker.project.goods.DeductGoodsInventory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 订单信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {

    /** 用户地址id */
    private Long userAddress;

    private List<OrderItem> orderItemList;

    /** 订单商品信息*/
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItem {

        /** 商品主键id */
        private Long goodsId;

        /** 商品个数 */
        private Integer count;

        public DeductGoodsInventory toDeductGoodsInventory() {
            return new DeductGoodsInventory(this.goodsId, this.count);
        }
    }
}
