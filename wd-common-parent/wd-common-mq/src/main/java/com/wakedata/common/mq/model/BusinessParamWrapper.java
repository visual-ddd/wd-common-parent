package com.wakedata.common.mq.model;

import com.wakedata.common.mq.model.consumer.Message;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 业务参数
 *
 * @author chenshaopeng
 * @date 2021/12/18
 */
@SuperBuilder
@Getter
@ToString
public class BusinessParamWrapper {

    private final Message message;

}
