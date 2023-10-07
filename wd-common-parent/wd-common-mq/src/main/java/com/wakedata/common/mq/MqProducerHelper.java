package com.wakedata.common.mq;

import com.alibaba.fastjson.JSON;
import com.wakedata.common.mq.common.MqConfigurationBuilder;
import com.wakedata.common.mq.enums.RequestProtocolEnum;
import com.wakedata.common.mq.model.MqConfigParam;
import com.wakedata.common.mq.common.MqConfigParamConvert;
import com.wakedata.common.mq.model.producer.MakeMessageBody;


/**
 * MQ生产者工具类
 *
 * @author chenshaopeng
 * @date 2021/12/11
 */
public class MqProducerHelper {

    /**
     * 基于默认配置发送消息
     *
     * @param message 消息对象
     * @param topic 主题
     * @return 发送结果
     */
    public static boolean publish(Object message, String topic) {
        return publish(message, topic, null);
    }

    /**
     * 基于默认配置发送消息
     *
     * @param message 消息对象
     * @param topic 主题
     * @param tag 标签
     * @return 发送结果
     */
    public static boolean publish(Object message, String topic, String tag) {
        return publish(message, null, topic, tag);
    }

    /**
     * 基于默认配置发送消息
     *
     * @param message 消息对象
     * @param protocol 协议
     * @param topic 主题
     * @return 发送结果
     */
    public static boolean publish(Object message, RequestProtocolEnum protocol, String topic) {
        return publish(message, protocol, topic, null);
    }

    /**
     * 基于默认配置发送消息
     *
     * @param message 消息对象
     * @param protocol 协议
     * @param topic 主题
     * @param tag 标签
     * @return 发送结果
     */
    public static boolean publish(Object message, RequestProtocolEnum protocol, String topic, String tag) {
        MqConfigParam customParams = new MqConfigParam();
        customParams.setTopic(topic);
        customParams.setTag(tag);
        return createConfigAndExecSend(message, protocol, null, customParams);
    }

    /**
     * 基于默认配置发送消息
     *
     * @param message 消息对象
     * @param protocol 协议
     * @param topic 主题
     * @param tag 标签
     * @param queueId 队列Id
     * @return 发送结果
     */
    public static boolean publish(Object message, RequestProtocolEnum protocol, String topic, String tag, String queueId) {
        MqConfigParam customParams = new MqConfigParam();
        customParams.setTopic(topic);
        customParams.setTag(tag);
        customParams.setQueueId(queueId);
        return createConfigAndExecSend(message, protocol, null, customParams);
    }

    /**
     * 基于自定义配置发送消息
     * 配置优先级：自定义配置 > 默认配置
     *
     * @param message 消息对象
     * @param customParams 自定义参数
     * @return 发送结果
     */
    public static boolean publish(Object message, MqConfigParam customParams) {
        return createConfigAndExecSend(message, null, null, customParams);
    }

    /**
     * 基于私有配置发送消息
     * 配置优先级：私有配置 > 默认配置
     *
     * @param message 消息对象
     * @param privateConfigName 私有配置名称
     * @return 发送结果
     */
    public static boolean publishForPrivateConfig(Object message, String privateConfigName) {
        return publishForPrivateConfig(message, privateConfigName,null);
    }

    /**
     * 基于私有配置和自定义配置发送消息
     * 配置优先级：自定义配置 > 私有配置 > 默认配置
     *
     * @param message 消息对象
     * @param privateConfigName 私有配置名称
     * @param topic 主题
     * @return 发送结果
     */
    public static boolean publishForPrivateConfig(Object message, String privateConfigName, String topic) {
        return publishForPrivateConfig(message, privateConfigName, topic, null);
    }

    /**
     * 基于私有配置和自定义配置发送消息
     * 配置优先级：自定义配置 > 私有配置 > 默认配置
     *
     * @param message 消息对象
     * @param privateConfigName 私有配置名称
     * @param topic 主题
     * @param tag 标签
     * @return 发送结果
     */
    public static boolean publishForPrivateConfig(Object message, String privateConfigName, String topic, String tag) {
        MqConfigParam customParam = new MqConfigParam();
        customParam.setTopic(topic);
        customParam.setTag(tag);
        return createConfigAndExecSend(message, null, privateConfigName, customParam);
    }

    /**
     * 基于自定义参数发送消息
     *
     * @param message 消息体
     * @param customParam 自定义参数
     * @return 发送结果
     */
    public static boolean publishForCustomParam(Object message, MqConfigParam customParam){
        return createConfigAndExecSend(message, null, null, customParam);
    }

    /**
     * 创建配置并执行发送
     *
     * @param message 消息对象
     * @param protocol 协议
     * @param privateConfigName 私有配置名称
     * @param customParam 自定义配置
     * @return 发送结果
     */
    public static boolean createConfigAndExecSend(Object message, RequestProtocolEnum protocol
            , String privateConfigName, MqConfigParam customParam){
        customParam = MqConfigurationBuilder.build(protocol, privateConfigName, customParam);

        MakeMessageBody body = MqConfigParamConvert.convertToMakeMessageBody(customParam);
        body.setMessage(JSON.toJSONString(message));
        body.checkAndSetQueue();
        body.checkRequisiteParam();

        return customParam.getProtocol().producer().sendMessage(body);
    }

}
