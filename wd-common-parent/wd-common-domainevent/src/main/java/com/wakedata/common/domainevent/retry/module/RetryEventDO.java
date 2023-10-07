package com.wakedata.common.domainevent.retry.module;

import lombok.Data;

import java.util.Date;

/**
 * RetryEventDO
 * @author hhf
 * @date 2022/6/13
 **/
@Data
public class RetryEventDO {
    private Long id;
    /**
     * 事件内容
     */
    private String eventInfo;
    /**
     * 目标方法信息
     */
    private TargetInfo targetInfo;
    /**
     * 幂等值
     */
    private String idem;
    /**
     * 重试次数
     */
    private Integer retryTimes;
    /**
     * 最后重试时间
     */
    private Date lastRetryTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 重试类型
     */
    private Integer retryType;
}