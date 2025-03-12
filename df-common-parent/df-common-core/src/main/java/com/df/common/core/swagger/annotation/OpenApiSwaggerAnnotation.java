package com.df.common.core.swagger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 为了swagger分组设置的注解
 * 开发平台需要能力图谱中的接口，加上这个注解就可以将接口中的某一个接口方法放在开放平台分组中
 *
 * @author focus
 * @date 2022/8/8 22:52
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenApiSwaggerAnnotation {
}
