package com.wakedata.common.spring.nacos;

import com.alibaba.cloud.nacos.ConditionalOnNacosDiscoveryEnabled;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 解决openfeign不支持下划线的问题，新增配置转换
 * @author wangcan
 * @date 2021/12/31 15:47
 */
@Configuration
@ConditionalOnNacosDiscoveryEnabled
public class NacosDiscoveryConfig {

    @Value("${spring.application.name:}")
    private String service;

    @Bean
    @Primary
    @ConfigurationProperties(prefix ="spring.cloud.nacos.discovery")
    public NacosDiscoveryProperties properties() {
        return new NacosDiscoveryProperties();
    }

    @Bean
    @Primary
    @ConditionalOnBean(NacosDiscoveryProperties.class)
    public NacosServiceRegistry nacosServiceRegistry(
            NacosDiscoveryProperties nacosDiscoveryProperties) {
        nacosDiscoveryProperties.setService(service.replace("_", "-"));
        return new NacosServiceRegistry(nacosDiscoveryProperties);
    }
}
