package com.wakedata.common.mq.model.consumer;

import com.wakedata.common.mq.model.MqConfigParam;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 消费者新增订阅 - 包装类
 *
 * @author chenshaopeng
 * @date 2021/11/10
 */
@Setter
@Getter
@EqualsAndHashCode
@SuperBuilder
public class MakeSubscribeBody extends MqConfigParam {

    /**
     * 队列记录列表
     */
    private List<QueueRecord> queueRecordList;

    public void setQueueRecordList(List<QueueRecord> queueRecordList) {
        this.queueRecordList = queueRecordList;
    }
}
