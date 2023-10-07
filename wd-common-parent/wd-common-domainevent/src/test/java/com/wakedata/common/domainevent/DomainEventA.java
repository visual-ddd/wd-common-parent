package com.wakedata.common.domainevent;

import com.wakedata.common.domainevent.model.BaseDomainEvent;
import lombok.Data;

@Data
public class DomainEventA extends BaseDomainEvent {

    private String message;

    public DomainEventA() {
        this.message = "消息体";
    }

    @Override
    public String eventCode() {
        return "${project.domain-event.event-code-a}";
    }
}
