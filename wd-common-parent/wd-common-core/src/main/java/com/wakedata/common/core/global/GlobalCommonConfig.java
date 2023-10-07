package com.wakedata.common.core.global;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 全局公共配置
 * @date 2022/1/13 15:15
 */
@Data
@Component
public class GlobalCommonConfig {

    /**
     * wkcloud 权限平台域id
     */
    @Value("${permission.platform.id:4}")
    private String permissionPlatformId;

    @Value("${phone.regex:^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[013,5-8]|18[0-9]|19[189])[0-9]{8}$}")
    private String phoneRegex;

    @Value("${email.regex:^\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}$}")
    private String emailRegex;

    @Value("${audit.log.type:es}")
    private String auditLogType;

}
