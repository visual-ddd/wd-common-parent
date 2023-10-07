package com.wakedata.common.domainevent.config;

import com.wakedata.common.core.GlobalApplicationContext;
import com.wakedata.common.mq.enums.RequestProtocolEnum;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * 领域事件配置
 *
 * @author chenshaopeng
 * @date 2021/12/16
 */
@Data
public class DomainEventConfig {

    /**
     * 领域事件类型
     * <p>
     * 可选：kafka/rocket_mq/rabbit_mq
     */
    private RequestProtocolEnum domainEventProtocol;

    /**
     * 领域事件Topic
     */
    private String domainEventTopic;


    @Value("${domain-event.protocol:rocketmq}")
    public void setDomainEventProtocol(String domainEventProtocol) {
        this.domainEventProtocol = RequestProtocolEnum.covert(domainEventProtocol);
    }

    @Value("${domain-event.topic:}")
    public void setDomainEventTopic(String domainEventTopic) {
        this.domainEventTopic = domainEventTopic;
    }

}
