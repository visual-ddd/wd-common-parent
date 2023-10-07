package com.wakedata.common.mq.common;

import com.wakedata.common.mq.model.MqConfigParam;
import com.wakedata.common.mq.model.consumer.MakeSubscribeBody;
import com.wakedata.common.mq.model.producer.MakeMessageBody;
import org.springframework.lang.NonNull;

/**
 * MQ配置转换
 *
 * @author chenshaopeng
 * @date 2021/12/11
 */
public class MqConfigParamConvert {

    /**
     * 转换为消费者订阅Body
     */
    public static MakeSubscribeBody convertToMakeSubscribeBody(@NonNull MqConfigParam configParam){
        return MakeSubscribeBody.builder()
                .protocol(configParam.getProtocol())
                .bootstrapServers(configParam.getBootstrapServers())
                .queueId(configParam.getQueueId())
                .topic(configParam.getTopic())
                .tag(configParam.getTag())
                .batchPullNumber(configParam.getBatchPullNumber())
                .pollingWaitSeconds(configParam.getPollingWaitSeconds())
                .requestTimeout(configParam.getRequestTimeout())
                .secretId(configParam.getSecretId())
                .secretKey(configParam.getSecretKey())
                .rocketMqAccessKey(configParam.getRocketMqAccessKey())
                .rocketMqSecretKey(configParam.getRocketMqSecretKey())
                .securityProtocol(configParam.getSecurityProtocol())
                .saslMechanism(configParam.getSaslMechanism())
                .saslJaasConfig(configParam.getSaslJaasConfig())
                .autoCreateTopic(configParam.getAutoCreateTopic())
                .build();
    }

    /**
     * 转换为发送消息Body
     */
    public static MakeMessageBody convertToMakeMessageBody(@NonNull MqConfigParam configParam){
        return MakeMessageBody.builder()
                .protocol(configParam.getProtocol())
                .bootstrapServers(configParam.getBootstrapServers())
                .queueId(configParam.getQueueId())
                .topic(configParam.getTopic())
                .tag(configParam.getTag())
                .secretId(configParam.getSecretId())
                .secretKey(configParam.getSecretKey())
                .rocketMqAccessKey(configParam.getRocketMqAccessKey())
                .rocketMqSecretKey(configParam.getRocketMqSecretKey())
                .securityProtocol(configParam.getSecurityProtocol())
                .saslMechanism(configParam.getSaslMechanism())
                .saslJaasConfig(configParam.getSaslJaasConfig())
                .autoCreateTopic(configParam.getAutoCreateTopic())
                .build();
    }

}
