package com.wakedata.common.redis.delayed.annotation;


import java.lang.annotation.*;

/**
 * 延时消息监听
 * @author hhf
 * @date 2022/04/11
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DelayMsgSubscribe {
}
