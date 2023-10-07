package com.wakedata.common.spring.i18n.timezone;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * 时区公共配置
 *
 * @author luomeng
 * @date 2022/4/15 16:47
 */
@Data
public class TimeZoneDateTimeConfig {

    /**
     * 指定默认时区
     * 目前数据库连接信息中会配置使用的时区，因此此处需要配置默认时区和数据库保持一致，不然时区转换可能会出错
     */
    @Value("${common.timezone.default:Asia/Shanghai}")
    private String defaultTimeZone;

}
