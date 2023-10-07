package com.wakedata.common.mq.model.consumer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * 接收消息体 - 包装类
 *
 * @author chenshaopeng
 * @date 2021/11/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class Message extends QueueRecord {

    /**
     * 消息内容
     */
    private String value;

}
