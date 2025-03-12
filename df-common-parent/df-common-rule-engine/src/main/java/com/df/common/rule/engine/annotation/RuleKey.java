package com.df.common.rule.engine.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author focus
 */
@Target({TYPE, FIELD, METHOD})
@Retention(RUNTIME)
public @interface RuleKey {

      String value() default "";
}
