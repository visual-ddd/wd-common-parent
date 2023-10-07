package com.wakedata.common.mq.config;

import com.wakedata.common.mq.enums.RequestProtocolEnum;
import com.wakedata.common.mq.model.MqConfigParam;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 私有MQ相关配置
 *
 * @author chenshaopeng
 * @date 2021/12/8
 */
@Getter
@Setter
public class PrivateMqConfig {

    public static final String PREFIX = "private.mq";

    private List<Config> configs;

    @Getter
    @Setter
    public static class Config {
        private ConfigDetail item;
    }

    @Getter
    @Setter
    public static class ConfigDetail extends MqConfigParam {

        /**
         * 配置名称
         */
        private String name;


        public void setProtocol(String protocol) {
            super.setProtocol(RequestProtocolEnum.covert(protocol));
        }

        @Override
        public RequestProtocolEnum getProtocol() {
            return super.getProtocol();
        }
    }

}
