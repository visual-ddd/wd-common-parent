package com.wakedata.common.chatgpt.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置
 *
 */
@Data
@ConfigurationProperties(prefix = "common.chatgpt")
public class ChatGptProperties {


    /**
     * 代理服务器
     */
    @Value("${common.chatgpt.api.proxy.host:http://www.focusyi.top/}")
    private String proxyApiHost = "http://www.focusyi.top/";

    /**
     * apiKey
     */
    @Value("${common.chatgpt.api.key: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}")
    private String apiKey= "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";

}
