package com.wakedata.common.mq.model;

import com.wakedata.common.mq.enums.RequestProtocolEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 基本Mq配置参数 - 包装类
 *
 * @author chenshaopeng
 * @date 2021/12/10
 */
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseMqConfigParam {

    /**
     * 队列Id
     */
    protected String queueId;

    /**
     * 服务地址
     */
    protected String bootstrapServers;

    /**
     * 主题
     */
    protected String topic;

    /**
     * 标签
     */
    protected String tag;

    /**
     * 协议
     */
    private RequestProtocolEnum protocol;

    /**
     * 请求超时时间
     */
    protected Integer requestTimeout;

    /**
     * secretId - cmq配置
     */
    protected String secretId;

    /**
     * secretKey - cmq配置
     */
    protected String secretKey;

    /**
     * accessKey - rocketMQ配置（非必填）
     */
    protected String rocketMqAccessKey;

    /**
     * secretKey - rocketMQ配置（非必填）
     */
    protected String rocketMqSecretKey;

    /**
     * security.protocol - kafka安全认证配置（非必填）
     */
    protected String securityProtocol;

    /**
     * sasl.mechanism - kafka安全认证配置（非必填）
     */
    protected String saslMechanism;

    /**
     * sasl.jaas.config - kafka安全认证配置（非必填）
     */
    protected String saslJaasConfig;

    /**
     * autoCreateTopic - rabbitMQ配置（非必填）
     */
    protected Boolean autoCreateTopic;

    /**
     * 检查必要参数
     */
    public void checkRequisiteParam(){
        requireNonBlank(queueId, "`queueId` cannot be empty!");
        requireNonBlank(bootstrapServers, "`bootstrapServers` cannot be empty!");
        Objects.requireNonNull(protocol, "`protocol` cannot be empty!");

        if(protocol == RequestProtocolEnum.ROCKET_MQ || protocol == RequestProtocolEnum.KAFKA){
            requireNonBlank(topic, "`topic` cannot be empty!");
        }
    }

    private void requireNonBlank(String value, String message){
        if(StringUtils.isBlank(value)){
            throw new NullPointerException(message);
        }
    }
}
