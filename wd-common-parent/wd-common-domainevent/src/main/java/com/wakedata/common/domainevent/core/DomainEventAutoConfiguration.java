package com.wakedata.common.domainevent.core;

import com.wakedata.common.domainevent.config.DomainEventConfig;
import com.wakedata.common.domainevent.service.AbstractDomainEventSupport;
import com.wakedata.common.domainevent.service.KafkaDomainEventSupportImpl;
import com.wakedata.common.domainevent.service.RabbitMqDomainEventSupportImpl;
import com.wakedata.common.domainevent.service.RocketMqDomainEventSupportImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;

/**
 * 领域事件自动装配
 *
 * @author chenshaopeng
 * @date 2021/12/16
 */
@Configuration
@Import({DomainEventConfig.class, DomainEventAnnotationBeanPostProcessor.class})
public class DomainEventAutoConfiguration {

    @Resource
    private DomainEventConfig configuration;

    @Bean
    public AbstractDomainEventSupport domainEventHelper(){
        if (KafkaDomainEventSupportImpl.PROTOCOL.equals(configuration.getDomainEventProtocol())) {
            return new KafkaDomainEventSupportImpl();
        }
        if (RabbitMqDomainEventSupportImpl.PROTOCOL.equals(configuration.getDomainEventProtocol())) {
            return new RabbitMqDomainEventSupportImpl();
        }
        return new RocketMqDomainEventSupportImpl();
    }

}
