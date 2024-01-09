package com.wakedata.common.core.constants;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 公共MQ Topic配置类
 *
 * @author chenshaopeng
 * @date 2021/12/17
 */
@Data
@Configuration("commonMqTopic")
public class CommonMqTopic {

    /**
     * 门店服务topic
     */
    @Value("${mq.store.topic:}")
    private String storeTopic;

}
