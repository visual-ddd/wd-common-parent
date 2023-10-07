package com.wakedata.common.domainevent.retry.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 领域事件重试监听
 * @author hhf
 * @date 2022/6/14
 */
@Getter
public class RetryEvent extends ApplicationEvent {

    private String className;
    private String methodName;
    private String paramClassName;
    private String paramValue;

    public RetryEvent(Object source, String className, String methodName, String paramClassName, String paramValue) {
        super(source);
        this.className = className;
        this.methodName = methodName;
        this.paramClassName = paramClassName;
        this.paramValue = paramValue;
    }

}