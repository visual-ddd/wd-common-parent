package com.wakedata.common.spring.i18n;

import cn.hutool.core.util.StrUtil;
import com.wakedata.common.core.GlobalApplicationContext;
import com.wakedata.common.spring.i18n.timezone.TimeZoneDateTimeConfig;
import com.wakedata.common.spring.i18n.timezone.TimeZoneDateTimeConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.context.i18n.SimpleTimeZoneAwareLocaleContext;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author luomeng
 * @Description 使用自定义国际化拦截器
 * @createTime 2022-04-18 20:55:00
 */
@Slf4j
public class I18nDataCookieLocaleResolver extends CookieLocaleResolver {

    /**
     * 用户语言Key
     */
    private static final String LANGUAGE = "Accept-Language";
    /**
     * 时区信息Key
     */
    private static final String TIMEZONE = "Accept-Timezone";

    /**
     * 该方法只改变响应的本地化信息
     *
     * @param request
     * @return
     */
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        return setLocale(request);
    }

    /**
     * 更改本地化信息
     *
     * @param request
     * @return
     */
    @Override
    public LocaleContext resolveLocaleContext(HttpServletRequest request) {

        Locale locale = setLocale(request);
        TimeZone timeZone = setLocaleTimeZone(request);
        if (timeZone != null) {
            return new SimpleTimeZoneAwareLocaleContext(locale, timeZone);
        } else if (locale != null) {
            return new SimpleLocaleContext(locale);
        }
        return super.resolveLocaleContext(request);
    }

    /**
     * 设置本地化时区信息
     *
     * @param request
     */
    private TimeZone setLocaleTimeZone(HttpServletRequest request) {
        String reqTimeZone = getRequestTimeZone(request);
        TimeZone timeZone = null;
        if (StrUtil.isNotBlank(reqTimeZone)) {
            timeZone = TimeZone.getTimeZone(reqTimeZone);
        }
        if (timeZone == null) {
            TimeZoneDateTimeConfig timeZoneDateTimeConfig = GlobalApplicationContext.getBean(TimeZoneDateTimeConfig.class);
            timeZone = timeZoneDateTimeConfig != null ? TimeZone.getTimeZone(timeZoneDateTimeConfig.getDefaultTimeZone()) : null;
        }
        //未指定时区，默认使用中国所在的时区
        if (timeZone == null) {
            log.error("Error obtaining the request time zone information, use {} time Zone", TimeZoneDateTimeConstant.DEFAULT_TIME_ZONE);
            timeZone = TimeZone.getTimeZone(TimeZoneDateTimeConstant.DEFAULT_TIME_ZONE);
        }
        return timeZone;
    }

    /**
     * 获取当前请求时区信息
     *
     * @param request
     * @return
     */
    private String getRequestTimeZone(HttpServletRequest request) {
        String reqTimeZone = request.getHeader(TIMEZONE);
        if (StrUtil.isBlank(reqTimeZone)) {
            reqTimeZone = request.getHeader(TIMEZONE.toLowerCase());
        }
        return reqTimeZone;
    }

    /**
     * 设置本地化语言信息
     *
     * @param request
     * @return
     */
    private Locale setLocale(HttpServletRequest request) {
        String reqLanguage = getRequestLanguage(request);
        //默认使用中文
        if (StrUtil.isBlank(reqLanguage)) {
            return Locale.SIMPLIFIED_CHINESE;
        }
        //中文
        if (reqLanguage.contains(Locale.CHINESE.getLanguage())) {
            //区分繁体和简体
            //对于繁体, 前端传过来的头是 "zh-Hant"
            Locale hanLocale = new Locale("zh", "Hant");
            // 这里转为大写的原因是 new Locale("zh", "Hant"); 得到的 hanLocale.getCountry() 变成了 HANT, 因此需要转化为大写来进行字符串的匹配
            reqLanguage = reqLanguage.toUpperCase();
            if (reqLanguage.contains(hanLocale.getCountry())) {
                return Locale.TRADITIONAL_CHINESE;
            }
            return Locale.SIMPLIFIED_CHINESE;
        }
        //英文
        if (reqLanguage.contains(Locale.ENGLISH.getLanguage())) {
            return Locale.US;
        }
        //对于泰文, 前端传过来的头是 "th"
        //泰文
        Locale thaiLocale = new Locale("th", "TH");
        if (reqLanguage.contains(thaiLocale.getLanguage())) {
            return thaiLocale;
        }
        //增加语言只需要拓展此处及添加对应语言包就行，不用修改其他代码，因此未考虑用策略模式实现
        //...
        //默认使用中文
        return Locale.SIMPLIFIED_CHINESE;
    }

    /**
     * 获取当前请求语言信息
     *
     * @param request
     * @return
     */
    private String getRequestLanguage(HttpServletRequest request) {
        String reqLanguage = request.getHeader(LANGUAGE);
        if (StrUtil.isBlank(reqLanguage)) {
            reqLanguage = request.getHeader(LANGUAGE.toLowerCase());
        }
        return reqLanguage;
    }
}
