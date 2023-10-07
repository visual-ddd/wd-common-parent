package com.wakedata.common.redis.lock.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hhf
 * @date 2020/12/28
 */
@Target(value = {ElementType.PARAMETER, ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface LockKey {
    String value() default "";
}