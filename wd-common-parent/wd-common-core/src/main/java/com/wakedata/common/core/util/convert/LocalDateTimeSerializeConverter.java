package com.wakedata.common.core.util.convert;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @ClassName LocalDateTimeSerializeConverter
 * @Description LocalDateTime 转换为UTC时间戳(适用于北京时间)
 * @Date 2022/4/13 16:50
 * @Author LinXuPei
 */
public class LocalDateTimeSerializeConverter extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(LocalDateTimeUtil.toEpochMilli(DateUtil.toInstant(value)));
    }
}
