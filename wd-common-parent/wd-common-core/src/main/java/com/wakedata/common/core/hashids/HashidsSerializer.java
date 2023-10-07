package com.wakedata.common.core.hashids;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.wakedata.common.core.hashids.annotation.HashidsConvert;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author luomeng
 * @Description 将id编码成hash串
 * @createTime 2022-05-13 15:21:00
 */
@Slf4j
public class HashidsSerializer  extends JsonSerializer<Object> implements ContextualSerializer {
    /**
     * hash串加密用的盐值
     */
    private String salt;
    /**
     * 生成的最小hash长度
     */
    private Integer minHashLength;


    @Override
    public void serialize(Object id, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (!HashidsUtil.checkProperty(id.getClass())) {
            return;
        }
        Long convertId = convertId(id);
        String hash = HashidsUtil.encode(convertId, salt, minHashLength);
        jsonGenerator.writeString(hash);
    }

    private Long convertId(Object id){
        if(id instanceof Integer){
            return Long.valueOf(String.valueOf(id));
        }
        return (Long)id;
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty == null) {
            return serializerProvider.findNullValueSerializer(null);
        }
        //只读取number类型
        if (!HashidsUtil.checkProperty(beanProperty.getType().getRawClass())) {
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        //获取属性上的注解
        HashidsConvert annotation = HashidsUtil.getHashidsConvertAnnotation(beanProperty);
        if (annotation != null) {
            this.salt = annotation.salt();
            this.minHashLength = annotation.minHashLength();
            return this;
        }
        return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
    }



}
