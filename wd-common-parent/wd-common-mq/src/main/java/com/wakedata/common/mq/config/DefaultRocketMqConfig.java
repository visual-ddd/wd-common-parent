package com.wakedata.common.mq.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * rocketMQ相关配置 - 公共配置/默认配置
 *
 * @author chenshaopeng
 * @date 2021/12/11
 */
@Data
public class DefaultRocketMqConfig {

    /**
     * 组名或queueId
     */
    @Value("${common.mq.rocketmq.group-name:}")
    private String groupName;

    /**
     * 实例名称，可以与组名称保持一致
     */
    @Value("${common.mq.rocketmq.instance-name:}")
    private String instanceName;

    /**
     * 集群地址
     */
    @Value("${common.mq.rocketmq.bootstrap-servers:}")
    private String bootstrapServers;

    @Value("${common.mq.rocketmq.access-key:}")
    private String accessKey;

    @Value("${common.mq.rocketmq.secret-key:}")
    private String secretKey;
}
