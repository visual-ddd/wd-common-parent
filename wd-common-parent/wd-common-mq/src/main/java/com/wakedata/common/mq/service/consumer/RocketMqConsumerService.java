package com.wakedata.common.mq.service.consumer;

import com.wakedata.common.mq.exception.ConnectException;
import com.wakedata.common.mq.model.consumer.QueueRecord;
import com.wakedata.common.mq.model.consumer.MakeSubscribeBody;
import com.wakedata.common.mq.model.consumer.Message;
import com.wakedata.common.mq.exception.AlreadyExistConsumerException;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.client.consumer.store.OffsetStore;
import org.apache.rocketmq.client.consumer.store.RemoteBrokerOffsetStore;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.impl.MQClientManager;
import org.apache.rocketmq.client.impl.factory.MQClientInstance;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.RPCHook;

import java.util.*;
import java.util.function.Consumer;


/**
 * RocketMQ消费者服务
 *
 * @author chenshaopeng
 * @date 2021/12/11
 */
public class RocketMqConsumerService extends AbstractConsumerService {

    private final static Map<String, DefaultMQPushConsumer> ROCKETMQ_CONSUMER_THREAD_MAP = new HashMap<>();


    @Override
    public synchronized void addSubscribe(MakeSubscribeBody info, Consumer<Message> msgConsumer){
        String threadKey = generateConsumerKey(
                info.getQueueId(), info.getBootstrapServers(), info.getTopic(), info.getTag());

        if(ROCKETMQ_CONSUMER_THREAD_MAP.containsKey(threadKey)){
            throw new AlreadyExistConsumerException();
        }

        DefaultMQPushConsumer consumer;
        try {
            consumer = createDefaultMqPushConsumer(info);
            consumer.registerMessageListener(new RocketMqConsumerThread(msgConsumer, info.getQueueId()));
            consumer.start();
            this.resetLastOffset(consumer, info);
        } catch (MQClientException e) {
            throw new ConnectException(e);
        } catch (IllegalStateException e) {
            throw new ConnectException(e.getMessage());
        }
        ROCKETMQ_CONSUMER_THREAD_MAP.put(threadKey, consumer);
    }

    @Override
    public Set<String> subscribeList(){
        return ROCKETMQ_CONSUMER_THREAD_MAP.keySet();
    }

    @Override
    public synchronized void deleteSubscribe(String consumerKey){
        DefaultMQPushConsumer consumerThread = ROCKETMQ_CONSUMER_THREAD_MAP.get(consumerKey);
        if (consumerThread == null) {
            return;
        }
        consumerThread.shutdown();
        ROCKETMQ_CONSUMER_THREAD_MAP.remove(consumerKey);
    }

    private DefaultMQPushConsumer createDefaultMqPushConsumer(MakeSubscribeBody info) throws MQClientException {
        DefaultMQPushConsumer consumer;
        String rocketMqAccessKey = info.getRocketMqAccessKey();
        String rocketMqSecretKey = info.getRocketMqSecretKey();
        if (StringUtils.isNotBlank(rocketMqAccessKey) || StringUtils.isNotBlank(rocketMqSecretKey)) {
            consumer = new DefaultMQPushConsumer(info.getQueueId(), getAclRpcHook(rocketMqAccessKey, rocketMqSecretKey), new AllocateMessageQueueAveragely());
        } else {
            consumer = new DefaultMQPushConsumer(info.getQueueId());
        }
        consumer.setNamesrvAddr(info.getBootstrapServers());
        consumer.setInstanceName(consumer.getConsumerGroup());
        consumer.subscribe(info.getTopic(), Objects.nonNull(info.getTag()) ? info.getTag() : "*");
        consumer.setMessageModel(MessageModel.CLUSTERING);
        // 一个新的订阅组第一次启动从队列的最前位置开始消费，后续再启动接着上次消费的进度开始消费
        // consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        if(Objects.nonNull(info.getBatchPullNumber())){
            // 单次拉取最大消息数
            consumer.setPullBatchSize(info.getBatchPullNumber());
        }
        return consumer;
    }

    /**
     * 重置offset, 如果本地有offset记录, 则重置为上次保存的offset; 否则从0开始拉取消息
     */
    public void resetLastOffset(DefaultMQPushConsumer consumer, MakeSubscribeBody info) throws MQClientException {
        if(Objects.isNull(info.getQueueRecordList()) || info.getQueueRecordList().isEmpty()){
            return;
        }
        try{
            MQClientInstance clientInstance = MQClientManager.getInstance().getOrCreateMQClientInstance(consumer);
            OffsetStore offsetStore = new RemoteBrokerOffsetStore(clientInstance, consumer.getConsumerGroup());
            for (MessageQueue queue : consumer.fetchSubscribeMessageQueues(info.getTopic())) {
                for(QueueRecord queueRecord : info.getQueueRecordList()){
                    if(queue.getQueueId() != queueRecord.getQueueId()){
                        continue;
                    }
                    offsetStore.updateConsumeOffsetToBroker(queue, queueRecord.getOffset() + 1, true);
                }
            }
        } catch (Exception e){
            throw new MQClientException("resetLastOffset error!", e);
        }
    }

    private RPCHook getAclRpcHook(String accessKey, String secretKey) {
        return new AclClientRPCHook(new SessionCredentials(accessKey, secretKey));
    }

}