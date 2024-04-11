package com.faker.project.service;


import com.faker.project.goods.GoodsInfo;

import java.util.List;

/**
 * 异步服务接口定义
 * */
public interface IAsyncService {

    /** 异步将商品信息保存下来 */
    void asyncImportGoods(List<GoodsInfo> goodsInfos, String taskId);
}
