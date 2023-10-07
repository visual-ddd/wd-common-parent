package com.wakedata.common.core.location;

import lombok.Data;

/**
 * @author zhengqinghui@wakedata.com
 * @date 2022/3/30 10:30
 */
@Data
public class IpInterpretationResultDO {

    /**
     * ip
     */
    private String ip;

    /**
     * 经纬度
     */
    private LocationToPoiDO location;

    /**
     * 定位行政区划信息
     */
    private IpInterpretationResultAdInfoDO adInfo;
}