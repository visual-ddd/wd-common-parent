package com.wakedata.common.mq.service.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.wakedata.common.mq.exception.ConnectException;
import com.wakedata.common.mq.exception.SendMessageException;
import com.wakedata.common.mq.model.producer.MakeMessageBody;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * 华为云RabbitMq 生产者服务
 *
 * @author ZhuXueLiang
 * @version 1.0
 * @date 2022/11/11 11:11
 */
@Slf4j
public class RabbitMqProducerService extends AbstractProducerService {

    private final Map<String, Channel> RABBIT_MQ_PRODUCER_THREAD_MAP = new HashMap<>();


    @Override
    public Boolean sendMessage(MakeMessageBody info) {
        Channel channel = getRabbitMqConnection(info);
        try {
            // 发送信息
            channel.basicPublish(info.getTopic(), info.getTag(), null, info.getMessage().getBytes());
            return true;
        } catch (Exception e) {
            throw new SendMessageException(e);
        }
    }

    private Channel getRabbitMqConnection(MakeMessageBody info) {
        Channel channel;

        String threadKey = String.format(PRODUCER_KEY_TEMPLATE
            , info.getQueueId(), info.getBootstrapServers(), info.getTopic());
        if(RABBIT_MQ_PRODUCER_THREAD_MAP.containsKey(threadKey)){
            channel = RABBIT_MQ_PRODUCER_THREAD_MAP.get(threadKey);
            return channel;
        }
        channel = getRabbitChannel(info);

        RABBIT_MQ_PRODUCER_THREAD_MAP.put(threadKey, channel);
        return channel;
    }

    /**
     * 创建 RabbitMq TCP 连接
     * @param info 参数信息
     * @return TCP 连接
     */
    private static Connection getConnection(MakeMessageBody info) {
        ConnectionFactory factory = new ConnectionFactory();

        //公网接入点
        factory.setHost(info.getBootstrapServers());
        //静态用户名
        factory.setUsername(info.getSecretId());
        //静态密码
        factory.setPassword(info.getSecretKey());

        try {
            return factory.newConnection();
        } catch (Exception e) {
            throw new ConnectException("rabbitmq 创建连接异常！");
        }
    }

    /**
     * 创建队列通道
     *
     * @return 队列管道
     */
    public static Channel getRabbitChannel(MakeMessageBody info) {
        Connection connection = getConnection(info);
        Channel channel;
        try {
            channel = connection.createChannel();
            if (Boolean.TRUE.equals(info.getAutoCreateTopic())) {
                channel.exchangeDeclare(info.getTopic(), BuiltinExchangeType.DIRECT);
                channel.basicQos(1);
            }
            channel.exchangeDeclarePassive(info.getTopic());
        } catch (Exception e) {
            throw new ConnectException("创建rabbitMq通道异常", e);
        }
        return channel;
    }

}
