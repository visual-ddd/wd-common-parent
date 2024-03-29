package com.wakedata.common.storage.config;

import lombok.Data;

/**
 * @Desc 桶配置
 * @Author zkz
 * @Date 2021/12/8
 */
@Data
public class BucketConfig {
    /**
     * 权限
     */
    private String readPermission;
    /**
     * 自定义桶的key
     */
    private String key;
    /**
     * 桶名称，对应具体服务商
     */
    private String name;
    /**
     * cdn 链接
     */
    private String cdnUrl;

    /**
     * 是否是默认桶
     */
    private Boolean isDefault;
}