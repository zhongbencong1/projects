package com.faker.project.feign;

import com.faker.project.common.TableId;
import com.faker.project.goods.DeductGoodsInventory;
import com.faker.project.goods.SimpleGoodsInfo;
import com.faker.project.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 用户商品微服务Feign调用接口: contextId:client唯一id, value:调用的微服务的name.
 * NotSecuredBalanceClient: 不安全的调用, 调用失败需要抛出异常而非吞掉异常: 用于事务
 * */
@FeignClient(contextId = "NotSecuredGoodsClient", value = "project-repository-goods-service")
public interface NotSecuredGoodsClient {

    /** 根据 ids 扣减商品 */
    @RequestMapping(value = "/project-repository-goods-service/goods/deduct-goods-inventory", method = RequestMethod.PUT)
    CommonResponse<Boolean> deductGoodsInventory(@RequestBody List<DeductGoodsInventory> deductGoodsInventories);

    /** 根据 ids 查询简单的商品信息 */
    @RequestMapping(value = "/project-repository-goods-service/goods/simple-goods-info", method = RequestMethod.POST)
    CommonResponse<List<SimpleGoodsInfo>> getSimpleGoodsInfoByTableId(@RequestBody TableId tableId);
}
