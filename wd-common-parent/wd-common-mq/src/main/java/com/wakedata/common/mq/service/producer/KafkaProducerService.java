package com.wakedata.common.mq.service.producer;

import com.wakedata.common.mq.exception.ConnectException;
import com.wakedata.common.mq.exception.SendMessageException;
import com.wakedata.common.mq.model.producer.MakeMessageBody;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Kafka生产者服务
 *
 * @author chenshaopeng
 * @date 2021/11/10
 */
public class KafkaProducerService extends AbstractProducerService {

    private final Map<String, KafkaProducer<String, String>> KAFKA_PRODUCER_THREAD_MAP = new HashMap<>();


    private KafkaProducer<String, String> getProducer(MakeMessageBody info) {
        KafkaProducer<String, String> producer;

        String threadKey = String.format(PRODUCER_KEY_TEMPLATE
                , info.getQueueId(), info.getBootstrapServers(), info.getTopic());
        if(KAFKA_PRODUCER_THREAD_MAP.containsKey(threadKey)){
            producer = KAFKA_PRODUCER_THREAD_MAP.get(threadKey);
            return producer;
        }
        producer = this.createKafkaProducer(info);

        KAFKA_PRODUCER_THREAD_MAP.put(threadKey, producer);
        return producer;
    }

    public ProducerRecord<String, String> getMessage(MakeMessageBody info) {
        return new ProducerRecord<>(info.getTopic(), info.getMessage());
    }


    @Override
    public Boolean sendMessage(MakeMessageBody info) {
        try {
            return null != getProducer(info)
                    .send(getMessage(info)).get(DEFAULT_SEND_MSG_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            throw new ConnectException(e);
        } catch (Exception e) {
            throw new SendMessageException(e);
        }
    }


    private KafkaProducer<String, String> createKafkaProducer(MakeMessageBody info){
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, info.getQueueId());
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, info.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 10240000);
        props.put(ProducerConfig.ACKS_CONFIG, "1");
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, getRequestTimeout(info.getRequestTimeout()));
        if (StringUtils.isNotBlank(info.getSecurityProtocol())) {
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, info.getSecurityProtocol());
            props.put(SaslConfigs.SASL_MECHANISM, info.getSaslMechanism());
            props.put(SaslConfigs.SASL_JAAS_CONFIG, info.getSaslJaasConfig());
        }
        return new KafkaProducer<>(props);
    }

}
