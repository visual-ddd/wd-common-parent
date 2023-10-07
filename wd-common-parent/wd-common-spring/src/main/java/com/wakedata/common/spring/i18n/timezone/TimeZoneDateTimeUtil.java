package com.wakedata.common.spring.i18n.timezone;

import com.fasterxml.jackson.databind.BeanProperty;
import com.wakedata.common.core.GlobalApplicationContext;
import com.wakedata.common.spring.i18n.timezone.annotation.TimeZoneDateTimeFormat;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

/**
 * @author luomeng
 * @Description 时区时间序列化工具类
 * @createTime 2022-04-17 17:22:00
 */
@Slf4j
public class TimeZoneDateTimeUtil {

    /**
     * 校验属性类型，只处理时间类型
     *
     * @param rawClass
     * @return
     */
    public static boolean checkProperty(Class<?> rawClass) {
        if (Objects.equals(rawClass, Date.class)
                || Objects.equals(rawClass, LocalDate.class)
                || Objects.equals(rawClass, LocalDateTime.class)) {
            return true;
        }
        return false;
    }

    /**
     * 获取属性上的注解
     *
     * @param beanProperty
     * @return
     */
    public static TimeZoneDateTimeFormat getTimeZoneDateTimeFormat(BeanProperty beanProperty) {
        TimeZoneDateTimeFormat annotation = beanProperty.getAnnotation(TimeZoneDateTimeFormat.class);
        if (annotation == null) {
            annotation = beanProperty.getContextAnnotation(TimeZoneDateTimeFormat.class);
        }
        return annotation;
    }

    /**
     * 时间按时区转换
     *
     * @param time       时间
     * @param fromZoneId 原来的时区
     * @param toZoneId   需要转换到的时区
     * @return
     */
    public static LocalDateTime timeZoneConverter(LocalDateTime time, ZoneId fromZoneId, ZoneId toZoneId) {
        if (fromZoneId.equals(toZoneId)) {
            return time;
        }
        return time.atZone(fromZoneId).withZoneSameInstant(toZoneId).toLocalDateTime();
    }

    /**
     * 获取系统配置的时区信息
     *
     * @return
     */
    public static ZoneId getSystemConfigTimeZone() {
        TimeZoneDateTimeConfig timeZoneDateTimeConfig = GlobalApplicationContext.getBean(TimeZoneDateTimeConfig.class);
        if (timeZoneDateTimeConfig == null) {
            log.error("Error obtaining the default time zone information, use {} time Zone",TimeZoneDateTimeConstant.DEFAULT_TIME_ZONE);
            return ZoneId.of(TimeZoneDateTimeConstant.DEFAULT_TIME_ZONE);
        }
        return ZoneId.of(timeZoneDateTimeConfig.getDefaultTimeZone());
    }


}
