package com.wakedata.common.redis.jedis.redis;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 对应配置文件common-http.yaml
 *
 * @author pengxu
 * @date 2020-10-16
 */
@Component
@RefreshScope
@Data
@Deprecated
public class CommonHttpConfig {

    public static CommonHttpConfig getInstance(){
        return BaseApplicationContext.getBeanByClass(CommonHttpConfig.class);
    }

    @Value("${http.client.socket.timeout}")
    private Integer httpClientSocketTimeout;

    @Value("${http.client.connection.timeout}")
    private Integer httpClientConnectionTimeout;

    @Value("${http.client.proxy.host:}")
    private String httpClientProxyHost;

    @Value("${http.client.proxy.port:}")
    private Integer httpClientProxyPort;

}
