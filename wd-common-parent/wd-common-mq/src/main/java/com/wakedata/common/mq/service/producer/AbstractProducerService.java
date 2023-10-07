package com.wakedata.common.mq.service.producer;


import com.wakedata.common.mq.model.producer.MakeMessageBody;

import java.util.Optional;

/**
 * 消息生产者抽象类
 *
 * @author chenshaopeng
 * @date 2021/11/10
 */
public abstract class AbstractProducerService {

    protected static final String PRODUCER_KEY_TEMPLATE = "producer_thread_%s_%s_%s";

    protected static final Integer DEFAULT_SEND_MSG_TIMEOUT = 3000;


    protected Integer getRequestTimeout(Integer requestTimeout){
        return Optional.ofNullable(requestTimeout).orElse(DEFAULT_SEND_MSG_TIMEOUT);
    }

    /**
     * 发送消息
     */
    public abstract Boolean sendMessage(MakeMessageBody info);

}
