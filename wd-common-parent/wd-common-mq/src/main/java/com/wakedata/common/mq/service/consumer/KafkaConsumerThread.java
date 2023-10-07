package com.wakedata.common.mq.service.consumer;

import com.wakedata.common.mq.model.consumer.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import java.time.Duration;
import java.util.function.Consumer;

public class KafkaConsumerThread extends Thread {

    private final KafkaConsumer<String, String> kafkaConsumer;

    private final Consumer<Message> msgConsumer;

    private final String groupId;

    KafkaConsumerThread(KafkaConsumer<String, String> kafkaConsumer, Consumer<Message> msgConsumer, String groupId){
        this.kafkaConsumer = kafkaConsumer;
        this.msgConsumer = msgConsumer;
        this.groupId = groupId;
    }

    public KafkaConsumer<String, String> getKafkaConsumer() {
        return kafkaConsumer;
    }

    @Override
    public void run() {
        try {
            while (true){
                if (isInterrupted()) {
                    return;
                }
                pullConsumerRecords();
            }
        } catch (WakeupException ignored) {
        } finally {
            kafkaConsumer.close();
            super.interrupt();
        }
    }

    /**
     * 拉取topic消息
     */
    private void pullConsumerRecords(){
        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(1000));
        for (ConsumerRecord<String, String> record : records) {
            msgConsumer.accept(Message.builder().value(record.value())
                    .offset(record.offset()).partition(record.partition())
                    .groupId(groupId).topic(record.topic())
                    .build()
            );
        }
        kafkaConsumer.commitSync();
    }

}