package com.wakedata.common.core.util;

import cn.hutool.core.bean.copier.CopyOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BeanCopy相关操作
 *
 * @author hhf
 * @date 2021/1/28
 */
@Slf4j
public class BeanUtil {

    /**
     * 复制bean的属性
     *
     * @param source 源 要复制的对象
     * @param target 目标 复制到此对象
     */
    public static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }

    /**
     * 复制对象
     *
     * @param source 源 要复制的对象
     * @param target 目标 复制到此对象
     * @param <T>
     * @return
     */
    public static <T> T copy(Object source, Class<T> target) {
        if (source == null || target == null) {
            return null;
        }
        try {
            //1,8之后需要先获取对象的构造函数再调用实例
            T newInstance = target.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, newInstance);
            //加入localDateTime转date
            localDateTimeToDate(source,newInstance,"createTime");
            localDateTimeToDate(source,newInstance,"updateTime");
            return newInstance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加入localDateTime转Date
     * @param source
     * @param newInstance
     * @param propertyName
     */
    private static <T> void localDateTimeToDate(Object source, T newInstance, String propertyName){
        try {
            PropertyDescriptor sourceTime = BeanUtils.getPropertyDescriptor(source.getClass(), propertyName );
            if(sourceTime != null && sourceTime.getPropertyType() == LocalDateTime.class){
                LocalDateTime sourceTimeVal = (LocalDateTime) sourceTime.getReadMethod().invoke(source);
                if(sourceTimeVal != null) {
                    PropertyDescriptor targetTime = BeanUtils.getPropertyDescriptor(newInstance.getClass(), propertyName);
                    if (targetTime != null && targetTime.getPropertyType() == Date.class) {
                        targetTime.getWriteMethod().invoke(newInstance,LocalDateTimeUtil.toDate(sourceTimeVal));
                    }
                }
            }

        }catch (Exception e){
            log.error("BeanUtil->localDateTimeToDate方法转换出错:propertyName = {}，err = {}",propertyName,e);
        }
    }


    /**
     * 复制list
     *
     * @param source
     * @param target
     * @param <T>
     * @param <K>
     * @return
     */
    public static <T, K> List<K> copyList(List<T> source, Class<K> target) {

        if (null == source || source.isEmpty()) {
            return Collections.emptyList();
        }
        return source.stream().map(e -> copy(e, target)).collect(Collectors.toList());
    }


    /**
     * 将source的非空属性合并到target
     * 深度拷贝存在问题
     *
     * @param source    来源对象
     * @param target    被合并对象
     * @param beanClass 需要合并称的对象类
     * @param <T>
     * @return 合并后的对象
     */
    public static <T> T merge(Object source, Object target, Class<T> beanClass, String... ignoreProperties) {
        T obj = cn.hutool.core.bean.BeanUtil.copyProperties(target, beanClass);
        cn.hutool.core.bean.BeanUtil.copyProperties(source, obj, CopyOptions.create().ignoreNullValue().setIgnoreProperties(ignoreProperties));

        return obj;
    }

}
