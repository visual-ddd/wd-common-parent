package com.wakedata.common.domainevent.core;

import com.wakedata.common.domainevent.annotation.DomainEventSubscribe;
import com.wakedata.common.domainevent.model.DomainAnnotationApplyTarget;
import com.wakedata.common.mq.core.MessageCenterAnnotationBeanPostProcessor;
import com.wakedata.common.mq.core.MessageCenterAutoConfiguration;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
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
 * @author chenshaopeng
 * @date 2021/12/16
 */
@Import(CreationDomainEventConsumer.class)
public class DomainEventAnnotationBeanPostProcessor implements MergedBeanDefinitionPostProcessor, Ordered{

    private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap<>(64));

    private final static List<DomainAnnotationApplyTarget> APPLY_TARGETS = new ArrayList<>(8);


    /**
     * spring bean创建回调方法
     * 找到Bean当中全部含有“@DomainEventSubscribe”注解的方法
     */
    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) {
        if (bean instanceof AopInfrastructureBean || bean instanceof MessageCenterAutoConfiguration) {
            return bean;
        }
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);

        if(this.nonAnnotatedClasses.contains(targetClass)){
            return bean;
        }
        Class<DomainEventSubscribe> annotatedClass = DomainEventSubscribe.class;
        List<Method> annotatedMethods = MessageCenterAnnotationBeanPostProcessor
                .findAnnotatedMethods(targetClass, annotatedClass.getName());

        if(annotatedMethods.isEmpty()){
            nonAnnotatedClasses.add(targetClass);
            return bean;
        }
        annotatedMethods.forEach(m -> APPLY_TARGETS.add(DomainAnnotationApplyTarget.builder()
                .bean(bean).method(m).annotation(m.getAnnotation(annotatedClass)). build())
        );
        return bean;
    }


    @Override
    public void postProcessMergedBeanDefinition(@NonNull RootBeanDefinition beanDefinition
            , @NonNull Class<?> beanType, @NonNull String beanName) {
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    public static List<DomainAnnotationApplyTarget> getAnnotationApplyTargetList() {
        return APPLY_TARGETS;
    }
}
