package com.wakedata.common.redis.delayed.model;

import com.wakedata.common.redis.delayed.annotation.DelayMsgSubscribe;
import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.Method;

/**
 * 注解应用目标 - 包装类
 *
 * @author hhf
 * @date 2022/04/11
 */
@Getter
@Builder
public class SubscriberAnnotationApplyTarget {

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
    private final DelayMsgSubscribe annotation;

}
