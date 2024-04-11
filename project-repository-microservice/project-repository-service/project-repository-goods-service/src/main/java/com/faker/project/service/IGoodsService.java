package com.faker.project.service;


import com.faker.project.common.TableId;
import com.faker.project.goods.DeductGoodsInventory;
import com.faker.project.goods.GoodsInfo;
import com.faker.project.goods.SimpleGoodsInfo;
import com.faker.project.vo.PageSimpleGoodsInfo;

import java.util.List;

/**
 * 商品微服务相关服务接口定义
 * */
public interface IGoodsService {

    /** 根据 TableId 查询商品详细信息 */
    List<GoodsInfo> getGoodsInfoByTableId(TableId tableId);

    /** 获取分页的商品信息 */
    PageSimpleGoodsInfo getSimpleGoodsInfoByPage(int page);

    /** 根据 TableId 查询简单商品信息 */
    List<SimpleGoodsInfo> getSimpleGoodsInfoByTableId(TableId tableId);

    /** 扣减商品库存 */
    Boolean deductGoodsInventory(List<DeductGoodsInventory> deductGoodsInventories);
}
