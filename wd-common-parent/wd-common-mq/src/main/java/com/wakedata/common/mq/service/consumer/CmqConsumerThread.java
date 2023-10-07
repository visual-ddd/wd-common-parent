package com.wakedata.common.mq.service.consumer;

import com.qcloud.cmq.client.consumer.MessageListener;
import com.wakedata.common.mq.model.consumer.Message;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class CmqConsumerThread implements MessageListener {

    private final Consumer<Message> msgConsumer;


    public CmqConsumerThread(Consumer<Message> msgConsumer) {
        this.msgConsumer = msgConsumer;
    }

    @Override
    public List<Long> consumeMessage(String queue, List<com.qcloud.cmq.client.consumer.Message> msgList) {
        List<Long> ackList = new ArrayList<>();
        for (com.qcloud.cmq.client.consumer.Message msg : msgList) {
            msgConsumer.accept(Message.builder().value(msg.getData())
                    .offset(msg.getMessageId()).queue(queue)
                    .build()
            );
        }
        return ackList;
    }
}
