package com.wakedata.common.redis.delayed;

import com.wakedata.common.redis.delayed.core.DelayMessageConsumer;
import com.wakedata.common.redis.delayed.core.DelayMsgSubscribeAnnotationBeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * SpringBoot自动装配
 *
 * @author hhf
 * @date 2022-04-11
 */
@Configuration
@ConditionalOnProperty(prefix = "common.redis.delay-queue", name = "enable", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@Import({DelayMsgSubscribeAnnotationBeanPostProcessor.class, RedisDelayQueueUtil.class, DelayMessageConsumer.class})
public class RedisDelayQueueAutoConfiguration {
}
