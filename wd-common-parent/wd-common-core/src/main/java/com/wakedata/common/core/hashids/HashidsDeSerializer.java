package com.wakedata.common.core.hashids;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.wakedata.common.core.hashids.annotation.HashidsConvert;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;

/**
 * @author luomeng
 * @Description 将hash解码成id
 * @createTime 2022-05-13 15:22:00
 */
@Slf4j
public class HashidsDeSerializer extends JsonDeserializer<Object> implements ContextualDeserializer {

    /**
     * hash串加密用的盐值
     */
    private String salt;
    /**
     * 生成的最小hash长度
     */
    private Integer minHashLength;

    /**
     * 属性的类型
     */
    private Class<?> propertyTypeClass;


    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String hash = jsonParser.getText();
        if (StrUtil.isBlank(hash)) {
            return null;
        }
        if (this.propertyTypeClass == null
                || !HashidsUtil.checkProperty(this.propertyTypeClass)) {
            return null;
        }
        //基础数据类型默认值返回0
        if(isPrimitive(hash)){
            return 0;
        }
        long id = HashidsUtil.decode(hash, salt, minHashLength);
        return HashidsUtil.convertId(this.propertyTypeClass,id);
    }

    /**
     * 基础类型
     * @param hash
     * @return
     */
    private boolean isPrimitive(String hash) {
        return HashidsConstant.DEFAULT_HASH.equals(hash) &&
                (Objects.equals(this.propertyTypeClass,int.class)
                || Objects.equals(this.propertyTypeClass,long.class));
    }


    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext deserializationContext, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty == null) {
            return this;
        }
        //只读取number类型
        if (!HashidsUtil.checkProperty(beanProperty.getType().getRawClass())) {
            return this;
        }
        this.propertyTypeClass = beanProperty.getType().getRawClass();
        //获取属性上的注解
        HashidsConvert annotation = HashidsUtil.getHashidsConvertAnnotation(beanProperty);
        if(annotation != null){
            this.salt = annotation.salt();
            this.minHashLength = annotation.minHashLength();
            return this;
        }
        return this;
    }


}
