package com.wakedata.common.mq.core;

import com.wakedata.common.core.GlobalApplicationContext;
import com.wakedata.common.mq.annotation.MessageSubscribe;
import com.wakedata.common.mq.config.CommonConfig;
import com.wakedata.common.mq.config.PrivateMqConfig;
import com.wakedata.common.mq.enums.RequestProtocolEnum;
import com.wakedata.common.mq.model.MqConfigParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * MQ多级配置选择器
 *
 * @author chenshaopeng
 * @date 2021/12/10
 */
@Slf4j
@Lazy
@Import(SpringElExpressionResolve.class)
public class MqConfigurationSelector {

    @Resource
    private PrivateMqConfig privateMqConfig;

    @Resource
    private SpringElExpressionResolve expression;


    /**
     * 获取MQ配置参数
     * 存在相同配置时根据优先级定义进行配置覆盖
     * 优先级: 公共配置 < 私有配置 < 自定义配置
     */
    public MqConfigParam getMqConfigParam(@NonNull MessageSubscribe annotation){
        MqConfigParam connectParams = new MqConfigParam();
        connectParams.setProtocol(Optional.of(CommonConfig.protocol).orElse(annotation.protocol()));

        this.setDefaultCommonConfig(connectParams);

        this.setPrivateConfig(annotation.config(), connectParams);

        this.setAnnotationCustomConfig(annotation, connectParams);
        return connectParams;
    }

    /**
     * 注入公共配置
     * 优先级选择，“order”越小优先级越高
     *
     * @param param MQ配置参数
     */
    public void setDefaultCommonConfig(MqConfigParam param) {
        Iterator<Map.Entry<String, DefaultConfigurationAdapter>> iterator = GlobalApplicationContext
                .getApplicationContext().getBeansOfType(DefaultConfigurationAdapter.class).entrySet().iterator();
        DefaultConfigurationAdapter mqDefaultConfigurationAdapter = null;
        while (iterator.hasNext()) {
            if(mqDefaultConfigurationAdapter == null
                || iterator.next().getValue().getOrder() < mqDefaultConfigurationAdapter.getOrder()){
                mqDefaultConfigurationAdapter = iterator.next().getValue();
            }
        }
        Objects.requireNonNull(mqDefaultConfigurationAdapter);
        mqDefaultConfigurationAdapter.setDefaultCommonConfig(param);
    }


    /**
     * 注入私有配置
     * 优先以私有配置为准，为空时再考虑默认配置
     *
     * @param configName 配置名称
     * @param param MQ配置参数
     */
    public void setPrivateConfig(String configName, MqConfigParam param){
        if(StringUtils.isBlank(configName)
                || Objects.isNull(privateMqConfig) || Objects.isNull(privateMqConfig.getConfigs())){
            return;
        }
        for(PrivateMqConfig.Config privateConfig : privateMqConfig.getConfigs()){
            PrivateMqConfig.ConfigDetail configDetail = privateConfig.getItem();
            if(!configDetail.getName().equals(configName)){
                continue;
            }
            RequestProtocolEnum protocol = configDetail.getProtocol();
            try{
                Objects.requireNonNull(protocol);

                RequestProtocolEnum covert = protocol;
                if(covert != param.getProtocol()){
                    param.setProtocol(covert);
                    // 私有配置和公共配置的协议变化后重置默认配置
                    setDefaultCommonConfig(param);
                }
            }catch (RuntimeException ignored){
            }
            param.setQueueId(Optional.ofNullable(configDetail.getQueueId()).orElse(param.getQueueId()));
            param.setBootstrapServers(Optional.ofNullable(
                    configDetail.getBootstrapServers()).orElse(param.getBootstrapServers())
            );
            param.setTopic(Optional.ofNullable(configDetail.getTopic()).orElse(param.getTopic()));
            param.setTag(Optional.ofNullable(configDetail.getTag()).orElse(param.getTag()));
            if (RequestProtocolEnum.ROCKET_MQ == protocol) {
                param.setRocketMqAccessKey(Optional.ofNullable(configDetail.getRocketMqAccessKey()).orElse(param.getRocketMqAccessKey()));
                param.setRocketMqSecretKey(Optional.ofNullable(configDetail.getRocketMqSecretKey()).orElse(param.getRocketMqSecretKey()));
            }
            if (RequestProtocolEnum.KAFKA == protocol) {
                param.setSecurityProtocol(Optional.ofNullable(configDetail.getSecurityProtocol()).orElse(param.getSecurityProtocol()));
                param.setSaslMechanism(Optional.ofNullable(configDetail.getSaslMechanism()).orElse(param.getSaslMechanism()));
                param.setSaslJaasConfig(Optional.ofNullable(configDetail.getSaslJaasConfig()).orElse(param.getSaslJaasConfig()));
            }
            break;
        }
    }

    /**
     * 注入自定义配置
     * 优先以自定义配置为准，为空时再考虑其他配置
     *
     * @param annotation 协议
     * @param param MQ配置参数
     */
    private void setAnnotationCustomConfig(MessageSubscribe annotation, MqConfigParam param) {
        param.setProtocol(Optional.ofNullable(param.getProtocol()).orElse(annotation.protocol()));

        param.setQueueId(Optional.of(expression.resolveString(annotation.queueId()))
                .filter(StringUtils::isNotBlank).orElse(param.getQueueId()));

        param.setBootstrapServers(Optional.of(expression.resolveString(annotation.bootstrapServers()))
                .filter(StringUtils::isNotBlank).orElse(param.getBootstrapServers()));

        param.setTopic(Optional.of(expression.resolveString(annotation.topic()))
                .filter(StringUtils::isNotBlank).orElse(param.getTopic()));

        param.setTag(Optional.of(expression.resolveString(annotation.tag()))
                .filter(StringUtils::isNotBlank).orElse(param.getTag()));

    }

}
