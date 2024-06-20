package com.faker.project.schedule;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

import java.util.Optional;

/**
 * json属性访问器
 */
@Slf4j
@SuppressWarnings("all")
public class JsonPropertyAccessor implements PropertyAccessor {
    @Override
    public Class<?>[] getSpecificTargetClasses() {
        return new Class[]{JSONObject.class, JSONArray.class, String.class, JSON.class};
    }

    /**
     * 设置是否可读: 如果target是json格式则返回可读, 如果不是json, 则不可读
     */
    @Override
    public boolean canRead(EvaluationContext evaluationContext, Object target, String name) throws AccessException {
        if (target instanceof JSON) {
            return true;
        } else if (target instanceof String) {
            try {
                canWrite(evaluationContext, JSONUtil.parse(target), name);
            } catch (Exception e) {
                log.info("JsonPropertyAccessor canRead target is not json format, target: {}", target);
                return false;
            }
        }
        return true;
    }

    /**
     * target: 待解析的对象, name: 属性名
     * byPath: 解析的结果
     */
    @Override
    public TypedValue read(EvaluationContext evaluationContext, Object target, String name) throws AccessException {
        Object byPath = JSONUtil.getByPath(JSONUtil.parse(target), name);
        return new TypedValue(Optional.ofNullable(byPath).orElse(""));
    }

    // 设置是否可写
    @Override
    public boolean canWrite(EvaluationContext evaluationContext, Object o, String s) throws AccessException {
        return true;
    }

    /**
     *
     * @param evaluationContext 上下文
     * @param target 目标对象
     * @param name 目标名
     * @param newValue 设置的新的值
     */
    @Override
    public void write(EvaluationContext evaluationContext, Object target, String name, Object newValue) throws AccessException {
        JSON targetObj = JSONUtil.parse(target);
        JSONUtil.putByPath(targetObj, name, newValue);
    }
}
