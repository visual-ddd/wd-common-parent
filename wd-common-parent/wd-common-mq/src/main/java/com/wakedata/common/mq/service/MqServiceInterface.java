package com.wakedata.common.mq.service;

import com.wakedata.common.mq.service.consumer.AbstractConsumerService;
import com.wakedata.common.mq.service.producer.AbstractProducerService;

/**
 * Mq服务接口 - 含消费者、生产者服务
 *
 * @author chenshaopeng
 * @date 2021/12/15
 */
public interface MqServiceInterface {

    /**
     * 获取消费者服务
     *
     * @return 消费者实例
     */
    AbstractConsumerService consumer();

    /**
     * 获取生产者服务
     *
     * @return 生产者实例
     */
    AbstractProducerService producer();

}
