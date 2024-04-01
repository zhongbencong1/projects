package com.imooc.ecommerce.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通用响应对象定义
 * {
 *     "code": 0,
 *     "message": "",
 *     "data": {}
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> implements Serializable {

    private Integer code;

    private String message;

    private T Data;

    public CommonResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static <T> CommonResponse<T> success(T t) {
        CommonResponse<T> result = new CommonResponse<>(0, "success");
        result.setData(t);
        return result;
    }

    public static <T> CommonResponse<T> fail(String message, T t) {
        CommonResponse<T> result = new CommonResponse<>(-1, message);
        result.setData(t);
        return result;
    }
}
