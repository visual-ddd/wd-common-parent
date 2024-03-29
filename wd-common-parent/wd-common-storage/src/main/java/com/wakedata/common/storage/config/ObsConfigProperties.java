package com.wakedata.common.storage.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Desc 华为云 配置
 * @Author zkz
 * @Date 2021/12/8
 */
@Data
@ConfigurationProperties(prefix = "huawei.obs")
public class ObsConfigProperties extends BaseConfigProperties {
    private String internalEndpoint;
    private String accessKey;
    private String secretKey;

}
