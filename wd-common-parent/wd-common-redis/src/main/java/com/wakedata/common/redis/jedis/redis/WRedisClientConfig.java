package com.wakedata.common.redis.jedis.redis;

/**
 * redis客户端配置类
 * @author liaofei
 * @date 2019/9/12
 */
@Deprecated
public class WRedisClientConfig {

    private String keyPrefix = "";
    private String keySuffix = "";

    /**
     * @return the keyPrefix
     */
    public String getKeyPrefix() {
        return keyPrefix;
    }

    /**
     * @param keyPrefix the keyPrefix to set
     */
    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    /**
     * @return the keySuffix
     */
    public String getKeySuffix() {
        return keySuffix;
    }

    /**
     * @param keySuffix the keySuffix to set
     */
    public void setKeySuffix(String keySuffix) {
        this.keySuffix = keySuffix;
    }
}
