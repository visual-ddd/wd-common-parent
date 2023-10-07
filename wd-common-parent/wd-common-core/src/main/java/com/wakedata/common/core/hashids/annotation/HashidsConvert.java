package com.wakedata.common.core.hashids.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wakedata.common.core.hashids.HashidsConstant;
import com.wakedata.common.core.hashids.HashidsDeSerializer;
import com.wakedata.common.core.hashids.HashidsSerializer;

import java.lang.annotation.*;

/**
 * @author luomeng
 * @Description 将数字转换成短的、唯一的、可解码的哈希，相互转换
 * @createTime 2022-05-13 15:19:00
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.PARAMETER})
@JacksonAnnotationsInside
@Documented
@JsonSerialize(using = HashidsSerializer.class)
@JsonDeserialize(using = HashidsDeSerializer.class)
public @interface HashidsConvert {

    /**
     * hash串加密用的盐值
     * @return
     */
    String salt() default HashidsConstant.DEFAULT_SALT;

    /**
     * 生成的最小hash长度
     * @return
     */
    int minHashLength() default HashidsConstant.DEFAULT_MIN_HASH_LENGTH;
}
