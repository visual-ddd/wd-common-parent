package com.wakedata.common.storage.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URLEncoder;

/**
 * @Desc 腾讯云 配置
 * @Author zkz
 * @Date 2021/12/8
 */
@Data
@ConfigurationProperties(prefix = "tencent.cos")
public class CosConfigProperties extends BaseConfigProperties {
    private String internalEndpoint;
    private String accessKeyId;
    private String accessKeySecret;

    @Override
    public String buildDisposition(String fileName) {
        return "attachment;filename=\"" + URLEncoder.encode(fileName) + "\"";
    }
}
