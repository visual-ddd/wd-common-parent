package com.wakedata.common.mq.service.consumer;

import com.wakedata.common.mq.model.consumer.Message;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.function.Consumer;

public class RocketMqConsumerThread implements MessageListenerConcurrently {

    private final Consumer<Message> msgConsumer;

    private final String groupId;

    public RocketMqConsumerThread(Consumer<Message> msgConsumer, String groupId) {
        this.msgConsumer = msgConsumer;
        this.groupId = groupId;
    }

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
        for (MessageExt messages : list) {
            msgConsumer.accept(Message.builder().value(new String(messages.getBody()))
                    .offset(messages.getQueueOffset()).queueId(messages.getQueueId())
                    .brokerName(messages.getBrokerName()).groupId(groupId)
                    .topic(messages.getTopic()).brokerName(messages.getBrokerName())
                    .build()
            );
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
