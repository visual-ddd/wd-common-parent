package com.wakedata.common.domainevent.retry.module;

import com.wakedata.common.core.base.BaseEnum;

/**
 * 重试类型枚举
 *
 * @author hhf
 * @date 2022/6/14
 */
public enum RetryTypeEnum implements BaseEnum {

    SECHEDULE(0, "定时调度")

    , JVM_RESTART(1, "重启时重试");


    private final Integer value;

    private final String desc;

    RetryTypeEnum(Integer value, String desc) {
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
