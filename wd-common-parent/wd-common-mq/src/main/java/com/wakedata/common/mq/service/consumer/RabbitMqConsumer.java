package com.wakedata.common.mq.service.consumer;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.wakedata.common.mq.model.consumer.Message;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class RabbitMqConsumer extends DefaultConsumer {

    private final Channel channel;

    private final Consumer<Message> msgConsumer;

    private final String groupId;

    public RabbitMqConsumer(Channel channel, Consumer<Message> msgConsumer, String groupId) {
        super(channel);
        this.channel = channel;
        this.msgConsumer = msgConsumer;
        this.groupId = groupId;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
        byte[] body) {
        msgConsumer.accept(Message.builder().value(new String(body, StandardCharsets.UTF_8))
            .groupId(groupId).topic(envelope.getExchange())
            .build());
        try {
            channel.basicAck(envelope.getDeliveryTag(), false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
