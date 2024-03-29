package com.wakedata.common.storage.enums;

/**
 * 云存储读写权限，private/public/cdn
 * 默认private私有，可选public公共读，cdn加速读
 * @Author zkz
 * @Date 2021/12/8
 */
public enum ReadPermissionEnum {

    /**
     * 云存储读写权限
     */
    PRIVATE("private","私有读"),
    PUBLIC("public","公共读");

    private String name;
    private String desc;

    ReadPermissionEnum(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
