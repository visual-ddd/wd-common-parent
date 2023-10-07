package com.wakedata.common.domainevent.service;

import com.wakedata.common.core.GlobalApplicationContext;
import com.wakedata.common.domainevent.common.MessageBodyBuilder;
import com.wakedata.common.domainevent.model.BaseDomainEvent;
import com.wakedata.common.mq.MqProducerHelper;
import com.wakedata.common.mq.enums.RequestProtocolEnum;

import javax.annotation.Resource;

/**
 * ‘RocketMQ’领域事件实现 - 基于默认配置
 *
 * @author chenshaopeng
 * @date 2021/12/16
 */
public class RocketMqDomainEventSupportImpl extends AbstractDomainEventSupport {

    public static final RequestProtocolEnum PROTOCOL = RequestProtocolEnum.ROCKET_MQ;

    @Resource
    private com.wakedata.common.domainevent.config.DomainEventConfig config;


    @Override
    public void publish(BaseDomainEvent event) {

         //领域事件发送的时候会对事件进行封装  为了过滤消息使用eventCode作为tag
        MqProducerHelper.publish(MessageBodyBuilder.build(event), PROTOCOL, config.getDomainEventTopic()
                , event.getEventCode(), GlobalApplicationContext.getApplicationContext().getId()
        );
    }

}
