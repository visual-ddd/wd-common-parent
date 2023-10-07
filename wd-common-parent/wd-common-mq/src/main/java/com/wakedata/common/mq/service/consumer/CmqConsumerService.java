package com.wakedata.common.mq.service.consumer;

import com.qcloud.cmq.client.common.ClientConfig;
import com.qcloud.cmq.client.exception.MQClientException;
import com.qcloud.cmq.client.exception.MQServerException;
import com.wakedata.common.mq.exception.AlreadyExistConsumerException;
import com.wakedata.common.mq.exception.ConnectException;
import com.wakedata.common.mq.model.consumer.MakeSubscribeBody;
import com.wakedata.common.mq.model.consumer.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Cmq消费者服务
 *
 * @author chenshaopeng
 * @date 2021/12/11
 */
public class CmqConsumerService extends AbstractConsumerService{

    private final static Map<String, com.qcloud.cmq.client.consumer.Consumer>
            CMQ_CONSUMER_THREAD_MAP = new HashMap<>();


    @Override
    public void addSubscribe(MakeSubscribeBody info, Consumer<Message> msgConsumer) {
        String threadKey = generateConsumerKey(
                info.getQueueId(), info.getBootstrapServers(), info.getTopic(), info.getTag());

        if(CMQ_CONSUMER_THREAD_MAP.containsKey(threadKey)){
            throw new AlreadyExistConsumerException();
        }

        com.qcloud.cmq.client.consumer.Consumer consumer;
        try {
            consumer = createConsumer(info);
            consumer.start();
            consumer.subscribe(info.getQueueId(), new CmqConsumerThread(msgConsumer));
        } catch (MQClientException e) {
            throw new ConnectException(e);
        } catch (MQServerException e) {
            throw new ConnectException(e.getMessage());
        }

        CMQ_CONSUMER_THREAD_MAP.put(threadKey, consumer);
    }

    private com.qcloud.cmq.client.consumer.Consumer createConsumer(MakeSubscribeBody info){
        com.qcloud.cmq.client.consumer.Consumer consumer = new com.qcloud.cmq.client.consumer.Consumer();
        consumer.setNameServerAddress(info.getBootstrapServers());
        consumer.setSecretId(info.getSecretId());
        consumer.setSecretKey(info.getSecretKey());
        consumer.setSignMethod(ClientConfig.SIGN_METHOD_SHA256);
        if(Objects.nonNull(info.getPollingWaitSeconds())){
            consumer.setPollingWaitSeconds(info.getPollingWaitSeconds());
        }
        if(Objects.nonNull(info.getRequestTimeout())){
            consumer.setRequestTimeoutMS(getRequestTimeout(info.getRequestTimeout()));
        }
        if(Objects.nonNull(info.getBatchPullNumber())){
            // 单次拉取最大消息数
            consumer.setBatchPullNumber(info.getBatchPullNumber());
        }
        return consumer;
    }

    @Override
    public Set<String> subscribeList() {
        return CMQ_CONSUMER_THREAD_MAP.keySet();
    }

    @Override
    public void deleteSubscribe(String consumerKey) {
        com.qcloud.cmq.client.consumer.Consumer consumer = CMQ_CONSUMER_THREAD_MAP.get(consumerKey);
        if(consumer == null){
            return;
        }
        consumer.shutdown();
        CMQ_CONSUMER_THREAD_MAP.remove(consumerKey);
    }
}
