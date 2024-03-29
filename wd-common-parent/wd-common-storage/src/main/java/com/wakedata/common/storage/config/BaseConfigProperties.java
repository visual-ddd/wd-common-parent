package com.wakedata.common.storage.config;

import lombok.Data;

import java.util.List;

/**
 * @Desc
 * @Author zkz
 * @Date 2021/12/8
 */
@Data
public class BaseConfigProperties {

    private String endpoint;
    /**
     * 配置的桶集合
     */
    List<BucketConfig> bucketConfigs;

    /**
     * 解决 s3,cos 下载附件格式文件名中文乱码问题
     * @param fileName
     * @return
     */
    public String buildDisposition(String fileName) {
        return "attachment;filename=\"" + fileName + "\"";
    }
}
