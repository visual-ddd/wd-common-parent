package com.wakedata.common.bizlog.config;

import lombok.Getter;

@Getter
public class BizLogConfig {

    /**
     * 发送审计日志的消息队列Topic
     */
    private final String mqTopic;

    /**
     * MQ私有配置名称，可以为空
     */
    private final String mqPrivateConfigName;

    public BizLogConfig(BizLogProperties auditLogProperties) {
        this.mqTopic = auditLogProperties.getMqTopic();
        this.mqPrivateConfigName = auditLogProperties.getMqPrivateConfigName();
    }

}
