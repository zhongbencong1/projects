package com.faker.project.feign.hystrix;

import com.alibaba.fastjson.JSON;
import com.faker.project.common.TableId;
import com.faker.project.feign.SecuredGoodsClient;
import com.faker.project.goods.SimpleGoodsInfo;
import com.faker.project.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 商品服务熔断降级兜底
 * */
@Slf4j
@Component
public class GoodsClientHystrix implements SecuredGoodsClient {

    @Override
    public CommonResponse<List<SimpleGoodsInfo>> getSimpleGoodsInfoByTableId(TableId tableId) {
        log.error("goods client feign request error in order service getSimpleGoodsInfoByTableId error: [{}]", JSON.toJSONString(tableId));
        return new CommonResponse<>(-1, "goods client feign request error in order service", Collections.emptyList());
    }
}
