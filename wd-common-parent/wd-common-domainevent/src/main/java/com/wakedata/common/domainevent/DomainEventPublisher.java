package com.wakedata.common.domainevent;

import com.wakedata.common.core.GlobalApplicationContext;
import com.wakedata.common.core.exception.SysException;
import com.wakedata.common.domainevent.model.BaseDomainEvent;
import com.wakedata.common.domainevent.service.AbstractDomainEventSupport;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 领域事件发送者服务类
 *
 * @author: hhf
 * @date: 2021/12/16
 **/
@Component
public class DomainEventPublisher {

    private static DomainEventPublisher instance;

    @Resource
    private AbstractDomainEventSupport domainEventSupport;

    public DomainEventPublisher() {

    }

    public static DomainEventPublisher getInstance() {
        if (instance == null) {
            instance = GlobalApplicationContext.getBean(DomainEventPublisher.class);
            if (instance == null) {
                throw new SysException("DomainEventPublisher未实例化");
            }
        }
        return instance;
    }


    /**
     * 同步发送event
     * @param event 事件体
     */
    public void postSync(BaseDomainEvent event) {
        domainEventSupport.postSync(event);
    }

    /**
     * 异步发送event
     * @param event 事件体
     */
    public void postAsync(BaseDomainEvent event) {
        domainEventSupport.postAsync(event);
    }

    /**
     * 事务提交之后，进行消息发送
     * @param e 事件体
     */
    public void postAfterCommit(BaseDomainEvent e) {
        domainEventSupport.postAfterCommit(e);
    }
    /**
     * 二阶段事务提交之后，进行消息发送
     * @param e 事件体
     */
    public void postAfterSeataRMCommit(BaseDomainEvent e) {
        domainEventSupport.postAfterSeataRMCommit(e);
    }
}
