package com.wakedata.common.domainevent.service;

import com.wakedata.common.core.GlobalApplicationContext;
import com.wakedata.common.domainevent.common.MessageBodyBuilder;
import com.wakedata.common.domainevent.model.BaseDomainEvent;
import com.wakedata.common.mq.MqProducerHelper;
import com.wakedata.common.mq.enums.RequestProtocolEnum;

import javax.annotation.Resource;

/**
 * ‘Kafka’领域事件实现 - 基于默认配置
 *
 * @author chenshaopeng
 * @date 2021/12/16
 */
public class KafkaDomainEventSupportImpl extends AbstractDomainEventSupport {

    public static final RequestProtocolEnum PROTOCOL = RequestProtocolEnum.KAFKA;

    @Resource
    private com.wakedata.common.domainevent.config.DomainEventConfig config;


    @Override
    public void publish(BaseDomainEvent event) {
        MqProducerHelper.publish(MessageBodyBuilder.build(event), PROTOCOL, config.getDomainEventTopic()
                , event.getEventCode(), GlobalApplicationContext.getApplicationContext().getId()
        );
    }



}
