package com.wakedata.common.mq.common;

import com.wakedata.common.core.GlobalApplicationContext;
import com.wakedata.common.mq.config.CommonConfig;
import com.wakedata.common.mq.core.MqConfigurationSelector;
import com.wakedata.common.mq.enums.RequestProtocolEnum;
import com.wakedata.common.mq.model.MqConfigParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Optional;

/**
 * MQ配置构造器
 *
 * @author chenshaopeng
 * @date 2021/12/10
 */
@Slf4j
public class MqConfigurationBuilder {


    public static MqConfigParam build(RequestProtocolEnum protocol, String privateConfigName
            , MqConfigParam customParams) {
        protocol = Objects.isNull(protocol) && Objects.nonNull(customParams)
                ? customParams.getProtocol() : protocol;

        MqConfigParam param = build(protocol, privateConfigName);
        if(Objects.isNull(customParams)){
            return param;
        }
        param.setProtocol(Optional.ofNullable(param.getProtocol()).orElse(customParams.getProtocol()));
        param.setQueueId(Optional.ofNullable(customParams.getQueueId())
                .filter(StringUtils::isNotBlank).orElse(param.getQueueId()));
        param.setBootstrapServers(Optional.ofNullable(customParams.getBootstrapServers())
                .filter(StringUtils::isNotBlank).orElse(param.getBootstrapServers()));
        param.setTopic(Optional.ofNullable(customParams.getTopic())
                .filter(StringUtils::isNotBlank).orElse(param.getTopic()));
        param.setTag(Optional.ofNullable(customParams.getTag())
                .filter(StringUtils::isNotBlank).orElse(param.getTag()));
        param.setBatchPullNumber(Optional.ofNullable(customParams.getBatchPullNumber())
                .orElse(param.getBatchPullNumber()));
        param.setPollingWaitSeconds(Optional.ofNullable(customParams.getPollingWaitSeconds())
                .orElse(param.getPollingWaitSeconds()));
        param.setRequestTimeout(Optional.ofNullable(customParams.getRequestTimeout())
                .orElse(param.getRequestTimeout()));
        param.setSecretId(Optional.ofNullable(customParams.getSecretId())
                .filter(StringUtils::isNotBlank).orElse(param.getSecretId()));
        param.setSecretKey(Optional.ofNullable(customParams.getSecretKey())
                .filter(StringUtils::isNotBlank).orElse(param.getSecretKey()));
        param.setRocketMqAccessKey(Optional.ofNullable(customParams.getRocketMqAccessKey())
                .filter(StringUtils::isNotBlank).orElse(param.getRocketMqAccessKey()));
        param.setRocketMqSecretKey(Optional.ofNullable(customParams.getRocketMqSecretKey())
                .filter(StringUtils::isNotBlank).orElse(param.getRocketMqSecretKey()));
        param.setSecurityProtocol(Optional.ofNullable(customParams.getSecurityProtocol())
                .filter(StringUtils::isNotBlank).orElse(param.getSecurityProtocol()));
        param.setSaslMechanism(Optional.ofNullable(customParams.getSaslMechanism())
                .filter(StringUtils::isNotBlank).orElse(param.getSaslMechanism()));
        param.setSaslJaasConfig(Optional.ofNullable(customParams.getSaslJaasConfig())
                .filter(StringUtils::isNotBlank).orElse(param.getSaslJaasConfig()));
        param.setAutoCreateTopic(Optional.ofNullable(customParams.getAutoCreateTopic())
                .orElse(param.getAutoCreateTopic()));
        return param;
    }

    public static MqConfigParam build(RequestProtocolEnum protocol, MqConfigParam customParams) {
        return build(protocol, null, customParams);
    }

    private static MqConfigParam build(RequestProtocolEnum protocol, String privateConfigName){
        MqConfigParam connectParams = build(protocol);
        getSelectorInstance().setPrivateConfig(privateConfigName, connectParams);
        return connectParams;
    }

    private static MqConfigParam build(RequestProtocolEnum protocol){
        MqConfigParam connectParams = new MqConfigParam();
        connectParams.setProtocol(Optional.ofNullable(protocol).orElse(CommonConfig.protocol));
        getSelectorInstance().setDefaultCommonConfig(connectParams);
        return connectParams;
    }

    private static MqConfigurationSelector getSelectorInstance(){
        return GlobalApplicationContext.getBean(MqConfigurationSelector.class);
    }

}
