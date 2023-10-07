package com.wakedata.common.mq.annotation;


import com.wakedata.common.mq.enums.RequestProtocolEnum;

import java.lang.annotation.*;

/**
 * 消息订阅注解
 *
 * @author chenshaopeng
 * @date 2021/12/7
 */

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageSubscribe {

    /**
     * 私有配置名称
     *
     * (配置中定义的“private.mq.configs.item.name”)
     */
    String config() default "";

    /**
     * 主题
     *
     * (当公共配置与私有配置未指定时不能为空)
     */
    String topic() default "";

    /**
     * 队列Id
     *
     * (当公共配置与私有配置未指定时不能为空)
     */
    String queueId() default "";

    /**
     * 服务地址
     *
     * (当公共配置与私有配置未指定时不能为空)
     */
    String bootstrapServers() default "";

    /**
     * 标签
     */
    String tag() default "*";

    /**
     * 协议
     */
    RequestProtocolEnum protocol() default RequestProtocolEnum.ROCKET_MQ;

}
