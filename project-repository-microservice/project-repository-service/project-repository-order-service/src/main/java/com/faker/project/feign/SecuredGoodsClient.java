package com.faker.project.feign;

import com.faker.project.common.TableId;
import com.faker.project.feign.hystrix.GoodsClientHystrix;
import com.faker.project.goods.SimpleGoodsInfo;
import com.faker.project.vo.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 安全的商品服务 Feign 接口
 * */
@FeignClient(
        contextId = "SecuredGoodsClient",
        value = "project-repository-goods-service",
        fallback = GoodsClientHystrix.class //熔断时
)
public interface SecuredGoodsClient {

    /** 根据 ids 查询简单的商品信息 */
    @RequestMapping(value = "/ecommerce-goods-service/goods/simple-goods-info", method = RequestMethod.POST)
    CommonResponse<List<SimpleGoodsInfo>> getSimpleGoodsInfoByTableId(@RequestBody TableId tableId);
}
