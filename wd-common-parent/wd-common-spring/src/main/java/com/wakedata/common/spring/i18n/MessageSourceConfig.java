package com.wakedata.common.spring.i18n;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

/**
 * @author luomeng
 * @Description spring messageSource自动注入,自动加载多语言资源文件
 * @createTime 2021-12-09 14:43:00
 */
@Configuration
@Slf4j
public class MessageSourceConfig {
    /**
     * 多语言的资源文件路径,在application.properties中增加配置spring.messages.basename=classpath:i18n/message;classpath:i18n/error
     */
    @Value("${spring.messages.basename}")
    private String path;

    /**
     * 多个资源地址时用分号隔开
     */
    private static final String BASENAME_SPLIT = ";";
    /**
     * 默认编码
     */
    private static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * 多语言信息加载器
     * @return
     */
    @Bean("messageSource")
    public MessageSource messageSource() {
        if (StrUtil.isBlank(path)) {
            log.error("加载多语言资源时未找到资源路径，path:{}", path);
            return null;
        }
        log.info("自动加载多语言资源配置信息...,path:{}", path);
        CustomMessageSource messageSource = new CustomMessageSource();
        // 用分号隔开各个语言资源路径
        String[] paths = path.split(BASENAME_SPLIT);
        messageSource.setBasenames(paths);
        messageSource.setDefaultEncoding(DEFAULT_ENCODING);
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }

    /**
     * 自定义本地化信息加载器
     * @return
     */
    @Bean
    public LocaleResolver localeResolver() {
        return new I18nDataCookieLocaleResolver();
    }
}
