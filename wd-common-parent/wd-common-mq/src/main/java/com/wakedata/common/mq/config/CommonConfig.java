package com.wakedata.common.mq.config;

import com.wakedata.common.mq.enums.RequestProtocolEnum;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * 公共配置
 *
 * @author chenshaopeng
 * @date 2021/12/20
 */
@Data
public class CommonConfig {

    /**
     * 协议配置，默认使用rocketMQ
     */
    public static RequestProtocolEnum protocol;


    @Value("${common.mq.message.default-type:rocketMq}")
    public void setProtocol(String protocol) {
        CommonConfig.protocol = RequestProtocolEnum.covert(protocol);
    }
}
