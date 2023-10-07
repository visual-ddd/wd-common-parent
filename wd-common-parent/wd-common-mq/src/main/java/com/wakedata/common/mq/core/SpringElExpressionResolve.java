package com.wakedata.common.mq.core;

import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.lang.NonNull;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 解决spring EL表达式
 *
 *
 * @author chenshaopeng
 * @date 2021/12/9
 */
public class SpringElExpressionResolve {

    @Resource
    private ConfigurableListableBeanFactory beanFactory;

    /**
     * 解决表达式赋值
     *
     * @param value 带表达式的值
     * @return 处理后的值
     */
    public Object resolve(@NonNull String value) {
        if (!(value.startsWith("#{") && value.endsWith("}"))){
            return beanFactory.resolveEmbeddedValue(value);
        }
        return Objects.requireNonNull(beanFactory.getBeanExpressionResolver())
                .evaluate(value, new BeanExpressionContext(beanFactory, null));
    }

    public String resolveString(@NonNull Object value) {
        return String.valueOf(resolve(String.valueOf(value)));
    }

    public Integer resolveInteger(@NonNull Object value) {
        return Integer.valueOf(String.valueOf(resolve(String.valueOf(value))));
    }

    public Long resolveLong(@NonNull Object value) {
        return Long.valueOf(String.valueOf(resolve(String.valueOf(value))));
    }

    public Double resolveDouble(@NonNull Object value) {
        return Double.valueOf(String.valueOf(resolve(String.valueOf(value))));
    }


}
