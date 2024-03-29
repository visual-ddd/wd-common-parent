package com.wakedata.common.storage.config;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 阿里云 配置
 *
 * @author zkz
 * @date 2021/12/8
 */
@EqualsAndHashCode(callSuper = false)
@Data
@ConfigurationProperties(prefix = "aliyun.oss")
public class OssConfigProperties extends BaseConfigProperties {
    private String internalEndpoint;
    private String secretId;
    private String secretKey;
    private String regionId;
    /**
     * 前端直接上传文件到阿里云时,需要通过后端接口取授权token
     * 后端通过STS SDK获取授权token,
     * 其中RoleArn参数需要根据https://help.aliyun.com/document_detail/100624.html 配置好后获取
     */
    private String roleArn;
}
