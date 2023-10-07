package com.wakedata.common.spring;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.CharsetUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * spring自定义全局配置
 *
 * @author chenshaopeng
 * @date 2022/2/10
 */
public class SpringGlobalConfig {

    public static final String DATE_FORMAT = DatePattern.NORM_DATETIME_PATTERN;

    public static final DateTimeFormatter DATETIME_FORMATTER = DatePattern.NORM_DATETIME_FORMATTER;

    public static final DateTimeFormatter NORM_DATE_FORMATTER = DatePattern.NORM_DATE_FORMATTER;


    /**
     * 配置Jackson对LocalDateTime转换的格式
     */
    @Bean
    @ConditionalOnMissingBean(MappingJackson2HttpMessageConverter.class)
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(){
        MappingJackson2HttpMessageConverter httpMessageConverter = new MappingJackson2HttpMessageConverter();

        ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder()
                .findModulesViaServiceLoader(true)
                .dateFormat(new SimpleDateFormat(DATE_FORMAT))
                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DATETIME_FORMATTER))
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DATETIME_FORMATTER))
                .serializerByType(LocalDate.class, new LocalDateSerializer(NORM_DATE_FORMATTER))
                .deserializerByType(LocalDate.class, new LocalDateDeserializer(NORM_DATE_FORMATTER))
                .build();
        httpMessageConverter.setObjectMapper(objectMapper);
        httpMessageConverter.setDefaultCharset(CharsetUtil.CHARSET_UTF_8);
        return httpMessageConverter;
    }


}
