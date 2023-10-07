package com.wakedata.common.mq.annotation;

import com.wakedata.common.mq.core.MessageCenterAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用消息中心注解
 *
 * @author chenshaopeng
 * @date 2021/12/8
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(MessageCenterAutoConfiguration.class)
public @interface EnableMessageCenter {
}
