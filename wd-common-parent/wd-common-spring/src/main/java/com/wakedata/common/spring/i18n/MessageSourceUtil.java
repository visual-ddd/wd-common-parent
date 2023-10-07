package com.wakedata.common.spring.i18n;

import com.wakedata.common.core.resultcode.MessageSourceSupport;
import com.wakedata.common.spring.util.SpringApplicationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Objects;

/**
 * @author luomeng
 * @Description 读取多语言资源信息
 * @createTime 2021-12-09 16:10:00
 */
@Slf4j
@Component
public class MessageSourceUtil implements MessageSourceSupport {

    @Autowired
    private MessageSource messageSource;

    /**
     * 根据默认的本地化信息读取资源数据
     *
     * @param key 消息键
     * @return
     */
    @Override
    public String getMessageByDefaultLocale(String key) {
        return messageSource.getMessage(key, null, key, getDefaultLocale());
    }

    /**
     * 根据默认的本地化信息读取资源数据，当资源不存在时读取默认消息
     *
     * @param key           消息键
     * @param defaultMessage 默认消息
     * @return
     */
    public String getMessageByDefaultLocale(String key, String defaultMessage) {
        return messageSource.getMessage(key, null, defaultMessage, getDefaultLocale());
    }

    /**
     * 根据默认的本地化信息读取资源数据,可传入参数
     *
     * @param key   消息键
     * @param params 参数
     * @return
     */
    public String getMessageByDefaultLocale(String key, String... params) {
        return messageSource.getMessage(key, params, null, getDefaultLocale());
    }

    /**
     * 根据默认的本地化信息读取资源数据
     *
     * @param key           消息键
     * @param defaultMessage 默认消息
     * @param params         参数
     * @return
     */
    public String getMessageByDefaultLocale(String key, String defaultMessage, String... params) {
        return messageSource.getMessage(key, params, defaultMessage, getDefaultLocale());
    }


    /**
     * 根据当前的本地化信息读取消息
     *
     * @param key   消息键
     * @param locale 本地化信息
     * @return
     */
    public String getMessageByCurrentLocale(String key, Locale locale) {
        return messageSource.getMessage(key, null, null, locale);
    }

    /**
     * 根据当前的本地化信息读取消息，当资源不存在时读取默认消息
     *
     * @param key           消息键
     * @param defaultMessage 默认消息
     * @param locale         本地化信息
     * @return
     */
    public String getMessageByCurrentLocale(String key, String defaultMessage, Locale locale) {
        return messageSource.getMessage(key, null, defaultMessage, locale);
    }

    /**
     * 根据当前的本地化信息读取消息,可传入参数
     *
     * @param key   消息键
     * @param locale 本地化信息
     * @param params 参数
     * @return
     */
    public String getMessageByCurrentLocale(String key, Locale locale, String... params) {
        return messageSource.getMessage(key, params, null, locale);
    }

    /**
     * 根据当前的本地化信息读取消息
     *
     * @param key           消息键
     * @param defaultMessage 默认消息
     * @param locale         本地化信息
     * @param params         参数
     * @return
     */
    public String getMessageByCurrentLocale(String key, String defaultMessage, Locale locale, String... params) {
        return messageSource.getMessage(key, params, defaultMessage, locale);
    }


    /**
     * 获取默认语言环境
     *
     * @return 本地化信息
     */
    private Locale getDefaultLocale() {
        HttpServletRequest request = SpringApplicationUtil.getRequest();
        if (Objects.nonNull(request)){
            return RequestContextUtils.getLocale(request);
        }
        return Locale.getDefault();
    }


}
