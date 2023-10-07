package com.wakedata.common.mq.core;

import com.wakedata.common.mq.config.DefaultCmqConfig;
import com.wakedata.common.mq.config.DefaultKafkaConfig;
import com.wakedata.common.mq.config.DefaultRabbitMqConfig;
import com.wakedata.common.mq.config.DefaultRocketMqConfig;
import com.wakedata.common.mq.model.MqConfigParam;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

/**
 * 公共配置/默认配置适配器接口实现
 *
 * @author chenshaopeng
 * @date 2021/12/11
 */
@Slf4j
public class DefaultConfigurationAdapterImpl implements DefaultConfigurationAdapter {

    @Resource
    private DefaultRocketMqConfig defaultRocketMqConfig;

    @Resource
    private DefaultCmqConfig defaultCmqConfig;

    @Resource
    private DefaultKafkaConfig defaultKafkaConfig;

    @Resource
    private DefaultRabbitMqConfig defaultRabbitMqConfig;


    /**
     * 设置默认公共配置，兼容惟客云旧配置
     */
    @Override
    public void setDefaultCommonConfig(MqConfigParam target){
        switch (target.getProtocol()){
            case ROCKET_MQ:
                if (Objects.nonNull(defaultRocketMqConfig)) {
                    target.setQueueId(Optional.ofNullable(defaultRocketMqConfig.getGroupName())
                            .orElse(defaultRocketMqConfig.getInstanceName()));
                    target.setBootstrapServers(defaultRocketMqConfig.getBootstrapServers());
                    target.setRocketMqAccessKey(defaultRocketMqConfig.getAccessKey());
                    target.setRocketMqSecretKey(defaultRocketMqConfig.getSecretKey());
                }
                break;
            case KAFKA:
                if (Objects.nonNull(defaultKafkaConfig)) {
                    target.setQueueId(defaultKafkaConfig.getGroupName());
                    target.setBootstrapServers(defaultKafkaConfig.getBootstrapServers());
                }
                break;
            case C_MQ:
                if (Objects.nonNull(defaultCmqConfig)) {
                    target.setQueueId(defaultCmqConfig.getConsumerQueue());
                    target.setBootstrapServers(defaultCmqConfig.getVpcNameserverUrl());
                    target.setBatchPullNumber(Integer.parseInt(defaultCmqConfig.getConsumerBatchPullNum()));
                    target.setPollingWaitSeconds(Integer.parseInt(defaultCmqConfig.getConsumerPullWaitSecond()));
                    target.setRequestTimeout(Integer.parseInt(defaultCmqConfig.getConsumerRequestTimeout()));
                    target.setSecretId(defaultCmqConfig.getSecretId());
                    target.setSecretKey(defaultCmqConfig.getSecretKey());
                    break;
                }
            case RABBITMQ:
                if (Objects.nonNull(defaultRabbitMqConfig)) {
                    target.setBootstrapServers(defaultRabbitMqConfig.getBootstrapServers());
                    target.setQueueId(defaultRabbitMqConfig.getQueueName());
                    target.setSecretId(defaultRabbitMqConfig.getUserName());
                    target.setSecretKey(defaultRabbitMqConfig.getPassword());
                    target.setAutoCreateTopic(defaultRabbitMqConfig.getAutoCreateTopic());
                    break;
                }
            default :
                throw new RuntimeException("Invalid MQ protocol!");
        }
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
