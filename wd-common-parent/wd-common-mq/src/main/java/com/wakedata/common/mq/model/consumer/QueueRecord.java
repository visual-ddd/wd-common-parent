package com.wakedata.common.mq.model.consumer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 接收队列记录 - 包装类
 *
 * @author chenshaopeng
 * @date 2021/11/26
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class QueueRecord {

    /**
     * 队列组Id
     */
    private String groupId;

    /**
     * 主题
     */
    private String topic;

    /**
     * 偏移量
     */
    private Long offset;

    /**
     * 分区
     */
    private Integer partition;

    /**
     * 队列Id
     */
    private Integer queueId;

    /**
     * 队列(cmq)
     */
    private String queue;

    /**
     * MQ BrokerName
     */
    private String brokerName;

}
