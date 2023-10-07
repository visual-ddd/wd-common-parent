package com.wakedata.common.mq.service.consumer;


import com.wakedata.common.mq.model.consumer.Message;
import com.wakedata.common.mq.model.consumer.MakeSubscribeBody;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 消息消费者抽象类
 *
 * @author chenshaopeng
 * @date 2021/10/15
 */
public abstract class AbstractConsumerService {

    private static final String CONSUMER_KEY_TEMPLATE = "consumer_thread_%s_%s_%s_%s";

    private static final Integer DEFAULT_REQUEST_TIMEOUT = 3000;


    public static String generateConsumerKey(String queueId, String bootstrapServers, String topic, String tag){
        return String.format(CONSUMER_KEY_TEMPLATE, queueId, bootstrapServers, topic, tag);
    }

    protected Integer getRequestTimeout(Integer requestTimeout){
        return Optional.ofNullable(requestTimeout).orElse(DEFAULT_REQUEST_TIMEOUT);
    }

    /**
     * 新增订阅
     */
    public abstract void addSubscribe(MakeSubscribeBody info, Consumer<Message> msgConsumer);

    /**
     * 删除订阅
     */
    public abstract void deleteSubscribe(String consumerKey);

    /**
     * 订阅列表
     */
    public abstract Set<String> subscribeList();
}
