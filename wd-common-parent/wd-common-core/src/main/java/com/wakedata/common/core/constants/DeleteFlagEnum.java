package com.wakedata.common.core.constants;

import com.wakedata.common.core.base.BaseEnum;

/**
 * 删除标识枚举
 *
 * @date 2021/12/24
 */
public enum DeleteFlagEnum implements BaseEnum {

    /**
     * 删除标识
     */
    ACTIVE(0, "未删除")

    , DELETED(1, "已删除");


    private final Integer value;

    private final String desc;

    DeleteFlagEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }
}
