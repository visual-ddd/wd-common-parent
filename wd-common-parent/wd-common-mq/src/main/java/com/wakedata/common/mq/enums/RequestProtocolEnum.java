package com.wakedata.common.mq.enums;

import com.wakedata.common.mq.service.MqServiceInterface;
import com.wakedata.common.mq.service.consumer.AbstractConsumerService;
import com.wakedata.common.mq.service.consumer.CmqConsumerService;
import com.wakedata.common.mq.service.consumer.RabbitMqConsumerService;
import com.wakedata.common.mq.service.consumer.KafkaConsumerService;
import com.wakedata.common.mq.service.consumer.RocketMqConsumerService;
import com.wakedata.common.mq.service.producer.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 请求协议枚举 - 含协议相关实例
 *
 * @author chenshaopeng
 * @date 2021/8/8
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum RequestProtocolEnum implements MqServiceInterface {

    /**
     * 各种协议定义与相关的服务实例
     */
    HTTP(
            1, Boolean.FALSE
            , null, new HttpProducerService()
    ) {
        @Override
        public AbstractConsumerService consumer() {
            throw new RuntimeException("No consumer service instance!");
        }

    }

    , KAFKA(
            2, Boolean.TRUE
            , new KafkaConsumerService(), new KafkaProducerService()
    )

    , ROCKET_MQ(
            3, Boolean.TRUE
            , new RocketMqConsumerService(), new RocketMqProducerService()
    )

    , C_MQ(
            4, Boolean.TRUE
            , new CmqConsumerService(), new CmqProducerService()
    )

    , RABBITMQ(
        5, Boolean.TRUE
        , new RabbitMqConsumerService(), new RabbitMqProducerService()
    );


    /**
     * 枚举值
     */
    private Integer value;

    /**
     * 是否主动协议
     */
    private boolean active;

    /**
     * 消费者服务
     */
    private AbstractConsumerService consumerService;

    /**
     * 生产者服务
     */
    private AbstractProducerService producerService;


    @Override
    public AbstractConsumerService consumer() {
        return getConsumerService();
    }

    @Override
    public AbstractProducerService producer() {
        return getProducerService();
    }


    public String getName(){
        return this.toString();
    }

    public static String getName(int value){
        return covert(value).toString();
    }


    public static RequestProtocolEnum covert(int value){
        for(RequestProtocolEnum e : RequestProtocolEnum.values()){
            if(e.getValue() == value){
                return e;
            }
        }
        throw new RuntimeException("Enumeration not found!");
    }

    public static RequestProtocolEnum covert(String value){
        for(RequestProtocolEnum e : RequestProtocolEnum.values()){
            if(e.toString().startsWith(value.substring(0,2).toUpperCase())){
                return e;
            }
        }
        throw new RuntimeException("Enumeration not found!");
    }

    public static RequestProtocolEnum[] activeRequestTcp(){
        List<RequestProtocolEnum> list = new ArrayList<>();
        for(RequestProtocolEnum protocol : RequestProtocolEnum.values()){
            if(protocol.isActive()){
                list.add(protocol);
            }
        }
        return list.toArray(new RequestProtocolEnum[0]);
    }

}
