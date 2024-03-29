package com.wakedata.common.storage.enums;

import com.wakedata.common.core.base.BaseEnum;

/**
 * 桶枚举
 * @author hhf
 */
public enum BucketEnum implements BaseEnum {
    /**
     * 证书
     */
    CERT("cert", "证书"),
    /**
     * 素材中心
     */
    MATERIAL("material", "素材中心"),
    /**
     * 人脸图片
     */
    FACE("face", "人脸图片");

    private String bucketKey;
    private String value;

    BucketEnum(String bucketKey, String value) {
        this.bucketKey = bucketKey;
        this.value = value;
    }

    @Override
    public String getValue() {
        return bucketKey;
    }

    @Override
    public String getDesc() {
        return value;
    }
}
