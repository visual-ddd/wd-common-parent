package com.wakedata.common.redis.jedis.redis;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

/**
 * Created by pengxu on 2016/9/8.
 */
@Deprecated
public class BaseApplicationContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    /**
     * 获取applicationContext对象
     * @return applicationContext对象
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 根据bean的id来查找对象
     * @param id beanId
     * @return 根据id查找到的对象
     */
    public static <T> T getBeanById(String id) {
        return (T) applicationContext.getBean(id);
    }

    /**
     * 根据bean的id来查找对象
     * @param id beanId
     * @param clazz 类型
     * @return 根据id查找到的对象
     */
    public static <T> T getBeanById(String id, Class<T> clazz) {

            return applicationContext.getBean(id, clazz);
    }

    /**
     * 根据bean的class来查找对象
     * @param c class
     * @return bean的class查找到的对象
     */
    public static <T> T getBeanByClass(Class<T> c) {
            return applicationContext.getBean(c);
    }

    /**
     * 根据bean的class来查找所有的对象(包括子类)
     * @param c
     * @return
     */
    public static Map getBeansByClass(Class c) {
        return applicationContext.getBeansOfType(c);
    }

    public static String getMessage(String key) {
        return applicationContext.getMessage(key, null, Locale.getDefault());
    }
}
