package com.wakedata.common.domainevent.model;

import com.wakedata.common.domainevent.annotation.DomainEventSubscribe;
import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.Method;

/**
 * 注解应用目标 - 包装类
 *
 * @author chenshaopeng
 * @date 2021/12/9
 */
@Getter
@Builder
public class DomainAnnotationApplyTarget {

    /**
     * 目标bean
     */
    private final Object bean;

    /**
     * 目标方法
     */
    private final Method method;

    /**
     * 注解信息
     */
    private final DomainEventSubscribe annotation;
}
