package com.wakedata.common.mq.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class DefaultRabbitMqConfig {

    /**
     * 队列名称
     */
    @Value("${common.mq.rabbitmq.queue-name:}")
    private String queueName;

    /**
     * 集群地址
     */
    @Value("${common.mq.rabbitmq.bootstrap-servers:}")
    private String bootstrapServers;

    /**
     * 账号
     */
    @Value("${common.mq.rabbitmq.username:}")
    private String userName;

    /**
     * 密码
     */
    @Value("${common.mq.rabbitmq.password:}")
    private String password;

    /**
     * 是否自动创建Topic
     */
    @Value("${common.mq.rabbitmq.auto-create-topic:false}")
    private Boolean autoCreateTopic;
}