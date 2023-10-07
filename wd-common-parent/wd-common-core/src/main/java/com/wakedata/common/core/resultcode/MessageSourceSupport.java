package com.wakedata.common.core.resultcode;

/**
 * 消息国际化支持类
 *
 * @author chenshaopeng
 * @date 2022/3/2
 */
public interface MessageSourceSupport {

    /**
     * 获取国际化message
     *
     * @param key message键
     * @return 国际化message
     */
    String getMessageByDefaultLocale(String key);

}
