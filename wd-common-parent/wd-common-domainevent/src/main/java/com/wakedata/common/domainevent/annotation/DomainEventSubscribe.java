package com.wakedata.common.domainevent.annotation;


import com.wakedata.common.core.constants.IndustryEnum;

import java.lang.annotation.*;

/**
 * 领域事件订阅注解
 *
 * @author chenshaopeng
 * @date 2021/12/16
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DomainEventSubscribe {

    /**
     * 指定并覆盖全局配置的topic
     */
    String topic() default "";


    /**
     * 指定行业,指定行业后，仅接收该行业的消息
     * @return
     */
    IndustryEnum industry() default IndustryEnum.DEFAULT;

    /**
     * 是否本地消费
     * 当为`true`时，本地订阅者会直接消费该领域事件
     * 虽然是本地调用进行消费，但还是会发送一个MQ外部事件
     */
    boolean localConsume() default false;

}
