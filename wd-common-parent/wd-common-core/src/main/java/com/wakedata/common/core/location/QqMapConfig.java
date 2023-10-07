package com.wakedata.common.core.location;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 腾讯地图Config
 */
@Component
public class QqMapConfig {

    private static String accessKey;

    public static String getAccessKey() {
        return accessKey;
    }

    @Value("${qq.map.accessKey:}")
    public void setAccessKey(String accessKey) {
        QqMapConfig.accessKey = accessKey;
    }
}
