package com.wakedata.common.bizlog.core;

import com.mzt.logapi.beans.LogRecord;
import com.wakedata.common.core.util.BeanUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class WKLLogRecordWrapper extends LogRecord {

    /**
     * 应用ID
     */
    private String appBuId;

    /**
     * 登录账户ID
     */
    private String userId;

    /**
     * 租户名称
     */
    private String tenantName;

    /**
     * pc端为用户名称（c端用户微信昵称）
     */
    private String nickName;


    public WKLLogRecordWrapper(LogRecord logRecord) {
        BeanUtil.copyProperties(logRecord, this);
    }
}