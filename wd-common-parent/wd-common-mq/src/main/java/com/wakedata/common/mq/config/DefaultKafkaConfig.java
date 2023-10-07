package com.wakedata.common.mq.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * Kafka相关配置 - 公共配置/默认配置
 *
 * @author chenshaopeng
 * @date 2021/12/10
 */
@Data
public class DefaultKafkaConfig {

    /**
     * 组名或queueId
     */
    @Value("${common.mq.kafka.group-name:}")
    private String groupName;

    /**
     * 集群地址
     */
    @Value("${common.mq.kafka.bootstrap-servers:}")
    private String bootstrapServers;
}
