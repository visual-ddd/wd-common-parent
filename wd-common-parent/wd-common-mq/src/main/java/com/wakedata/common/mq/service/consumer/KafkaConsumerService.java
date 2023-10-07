package com.wakedata.common.mq.service.consumer;

import com.wakedata.common.mq.exception.ConnectException;
import com.wakedata.common.mq.model.consumer.Message;
import com.wakedata.common.mq.model.consumer.QueueRecord;
import com.wakedata.common.mq.model.consumer.MakeSubscribeBody;
import com.wakedata.common.mq.exception.AlreadyExistConsumerException;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.errors.TimeoutException;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.*;
import java.util.function.Consumer;

/**
 * Kafka消费者服务
 *
 * @author chenshaopeng
 * @date 2021/12/11
 */
public class KafkaConsumerService extends AbstractConsumerService {

    private final static Map<String, KafkaConsumerThread> KAFKA_CONSUMER_THREAD_MAP = new HashMap<>();


    @Override
    public synchronized void addSubscribe(MakeSubscribeBody info, Consumer<Message> msgConsumer){
        String threadKey = generateConsumerKey(
                info.getQueueId(), info.getBootstrapServers(), info.getTopic(), info.getTag());

        if(KAFKA_CONSUMER_THREAD_MAP.get(threadKey) != null){
            throw new AlreadyExistConsumerException();
        }

        KafkaConsumerThread kafkaConsumerThread;
        try {
            KafkaConsumer<String, String> kafkaConsumer = this.createKafkaConsumer(info);
            kafkaConsumer.subscribe(Collections.singletonList(info.getTopic())
                    , new ResetLastOffsetListener(kafkaConsumer, info));

            kafkaConsumerThread = new KafkaConsumerThread(kafkaConsumer, msgConsumer, info.getQueueId());
            kafkaConsumerThread.start();
        } catch (TimeoutException | ConfigException e) {
            throw new ConnectException(e);
        }
        KAFKA_CONSUMER_THREAD_MAP.put(threadKey, kafkaConsumerThread);
    }

    @Override
    public Set<String> subscribeList(){
        return KAFKA_CONSUMER_THREAD_MAP.keySet();
    }

    @Override
    public synchronized void deleteSubscribe(String consumerKey){
        KafkaConsumerThread consumerThread = KAFKA_CONSUMER_THREAD_MAP.get(consumerKey);
        if (consumerThread == null) {
            return;
        }
        // 调用wakeup优雅退出
        consumerThread.getKafkaConsumer().wakeup();
        KAFKA_CONSUMER_THREAD_MAP.remove(consumerKey);
    }


    private KafkaConsumer<String, String> createKafkaConsumer(MakeSubscribeBody info){
        Properties props = new Properties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, info.getQueueId());
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, info.getBootstrapServers());
        props.put(ConsumerConfig.EXCLUDE_INTERNAL_TOPICS_CONFIG, false);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, RoundRobinAssignor.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, getRequestTimeout(info.getRequestTimeout()));
        if(Objects.nonNull(info.getBatchPullNumber())){
            // 吞吐量, 单次拉取最大消息数
            props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, info.getBatchPullNumber());
        }
        if (StringUtils.isNotBlank(info.getSecurityProtocol())) {
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, info.getSecurityProtocol());
            props.put(SaslConfigs.SASL_MECHANISM, info.getSaslMechanism());
            props.put(SaslConfigs.SASL_JAAS_CONFIG, info.getSaslJaasConfig());
        }
        try {
            return new KafkaConsumer<>(props);
        } catch (KafkaException e) {
            throw new ConfigException(e.getMessage());
        }
    }

    /**
     * 重置offset, 如果本地有offset记录, 则重置为上次保存的offset; 否则从0开始拉取消息
     */
    public static class ResetLastOffsetListener implements ConsumerRebalanceListener{
        private final KafkaConsumer<String, String> consumer;

        private final MakeSubscribeBody info;

        public ResetLastOffsetListener(KafkaConsumer<String, String> consumer, MakeSubscribeBody info) {
            this.consumer = consumer;
            this.info = info;
        }

        @Override
        public void onPartitionsRevoked(Collection<TopicPartition> collection) {
        }

        @Override
        public void onPartitionsAssigned(Collection<TopicPartition> collection) {
            if(Objects.isNull(info.getQueueRecordList()) || info.getQueueRecordList().isEmpty()){
                return;
            }
            collection.forEach(topicPartition -> {
                for(QueueRecord queueRecord : info.getQueueRecordList()){
                    if(topicPartition.partition() == queueRecord.getPartition()){
                        consumer.seek(topicPartition, queueRecord.getOffset() + 1);
                    }
                }
            });
        }
    }

}