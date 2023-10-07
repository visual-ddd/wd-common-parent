package com.wakedata.common.domainevent.retry;

import com.wakedata.common.domainevent.core.DomainEventAutoConfiguration;
import com.wakedata.common.domainevent.retry.dao.RetryEventDAO;
import com.wakedata.common.domainevent.retry.event.RetryEventListener;
import com.wakedata.common.domainevent.retry.event.RetryEventPublisher;
import com.wakedata.common.domainevent.retry.task.RetryEventTask;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * SpringBoot自动装配
 *
 * @author hhf
 * @date 2022/6/14
 */
@Configuration
@ConditionalOnProperty(prefix = "domain.event.retry", name = "enable", havingValue = "true", matchIfMissing = false)
@AutoConfigureAfter(DomainEventAutoConfiguration.class)
@Import({RetryEventDAO.class, RetryEventPublisher.class, RetryEventListener.class, RetryEventTask.class})
public class RetryErrorEventAutoConfiguration {

}