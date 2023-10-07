package com.wakedata.common.core.constants;


import com.wakedata.common.core.base.BaseEnum;

/**
 * 可用不可用标记枚举
 */
public enum ActiveStatusEnum implements BaseEnum {

    /**
     * 不可用|禁用
     */
    INVALID(0, "invalid"),

    /**
     * 可用|启用
     */
    ACTIVE(1, "active"),

    /**
     * 草稿状态
     */
    DRAFT(2, "草稿"),
    ;


    private Integer value;

    private String desc;

    ActiveStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
