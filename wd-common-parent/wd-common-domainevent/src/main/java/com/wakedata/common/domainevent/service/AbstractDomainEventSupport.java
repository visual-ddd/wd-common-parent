package com.wakedata.common.domainevent.service;

import com.wakedata.common.core.util.AsyncExecutorUtil;
import com.wakedata.common.domainevent.filter.DomainEventPublishFilter;
import com.wakedata.common.domainevent.model.BaseDomainEvent;
//import io.seata.core.context.RootContext;
//import io.seata.core.model.BranchType;
//import io.seata.rm.RMTransactionHookAdapter;
//import io.seata.rm.RMTransactionHookManager;
//import io.seata.tm.api.transaction.TransactionHookAdapter;
//import io.seata.tm.api.transaction.TransactionHookManager;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 封装同步/异步发送event操作
 *
 * @author chenshaopeng
 * @date 2021/12/16
 */
public abstract class AbstractDomainEventSupport {

    /**
     * 事件发送前置处理
     * @param baseDomainEvent 事件体
     */
    protected BaseDomainEvent beforeProcess(BaseDomainEvent baseDomainEvent){
        return DomainEventPublishFilter.invokeFilter(baseDomainEvent);
    }

    /**
     * 调用MQ生产者发送事件
     * @param event 事件体
     */
    public abstract void publish(BaseDomainEvent event);

    /**
     * 同步发送event
     * @param event 事件体
     */
    public void postSync(BaseDomainEvent event){
        publish(beforeProcess(event));
    }

    /**
     * 异步发送event
     * @param event 事件体
     */
    public void postAsync(BaseDomainEvent event) {
        beforeProcess(event); AsyncExecutorUtil.exec(() -> publish(event));
    }

    /**
     * 事务提交后发送event
     *
     * 优先级：分布式事务 > 本地事务
     * @param event 事件体
     */
    public void postAfterCommit(BaseDomainEvent event) {
      if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    postAsync(event);
                }
            });
        } else {
            postAsync(event);
        }
    }

    /**
     * 在RM二阶段提交后触发
     */
    public void postAfterSeataRMCommit(BaseDomainEvent event) {

    }

}
