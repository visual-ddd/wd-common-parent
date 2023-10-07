package com.wakedata.common.domainevent;

import com.wakedata.common.domainevent.annotation.DomainEventSubscribe;
import org.springframework.stereotype.Component;

@Component
public class DomainEventHandler {

    @DomainEventSubscribe
    public void domainEventAConsumer(DomainEventA domainEvent) {
        System.out.println("收到信息：" + domainEvent);
    }

    @DomainEventSubscribe
    public void domainEventBConsumer(DomainEventB domainEvent) {
        System.out.println("收到信息：" + domainEvent);
    }
}
