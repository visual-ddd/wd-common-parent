package com.wakedata.common.redis.delayed.core;

import com.wakedata.common.redis.delayed.annotation.DelayMsgSubscribe;
import com.wakedata.common.redis.delayed.RedisDelayQueueAutoConfiguration;
import com.wakedata.common.redis.delayed.model.SubscriberAnnotationApplyTarget;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean创建回调处理类
 *
 * @author hhf
 * @date 2022/04/11
 */
public class DelayMsgSubscribeAnnotationBeanPostProcessor implements MergedBeanDefinitionPostProcessor, Ordered {

    private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap<>(64));

    private final static List<SubscriberAnnotationApplyTarget> APPLY_TARGETS = new ArrayList<>(8);


    /**
     * spring bean创建回调方法
     * 找到Bean当中全部含有“@DelayMsgSubscribe”注解的方法
     */
    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) {
        if (bean instanceof AopInfrastructureBean || bean instanceof RedisDelayQueueAutoConfiguration) {
            return bean;
        }
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);

        if (this.nonAnnotatedClasses.contains(targetClass)) {
            return bean;
        }

        Class<DelayMsgSubscribe> annotatedClass = DelayMsgSubscribe.class;
        List<Method> annotatedMethods = findAnnotatedMethods(targetClass, annotatedClass.getName());

        if (annotatedMethods.isEmpty()) {
            nonAnnotatedClasses.add(targetClass);
            return bean;
        }
        annotatedMethods.forEach(m -> APPLY_TARGETS.add(SubscriberAnnotationApplyTarget.builder()
                .bean(bean).method(m).annotation(m.getAnnotation(annotatedClass)).build())
        );
        return bean;
    }

    public static boolean hasAnnotatedMethods(Method method, String annotationName) {
        return !method.isBridge() && method.getAnnotations().length > 0
                && AnnotatedElementUtils.isAnnotated(method, annotationName);
    }

    public static List<Method> findAnnotatedMethods(Class<?> targetClass, String annotationName) {
        List<Method> existAnnotatedMethods = new ArrayList<>();
        try {
            Method[] methods = targetClass.getDeclaredMethods();

            for (Method method : methods) {
                if (!hasAnnotatedMethods(method, annotationName)) {
                    continue;
                }
                existAnnotatedMethods.add(method);
            }
        } catch (Throwable ex) {
            throw new IllegalStateException("Failed to introspect annotated methods on " + targetClass.getName(), ex);
        }
        return existAnnotatedMethods;
    }

    @Override
    public void postProcessMergedBeanDefinition(@NonNull RootBeanDefinition beanDefinition
            , @NonNull Class<?> beanType, @NonNull String beanName) {
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    public static List<SubscriberAnnotationApplyTarget> getAnnotationApplyTargetList() {
        return APPLY_TARGETS;
    }
}
