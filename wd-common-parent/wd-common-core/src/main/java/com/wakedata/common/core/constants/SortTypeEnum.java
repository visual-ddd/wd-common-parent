package com.wakedata.common.core.constants;

import com.wakedata.common.core.base.BaseEnum;

/**
 * 全局排序枚举类
 */
public enum SortTypeEnum implements BaseEnum {

    /**
     * 正序 ASC 1
     */
    ASC(1, "ASC"),

    /**
     * 倒序 DESC 2
     */
    DESC(2, "DESC");

    private Integer value;

    private String desc;

    SortTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * 获取枚举类的值
     *
     * @return Integer
     */
    @Override
    public Integer getValue() {
        return value;
    }

    /**
     * 获取枚举类的说明
     *
     * @return String
     */
    @Override
    public String getDesc() {
        return desc;
    }
}
