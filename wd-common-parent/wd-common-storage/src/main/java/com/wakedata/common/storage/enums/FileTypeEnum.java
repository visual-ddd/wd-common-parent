package com.wakedata.common.storage.enums;

import com.wakedata.common.core.base.BaseEnum;

/**
 * 文件类型枚举
 * @author zkz
 */
public enum FileTypeEnum implements BaseEnum {
    /**
     * 图片
     */
    PICTURE(0, "图片"),
    /**
     * 视频
     */
    VIDEO(1, "视频");

    private Integer value;
    private String desc;

    FileTypeEnum(Integer value, String desc) {
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
