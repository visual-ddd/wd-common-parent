package com.wakedata.common.domainevent.model;

import com.wakedata.common.mq.model.BusinessParamWrapper;
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
public class DomainBusinessParamWrapper extends BusinessParamWrapper {

    private final String eventCode;

}
