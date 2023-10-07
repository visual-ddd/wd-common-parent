package com.wakedata.common.core.location;

import lombok.Data;

/**
 * @author zhengqinghui@wakedata.com
 * @date 2022/3/30 10:35
 */
@Data
public class IpInterpretationResultAdInfoDO {

    /**
     * 国家
     */
    private String nation;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String district;

    /**
     * 行政区划代码
     */
    private Integer adCode;
}
