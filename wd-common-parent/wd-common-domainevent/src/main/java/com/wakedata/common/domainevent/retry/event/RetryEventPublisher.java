package com.wakedata.common.domainevent.retry.event;

import com.wakedata.common.core.GlobalApplicationContext;
import org.springframework.context.ApplicationEventPublisher;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Objects;

/**
 * 重试事件发送者
 *
 * @author hhf
 * @date 2022/6/14
 */
public class RetryEventPublisher {
    @Resource
    private ApplicationEventPublisher publisher;

    private static boolean enableRetryEvent = false;

    @PostConstruct
    public void init() {
        enableRetryEvent = true;
    }

    private void sendAsync(String className, String methodName, String paramClassName, String paramValue) {
        publisher.publishEvent(new RetryEvent(this, className, methodName, paramClassName, paramValue));
    }

    public static void send(String className, String methodName, String paramClassName, String paramValue) {
        if (enableRetryEvent) {
            Objects.requireNonNull(GlobalApplicationContext.getBean(RetryEventPublisher.class))
                    .sendAsync(className, methodName, paramClassName, paramValue);
        }
    }
}