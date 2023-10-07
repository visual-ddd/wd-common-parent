package com.wakedata.common.domainevent.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 消息对象 - 包装类
 *
 * @author chenshaopeng
 * @date 2021/12/17
 */
@Getter
@Setter
@ToString
public class MessageBodyWrapper implements Serializable {

    private static final long serialVersionUID = -6547545932351810962L;

    /**
     * 集团id
     */
    private Long epId;

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 源事件唯一ID
     */
    private String sourceEventId;

    /**
     * 事件编码
     */
    private String eventCode;

    /**
     * 来源系统编码
     */
    private String sourceSystemCode;

    /**
     * 消息发送的时间戳
     */
    private Long messageSendTimestamp;

    /**
     * 事件发生的时间戳
     */
    private Long eventHappenTimestamp;

    /**
     * 消息内容
     */
    private String infoData;

    /**
     * 消息标签
     */
    private String tags;

    /**
     * 消息所属行业key
     */
    private String industryKey;

}
