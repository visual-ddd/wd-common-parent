package com.wakedata.common.mq;

import com.wakedata.common.core.GlobalApplicationContext;
import com.wakedata.common.mq.common.MqConfigurationBuilder;
import com.wakedata.common.mq.core.CreationMessageConsumer;
import com.wakedata.common.mq.enums.RequestProtocolEnum;
import com.wakedata.common.mq.model.MqConfigParam;
import com.wakedata.common.mq.common.MqConfigParamConvert;
import com.wakedata.common.mq.model.consumer.MakeSubscribeBody;
import com.wakedata.common.mq.model.consumer.Message;

import java.util.Objects;
import java.util.function.Consumer;


/**
 * MQ消费者工具类
 *
 * @author chenshaopeng
 * @date 2021/12/11
 */
public class MqConsumerHelper {

    /**
     * 基于默认配置订阅
     *
     * @param msgConsumer 消息消费者
     * @param topic 主题
     */
    public static void subscribe(Consumer<Message> msgConsumer, String topic) {
        subscribe(msgConsumer, topic, null);
    }

    /**
     * 基于默认配置订阅
     *
     * @param msgConsumer 消息消费者
     * @param topic 主题
     * @param tag 标签
     */
    public static void subscribe(Consumer<Message> msgConsumer, String topic, String tag) {
        subscribe(msgConsumer, null, topic, tag);
    }

    /**
     * 基于默认配置订阅
     *
     * @param msgConsumer 消息消费者
     * @param protocol 协议
     * @param topic 主题
     */
    public static void subscribe(Consumer<Message> msgConsumer, RequestProtocolEnum protocol, String topic) {
        subscribe(msgConsumer, protocol, topic, null);
    }

    /**
     * 基于默认配置订阅
     *
     * @param msgConsumer 消息消费者
     * @param protocol 协议
     * @param topic 主题
     * @param tag 标签
     */
    public static void subscribe(Consumer<Message> msgConsumer, RequestProtocolEnum protocol, String topic, String tag) {
        MqConfigParam customParams = new MqConfigParam();
        customParams.setTopic(topic);
        customParams.setTag(tag);
        createConfigAndSubscribe(msgConsumer, protocol, null, customParams);
    }

    /**
     * 基于自定义配置订阅
     *
     * @param msgConsumer 消息消费者
     * @param customParams 自定义参数
     */
    public static void subscribe(Consumer<Message> msgConsumer, MqConfigParam customParams) {
        createConfigAndSubscribe(msgConsumer, null, null, customParams);
    }

    /**
     * 基于私有配置订阅
     *
     * @param msgConsumer 消息消费者
     * @param privateConfigName 私有配置名称
     */
    public static void subscribeForPrivateConfig(Consumer<Message> msgConsumer, String privateConfigName) {
        createConfigAndSubscribe(msgConsumer, null, privateConfigName, null);
    }

    /**
     * 创建配置并订阅
     *
     * @param msgConsumer 消息消费者
     * @param protocol 协议
     * @param privateConfigName 私有配置名称
     * @param customParams 自定义配置参数
     */
    private static void createConfigAndSubscribe(Consumer<Message> msgConsumer, RequestProtocolEnum protocol
            , String privateConfigName, MqConfigParam customParams){
        customParams = MqConfigurationBuilder.build(protocol, privateConfigName, customParams);

        MakeSubscribeBody subscribeBody = MqConfigParamConvert.convertToMakeSubscribeBody(customParams);
        subscribeBody.checkRequisiteParam();

        Objects.requireNonNull(GlobalApplicationContext.getBean(CreationMessageConsumer.class))
                .createSubscribe(subscribeBody, msgConsumer);
    }



}
