package com.wakedata.common.bizlog.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 业务日志配置
 *
 * @author wujunqiang
 * @date 2021/12/31 11:48
 */
@Data
@ConfigurationProperties(prefix = "common.bizlog")
public class BizLogProperties {


    /**
     * 发送业务日志的消息队列Topic
     */
    @Value("${common.bizlog.mq-topic:bizpf_dev_wd_common_bizlog_topic}")
    private String mqTopic;

    /**
     * MQ私有配置名称，可以为空
     */
    @Value("${common.bizlog.mq-private-config:}")
    private String mqPrivateConfigName;

}
