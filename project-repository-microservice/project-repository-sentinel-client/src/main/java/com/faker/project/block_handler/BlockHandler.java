package com.faker.project.block_handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.faker.project.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义通用的限流处理逻辑
 */
@Slf4j
public class BlockHandler {

    /** 通用限流处理方法 这个方法必须是 static 的 */
    public static CommonResponse<String> HandleBlockException(BlockException exception) {
        log.error("BlockHandler HandleBlockException exception rule:{}, ruleLimitApp:{}",
                JSON.toJSONString(exception.getRule()), exception.getRuleLimitApp());
        return new CommonResponse<>(-1, "flow rule trigger block HandleBlockException", null);
    }
}