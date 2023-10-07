package com.wakedata.common.mq.service.producer;

import cn.hutool.core.collection.ListUtil;
import com.qcloud.cmq.client.common.ClientConfig;
import com.qcloud.cmq.client.common.ResponseCode;
import com.qcloud.cmq.client.exception.MQClientException;
import com.qcloud.cmq.client.producer.Producer;
import com.qcloud.cmq.client.producer.PublishResult;
import com.wakedata.common.mq.exception.ConnectException;
import com.wakedata.common.mq.exception.SendMessageException;
import com.wakedata.common.mq.model.producer.MakeMessageBody;

import java.util.*;

/**
 * Cmq生产者服务
 *
 * @author chenshaopeng
 * @date 2021/12/14
 */
public class CmqProducerService extends AbstractProducerService {

    private final Map<String, Producer> CMQ_PRODUCER_THREAD_MAP = new HashMap<>();


    private Producer getProducer(MakeMessageBody info) throws MQClientException {
        Producer producer;

        String threadKey = String.format(PRODUCER_KEY_TEMPLATE
                , info.getQueueId(), info.getBootstrapServers(), info.getTopic());
        if(CMQ_PRODUCER_THREAD_MAP.containsKey(threadKey)){
            return CMQ_PRODUCER_THREAD_MAP.get(threadKey);
        }
        producer = this.createProducer(info);
        producer.start();

        CMQ_PRODUCER_THREAD_MAP.put(threadKey, producer);
        return producer;
    }

    @Override
    public Boolean sendMessage(MakeMessageBody info) {
        try {
            PublishResult result = getProducer(info).publish(info.getTopic(), info.getMessage()
                    , ListUtil.toList(info.getTag()));
            return ResponseCode.SUCCESS == result.getReturnCode();
        } catch (MQClientException e) {
            throw new ConnectException(e);
        } catch (Exception e) {
            throw new SendMessageException(e);
        }
    }

    private com.qcloud.cmq.client.producer.Producer createProducer(MakeMessageBody info){
        com.qcloud.cmq.client.producer.Producer producer = new com.qcloud.cmq.client.producer.Producer();
        producer.setNameServerAddress(info.getBootstrapServers());
        producer.setSecretId(info.getSecretId());
        producer.setSecretKey(info.getSecretKey());
        producer.setSignMethod(ClientConfig.SIGN_METHOD_SHA256);
        producer.setRequestTimeoutMS(getRequestTimeout(info.getRequestTimeout()));
        return producer;
    }

}
