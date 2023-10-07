package com.wakedata.common.domainevent.core;

import com.alibaba.fastjson.JSON;
import com.wakedata.common.core.context.BaseUserInfo;
import com.wakedata.common.core.context.UserInfoContext;
import com.wakedata.common.core.thread.ThreadPoolUtils;
import com.wakedata.common.domainevent.model.BaseDomainEvent;
import com.wakedata.common.domainevent.model.DomainAnnotationApplyTarget;
import com.wakedata.common.domainevent.retry.event.RetryEventPublisher;
import com.wakedata.common.domainevent.retry.util.AopTargetUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * 领域事件本地消费支持类
 *
 * @author chenshaopeng
 * @date 2022/2/11
 */
@Slf4j
public class DomainEventLocalConsumeSupport {

    private final static Map<String, List<DomainAnnotationApplyTarget>> CONSUMERS = new HashMap<>();

    /**
     * 领域事件本地订阅
     */
    public void createLocalSubscribe(DomainAnnotationApplyTarget applyTarget) {
        try {
            String consumerKey = applyTarget.getMethod().getParameterTypes()[0].getName();
            List<DomainAnnotationApplyTarget> applyTargetList = CONSUMERS.containsKey(consumerKey)
                    ? CONSUMERS.get(consumerKey) : new ArrayList<>();
            applyTargetList.add(applyTarget);
            CONSUMERS.put(consumerKey, applyTargetList);
        } catch (Exception e) {
            log.error("Failed to create a local event subscription", e);
        }
    }

    /**
     * 领域事件本地消费, 同步并行消费
     * 本地方法多线程调用，主线程等待子线程完成（阻塞）
     */
    public void startLocalConsume(BaseDomainEvent domainEvent) {
        Optional.ofNullable(CONSUMERS.get(domainEvent.getClass().getName())).ifPresent(targets -> {
            BaseUserInfo userInfo = UserInfoContext.getUser();

            CompletableFuture.allOf(targets.stream().map(target ->
                    CompletableFuture.runAsync(() ->
                                    invokeBusinessMethod(domainEvent, userInfo, target)
                            , ThreadPoolUtils.getThreadPool("domain-event")
                    )
            ).toArray(CompletableFuture[]::new)).join();
        });
    }

    /**
     * 调用业务方法
     */
    private void invokeBusinessMethod(BaseDomainEvent domainEvent, BaseUserInfo userInfo
            , DomainAnnotationApplyTarget target) {
        UserInfoContext.setUser(userInfo);
        try {
            target.getMethod().setAccessible(true);
            target.getMethod().invoke(target.getBean(), domainEvent);
        } catch (Exception e) {
            log.error("Domain event local consumption failed", e);
            sendConsumeErrorEvent(domainEvent, target);
        } finally {
            UserInfoContext.removeUserInfoContext();
        }
    }

    /**
     * 记录消费失败事件
     * @param domainEvent
     * @param target
     */
    private void sendConsumeErrorEvent(BaseDomainEvent domainEvent, DomainAnnotationApplyTarget target) {
        String targetClass;
        try {
            targetClass = AopTargetUtils.getTarget(target.getBean()).getClass().getName();
        } catch (Exception e) {
            log.error("get class name occurs error:", e);
            throw new RuntimeException(e.getMessage());
        }
        String targetMethod = target.getMethod().getName();
        String targetMethodParamClass = target.getMethod().getParameterTypes()[0].getName();
        RetryEventPublisher.send(targetClass, targetMethod, targetMethodParamClass, JSON.toJSONString(domainEvent));
    }

}
