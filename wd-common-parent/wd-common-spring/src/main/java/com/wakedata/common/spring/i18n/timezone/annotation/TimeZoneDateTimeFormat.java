package com.wakedata.common.spring.i18n.timezone.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wakedata.common.spring.i18n.timezone.TimeZoneDateTimeDeserializer;
import com.wakedata.common.spring.i18n.timezone.TimeZoneDateTimeSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 时间按指定时区序列化及反序列化注解
 *
 * @author luomeng
 * @date 2022/4/15 10:01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = TimeZoneDateTimeSerializer.class)
@JsonDeserialize(using = TimeZoneDateTimeDeserializer.class)
public @interface TimeZoneDateTimeFormat {

    /**
     * 时间格式，不填默认解析为时间戳
     *
     * @return
     */
    String pattern() default "";

    /**
     * 是否返回UTC标准时间，默认返回当前时区对应的时间
     *
     * @return
     */
    boolean utcTime() default false;

    /**
     * 反序列化时按时区进行转换，默认启用
     *
     * @return
     */
    boolean deserializeTimeZone() default true;

}
