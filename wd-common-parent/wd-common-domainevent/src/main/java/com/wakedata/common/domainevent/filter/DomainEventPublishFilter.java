package com.wakedata.common.domainevent.filter;

import com.wakedata.common.core.GlobalApplicationContext;
import com.wakedata.common.core.context.UserInfoContext;
import com.wakedata.common.domainevent.core.DomainEventLocalConsumeSupport;
import com.wakedata.common.domainevent.model.BaseDomainEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 领域事件publish过滤器
 *
 * @author chenshaopeng
 * @date 2022/2/11
 */
public class DomainEventPublishFilter {

    private final List<Runnable> filterMethodExecLink = new ArrayList<>();

    private final BaseDomainEvent domainEvent;


    /**
     * 创建过滤器链
     */
    private void createFilterLink(){

        // 注册`初始化事件用户信息上下文`
        registryFilter(() -> domainEvent.setUserInfoContext(UserInfoContext.getUser()));

//        // 注册`事件溯源应用`
//        registryFilter(() -> EventLifecycle.apply(domainEvent));

        // 注册`事件本地消费处理`
        registryFilter(() -> Objects.requireNonNull(GlobalApplicationContext
                .getBean(DomainEventLocalConsumeSupport.class)).startLocalConsume(domainEvent));
    }


    public DomainEventPublishFilter(BaseDomainEvent domainEvent) {
        this.domainEvent = domainEvent;
        this.createFilterLink();
    }

    /**
     * 注册过滤器
     */
    private void registryFilter(Runnable command){
        filterMethodExecLink.add(command);
    }

    /**
     * 执行过滤器
     */
    private BaseDomainEvent execFilterLink(){
        filterMethodExecLink.forEach(Runnable::run);
        return domainEvent;
    }


    /**
     * 调用过滤器
     */
    public static BaseDomainEvent invokeFilter(BaseDomainEvent baseDomainEvent){
        return new DomainEventPublishFilter(baseDomainEvent).execFilterLink();
    }

}
