package com.faker.project.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 类别(分级1) 品牌(分级2)
 * */
@Getter
@AllArgsConstructor
public enum BrandCategory {

    BRAND_A("20001", "品牌A"),
    BRAND_B("20002", "品牌B"),
    BRAND_C("20003", "品牌C"),
    BRAND_D("20004", "品牌D"),
    BRAND_E("20005", "品牌E"),
    ;

    /** 品牌分类编码 */
    private final String categoryCode;

    /** 品牌分类描述信息 */
    private final String description;

    /** 根据 code 获取到 BrandCategory */
    public static BrandCategory getBrand(String categoryCode) {
        Objects.requireNonNull(categoryCode);
        return Stream.of(values())
                .filter(bean -> bean.categoryCode.equals(categoryCode))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(categoryCode + " not exists"));
    }
}
