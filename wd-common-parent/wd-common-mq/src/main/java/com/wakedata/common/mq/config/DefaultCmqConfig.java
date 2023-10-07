package com.wakedata.common.mq.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * cmq相关的配置 - 公共配置/默认配置
 *
 * @author chenshaopeng
 * @date 2021/12/11
 */
@Data
public class DefaultCmqConfig {

    /**
     * cmq生产者和消费者
     */
    @Value("${common.mq.cmq.consumer.queue:}")
    private String consumerQueue;

    @Value("${common.mq.cmq.consumer.request-timeout:}")
    private String consumerRequestTimeout;

    @Value("${common.mq.cmq.consumer.pull-wait-second:}")
    private String consumerPullWaitSecond;

    @Value("${common.mq.cmq.consumer.batch-pull-num:}")
    private String consumerBatchPullNum;

    @Value("${common.mq.cmq.producer.request-timeout:}")
    private String producerRequestTimeout;

    @Value("${common.mq.cmq.producer.retry-times:}")
    private String producerRetryTimes;

    @Value("${common.mq.cmq.public.nameserver-url:}")
    private String publicNameserverUrl;

    @Value("${common.mq.cmq.vpc.nameserver-url:}")
    private String vpcNameserverUrl;

    /**
     * 开发者id
     */
    @Value("${tencent.secretId:}")
    private String secretId;

    /**
     * 开发者密钥
     */
    @Value("${tencent.secretKey:}")
    private String secretKey;

    /**
     * token
     */
    @Value("${tencent.cloud.market.token:}")
    private String tencentCloudMarketToken;
}
