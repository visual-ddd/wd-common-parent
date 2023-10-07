package com.wakedata.common.domainevent.service;

import com.wakedata.common.core.GlobalApplicationContext;
import com.wakedata.common.domainevent.common.MessageBodyBuilder;
import com.wakedata.common.domainevent.config.DomainEventConfig;
import com.wakedata.common.domainevent.model.BaseDomainEvent;
import com.wakedata.common.mq.MqProducerHelper;
import com.wakedata.common.mq.enums.RequestProtocolEnum;
import javax.annotation.Resource;

/**
 * rabbitMq 领域事件实现 - 基于默认配置
 *
 * @author ZhuXueLiang
 * @version 1.0
 * @date 2022/11/30 15:28
 */
public class RabbitMqDomainEventSupportImpl extends AbstractDomainEventSupport {

    public static final RequestProtocolEnum PROTOCOL = RequestProtocolEnum.RABBITMQ;

    @Resource
    private DomainEventConfig config;

    @Override
    public void publish(BaseDomainEvent event) {
        MqProducerHelper.publish(
            MessageBodyBuilder.build(event),
            PROTOCOL,
            config.getDomainEventTopic(),
            event.getEventCode(),
            GlobalApplicationContext.getApplicationContext().getId()
        );
    }

}
