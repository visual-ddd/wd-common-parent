package com.wakedata.common.mq.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * MQ配置参数 - 包装类
 *
 * @author chenshaopeng
 * @date 2021/12/10
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MqConfigParam extends BaseMqConfigParam {

    /**
     * 单次拉取最大消息数
     * 需根据实际消费能力进行设置
     *
     */
    protected Integer batchPullNumber;

    /**
     * 无消息时最大等待时间
     */
    protected Integer pollingWaitSeconds;

}
