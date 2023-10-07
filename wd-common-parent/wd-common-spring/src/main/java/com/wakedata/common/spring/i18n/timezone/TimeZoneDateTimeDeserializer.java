package com.wakedata.common.spring.i18n.timezone;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.wakedata.common.spring.i18n.timezone.annotation.TimeZoneDateTimeFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * @author luomeng
 * @Description 根据时区反序列化时间
 * @createTime 2022-04-17 17:10:00
 */
@Slf4j
public class TimeZoneDateTimeDeserializer extends JsonDeserializer<Object> implements ContextualDeserializer {

    /**
     * 时间格式
     */
    private String pattern;

    /**
     * 反序列化时按时区进行转换
     */
    private boolean deserializeTimeZone;

    /**
     * 属性的类型
     */
    private Class<?> propertyTypeClass;

    /**
     * 反序列化
     *
     * @param jsonParser
     * @param deserializationContext
     * @return
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        //获取时间
        String dateStr = jsonParser.getText();
        if (StrUtil.isBlank(dateStr)) {
            return null;
        }
        //非时间类型
        if (this.propertyTypeClass == null
                || !TimeZoneDateTimeUtil.checkProperty(this.propertyTypeClass)) {
            return null;
        }
        //时间转换
        if (StrUtil.isBlank(this.pattern)) {
            this.pattern = DatePattern.NORM_DATETIME_PATTERN;
        }
        LocalDateTime dateTime;
        if (StringUtils.isNumeric(dateStr)) {
            dateTime = LocalDateTimeUtil.of(Long.parseLong(dateStr));
            dateStr = LocalDateTimeUtil.format(dateTime, this.pattern);
        }
        dateTime = LocalDateTimeUtil.parse(dateStr, this.pattern);
        //时区转换
        if (this.deserializeTimeZone) {
            dateTime = TimeZoneDateTimeUtil.timeZoneConverter(dateTime
                    , LocaleContextHolder.getTimeZone().toZoneId()
                    , TimeZoneDateTimeUtil.getSystemConfigTimeZone());
        }
        return convertLocalDateTime(dateTime);
    }

    /**
     * localDatetime转换成指定类型
     *
     * @param dateTime
     * @return
     */
    private Object convertLocalDateTime(LocalDateTime dateTime) {
        if (Objects.equals(this.propertyTypeClass, Date.class)) {
            return Date.from(dateTime.atZone(TimeZoneDateTimeUtil.getSystemConfigTimeZone()).toInstant());
        } else if (Objects.equals(this.propertyTypeClass, LocalDate.class)) {
            return dateTime.toLocalDate();
        }
        return dateTime;
    }

    /**
     * 根据上下文信息定制jsonDeserializer 时间格式
     *
     * @param deserializationContext
     * @param beanProperty
     * @return
     * @throws JsonMappingException
     */
    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext deserializationContext, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty == null) {
            return this;
        }
        //只读取时间类型
        if (!TimeZoneDateTimeUtil.checkProperty(beanProperty.getType().getRawClass())) {
            return this;
        }
        this.propertyTypeClass = beanProperty.getType().getRawClass();
        //获取属性上的注解
        TimeZoneDateTimeFormat annotation = TimeZoneDateTimeUtil.getTimeZoneDateTimeFormat(beanProperty);
        if (annotation != null) {
            this.pattern = annotation.pattern();
            this.deserializeTimeZone = annotation.deserializeTimeZone();
            return this;
        }
        return this;
    }
}
