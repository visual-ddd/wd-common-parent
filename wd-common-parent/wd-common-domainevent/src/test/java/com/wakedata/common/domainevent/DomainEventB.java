package com.wakedata.common.domainevent;

import com.wakedata.common.domainevent.model.BaseDomainEvent;
import lombok.Data;

@Data
public class DomainEventB extends BaseDomainEvent {

    private String message;

    public DomainEventB() {
        this.message = "消息体";
    }

    @Override
    public String eventCode() {
        return "${project.domain-event.event-code-b}";
    }
}
