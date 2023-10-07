package com.wakedata.common.mq.service.producer;

import cn.hutool.system.SystemUtil;
import com.wakedata.common.core.GlobalApplicationContext;
import com.wakedata.common.mq.exception.ConnectException;
import com.wakedata.common.mq.exception.SendMessageException;
import com.wakedata.common.mq.model.producer.MakeMessageBody;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.RPCHook;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * RocketMQ生产者服务
 *
 * @author chenshaopeng
 * @date 2021/11/10
 */
public class RocketMqProducerService extends AbstractProducerService {

    private final Map<String, DefaultMQProducer> ROCKETMQ_PRODUCER_THREAD_MAP = new HashMap<>();


    private DefaultMQProducer getProducer(MakeMessageBody info) throws MQClientException {
        DefaultMQProducer producer;

        String threadKey = String.format(PRODUCER_KEY_TEMPLATE
                , info.getQueueId(), info.getBootstrapServers(), info.getTopic());
        if (ROCKETMQ_PRODUCER_THREAD_MAP.containsKey(threadKey)) {
            return ROCKETMQ_PRODUCER_THREAD_MAP.get(threadKey);
        }
        synchronized (info.getQueueId()) {
            if (ROCKETMQ_PRODUCER_THREAD_MAP.containsKey(threadKey)) {
                return ROCKETMQ_PRODUCER_THREAD_MAP.get(threadKey);
            }
            producer = this.createDefaultMQProducerProp(info);
            producer.start();

            ROCKETMQ_PRODUCER_THREAD_MAP.put(threadKey, producer);
            return producer;
        }
    }

    private Message getMessage(MakeMessageBody info) throws UnsupportedEncodingException {
        byte[] message = info.getMessage().getBytes(RemotingHelper.DEFAULT_CHARSET);
        return new Message(info.getTopic(), info.getTag(), message);
    }

    @Override
    public Boolean sendMessage(MakeMessageBody info) {
        try {
            DefaultMQProducer producer = getProducer(info);
            SendResult result = producer.send(getMessage(info));
            return result.getSendStatus().equals(SendStatus.SEND_OK);
        } catch (MQClientException e) {
            throw new ConnectException(e);
        } catch (Exception e) {
            throw new SendMessageException(e);
        }
    }

    private DefaultMQProducer createDefaultMQProducerProp(MakeMessageBody info) {
        DefaultMQProducer producer;
        String producerGroup = info.getQueueId() + "_" + info.getTopic();
        String rocketMqAccessKey = info.getRocketMqAccessKey();
        String rocketMqSecretKey = info.getRocketMqSecretKey();
        if (StringUtils.isNotBlank(rocketMqAccessKey) || StringUtils.isNotBlank(rocketMqSecretKey)) {
            producer = new DefaultMQProducer(producerGroup, getAclRpcHook(rocketMqAccessKey, rocketMqSecretKey));
        } else {
            producer = new DefaultMQProducer(producerGroup);
        }
        producer.setNamesrvAddr(info.getBootstrapServers());
        // 实例定义：应用名称 + ip地址 + 自定义group或queue
        producer.setInstanceName(GlobalApplicationContext.getApplicationContext().getId()
                + "_" + SystemUtil.getHostInfo().getAddress() + "_" + producer.getProducerGroup());
        // 为避免程序启动的时候报错，添加此代码，可以让rocketMQ自动创建topic
        producer.setCreateTopicKey("AUTO_CREATE_TOPIC_KEY");
        producer.setSendMsgTimeout(getRequestTimeout(info.getRequestTimeout()));
        return producer;
    }

    private RPCHook getAclRpcHook(String accessKey, String secretKey) {
        return new AclClientRPCHook(new SessionCredentials(accessKey, secretKey));
    }

}
