package com.wakedata.common.mq.service.consumer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.wakedata.common.mq.exception.AlreadyExistConsumerException;
import com.wakedata.common.mq.model.consumer.MakeSubscribeBody;
import com.wakedata.common.mq.model.consumer.Message;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;

/**
 * 华为云RabbitMq 消费者服务
 *
 * @author ZhuXueLiang
 * @version 1.0
 * @date 2022/11/11 10:58
 */
@Slf4j
public class RabbitMqConsumerService extends AbstractConsumerService {

    private final static Map<String, RabbitMqConsumer> RABBIT_MQ_CONSUMER_THREAD_MAP = new HashMap<>();


    @Override
    public void addSubscribe(MakeSubscribeBody info, Consumer<Message> msgConsumer) {
        String consumerKey = generateConsumerKey(
            info.getQueueId(), info.getBootstrapServers(), info.getTopic(), info.getTag());
        if (RABBIT_MQ_CONSUMER_THREAD_MAP.get(consumerKey) != null) {
            throw new AlreadyExistConsumerException();
        }
        RABBIT_MQ_CONSUMER_THREAD_MAP.put(consumerKey, createRabbitMqConsumer(info, msgConsumer));
    }

    @Override
    public synchronized void deleteSubscribe(String consumerKey) {
        RabbitMqConsumer consumer = RABBIT_MQ_CONSUMER_THREAD_MAP.get(consumerKey);
        if (consumer == null) {
            return;
        }
        Channel channel = consumer.getChannel();
        try {
            channel.close();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        RABBIT_MQ_CONSUMER_THREAD_MAP.remove(consumerKey);
    }

    @Override
    public Set<String> subscribeList() {
        return RABBIT_MQ_CONSUMER_THREAD_MAP.keySet();
    }

    /**
     * 创建rabbitMq消费者，并创建信道订阅
     *
     * @param info        info
     * @param msgConsumer 信息消费者回调
     * @return 消费者
     */
    private static RabbitMqConsumer createRabbitMqConsumer(MakeSubscribeBody info,
        Consumer<Message> msgConsumer) {
        RabbitMqConsumer rabbitMqConsumer;
        Channel channel = createRabbitChannel(info);
        rabbitMqConsumer = new RabbitMqConsumer(channel, msgConsumer, info.getQueueId());
        try {
            // 添加订阅
            channel.basicConsume(info.getQueueId(), false, rabbitMqConsumer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return rabbitMqConsumer;
    }

    /**
     * 获取 rabbitMq TCP连接
     *
     * @param info info
     * @return TCP连接
     */
    private static Connection getConnection(MakeSubscribeBody info) {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost(info.getBootstrapServers());
        factory.setUsername(info.getSecretId());
        factory.setPassword(info.getSecretKey());

        try {
            return factory.newConnection();
        } catch (Exception e) {
            throw new RuntimeException("rabbitMq连接异常", e);
        }
    }

    /**
     * 创建队列通道
     *
     * @return 队列管道
     */
    public static Channel createRabbitChannel(MakeSubscribeBody info) {
        Connection connection = getConnection(info);
        Channel channel = null;
        try {
            channel = connection.createChannel();
            if (Boolean.TRUE.equals(info.getAutoCreateTopic())) {
                channel.exchangeDeclare(info.getTopic(), BuiltinExchangeType.DIRECT);
                channel.basicQos(1);
            }
            channel.queueDeclare(info.getQueueId(), true, false, false, null);
            channel.queueBind(info.getQueueId(), info.getTopic(), info.getTag());
        } catch (Exception e) {
            log.error("创建rabbitMq通道异常", e);
        }
        return channel;
    }

}
