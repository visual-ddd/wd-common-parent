package com.wakedata.common.bizlog.core;

import com.mzt.logapi.beans.LogRecord;
import com.mzt.logapi.service.ILogRecordService;
import com.wakedata.common.bizlog.config.BizLogConfig;
import com.wakedata.common.core.context.BaseUserInfo;
import com.wakedata.common.core.context.UserInfoContext;
import com.wakedata.common.mq.MqProducerHelper;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * BizLogRecordService
 *
 * @author focus
 * @date 2022/10/10
 **/
@Slf4j
public class BizLogRecordService implements ILogRecordService {

    @Resource
    BizLogConfig bizLogConfig;

    @Override
    public void record(LogRecord logRecord) {

        WKLLogRecordWrapper logRecordWrapper = new WKLLogRecordWrapper(logRecord);
        buildUserInfo(logRecordWrapper);

        // 发送mq消息,存储业务日志
        MqProducerHelper.publish(logRecordWrapper, bizLogConfig.getMqTopic());
        log.info("action: {}", logRecordWrapper.getAction());
//        log.error("bizNo: {}",logRecord.getBizNo());
//        log.error("operator : {}",logRecord.getOperator());
//
//        log.error("日志打印：{}",JSONObject.toJSONString(logRecord));
    }

    /**
     * 填充用户信息到业务日志中
     *
     * @param logRecordWrapper 业务日志
     */
    private static void buildUserInfo(WKLLogRecordWrapper logRecordWrapper) {
        BaseUserInfo userInfo = UserInfoContext.getUser();
        if (userInfo != null) {
            logRecordWrapper.setTenant(Optional.ofNullable(String.valueOf(userInfo.getTenantId()))
                .orElse(StringUtils.EMPTY));
            logRecordWrapper.setAppBuId(Optional.ofNullable(String.valueOf(userInfo.getAppBuId()))
                .orElse(StringUtils.EMPTY));
            logRecordWrapper.setUserId(Optional.ofNullable(String.valueOf(userInfo.getUserId()))
                .orElse(StringUtils.EMPTY));
            logRecordWrapper.setTenantName(
                Optional.ofNullable(String.valueOf(userInfo.getTenantName()))
                    .orElse(StringUtils.EMPTY));
            logRecordWrapper.setNickName(Optional.ofNullable(String.valueOf(userInfo.getNickName()))
                .orElse(StringUtils.EMPTY));
        }
    }

    @Override
    public List<LogRecord> queryLog(String bizNo, String type) {
        return null;
    }

    @Override
    public List<LogRecord> queryLogByBizNo(String bizNo, String type, String subType) {
        return null;
    }
}
