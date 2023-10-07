package com.wakedata.common.spring.i18n.timezone;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.wakedata.common.spring.i18n.timezone.annotation.TimeZoneDateTimeFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 根据时区序列化时间
 *
 * @author luomeng
 * @date 2022/4/14 20:00
 */
@Slf4j
public class TimeZoneDateTimeSerializer extends JsonSerializer<Object> implements ContextualSerializer {

    /**
     * 时间格式
     */
    private String pattern;

    /**
     * 是否返回utc时间
     */
    private boolean utcTime;


    /**
     * 序列化
     *
     * @param dateTime
     * @param jsonGenerator
     * @param serializerProvider
     * @throws IOException
     */
    @Override
    public void serialize(Object dateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (!TimeZoneDateTimeUtil.checkProperty(dateTime.getClass())) {
            return;
        }
        LocalDateTime time = toLocalDateTime(dateTime);
        //时区转换
        if (this.utcTime) {
            time = LocalDateTimeUtil.ofUTC(DateUtil.toInstant(time));
        } else {
            time = TimeZoneDateTimeUtil.timeZoneConverter(time
                    , TimeZoneDateTimeUtil.getSystemConfigTimeZone()
                    , LocaleContextHolder.getTimeZone().toZoneId());
        }
        //未指定格式，返回时间戳
        if (StrUtil.isBlank(this.pattern)) {
            jsonGenerator.writeNumber(LocalDateTimeUtil.toEpochMilli(time));
            return;
        }
        //按指定格式进行解析
        jsonGenerator.writeString(LocalDateTimeUtil.format(time, this.pattern));

    }

    /**
     * 根据上下文信息定制jsonserializer 时间格式
     *
     * @param serializerProvider
     * @param beanProperty
     * @return
     * @throws JsonMappingException
     */
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty == null) {
            return serializerProvider.findNullValueSerializer(null);
        }
        //只读取时间类型
        if (!TimeZoneDateTimeUtil.checkProperty(beanProperty.getType().getRawClass())) {
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        //获取属性上的注解
        TimeZoneDateTimeFormat annotation = TimeZoneDateTimeUtil.getTimeZoneDateTimeFormat(beanProperty);
        if (annotation != null) {
            this.pattern = annotation.pattern();
            this.utcTime = annotation.utcTime();
            return this;
        }
        return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
    }

    /**
     * 转换为localDatetime
     *
     * @param dateTime
     * @return
     */
    private LocalDateTime toLocalDateTime(Object dateTime) {
        if (dateTime instanceof Date) {
            return LocalDateTimeUtil.of((Date) dateTime);
        } else if (dateTime instanceof LocalDate) {
            return ((LocalDate) dateTime).atStartOfDay();
        }
        return (LocalDateTime) dateTime;
    }

}
