package com.wakedata.common.domainevent.retry.event;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.wakedata.common.domainevent.model.MessageBodyWrapper;
import com.wakedata.common.domainevent.retry.dao.RetryEventDAO;
import com.wakedata.common.domainevent.retry.module.RetryEventDO;
import com.wakedata.common.domainevent.retry.module.RetryTypeEnum;
import com.wakedata.common.domainevent.retry.module.TargetInfo;
import org.springframework.context.ApplicationListener;

import javax.annotation.Resource;

/**
 * 重试事件监听
 *
 * @author hhf
 * @date 2022-6-14
 */
public class RetryEventListener implements ApplicationListener<RetryEvent> {

    @Resource
    RetryEventDAO failedEventDAO;

    @Override
    public void onApplicationEvent(RetryEvent retryEvent) {
        TargetInfo targetInfo = new TargetInfo();
        targetInfo.setClassName(retryEvent.getClassName());
        targetInfo.setMethodName(retryEvent.getMethodName());
        targetInfo.setParamClassName(retryEvent.getParamClassName());
        RetryEventDO failedEventDO = new RetryEventDO();
        failedEventDO.setEventInfo(retryEvent.getParamValue());
        failedEventDO.setTargetInfo(targetInfo);
        failedEventDO.setIdem(calcIdem(retryEvent));
        failedEventDO.setRetryType(RetryTypeEnum.SECHEDULE.getValue());
        failedEventDAO.save(failedEventDO);
    }

    /**
     * 计算幂等值
     * @param retryEvent
     * @return
     */
    private String calcIdem(RetryEvent retryEvent) {
        MessageBodyWrapper messageBodyWrapper = JSON.parseObject(retryEvent.getParamValue(), MessageBodyWrapper.class);
        StringBuilder builder = new StringBuilder();
        builder.append(retryEvent.getClassName());
        builder.append(retryEvent.getParamClassName());
        builder.append(retryEvent.getMethodName());
        builder.append(messageBodyWrapper.getEpId());
        builder.append(messageBodyWrapper.getAppId());
        builder.append(messageBodyWrapper.getEventCode());
        builder.append(messageBodyWrapper.getInfoData());
        builder.append(messageBodyWrapper.getEventHappenTimestamp());
        return SecureUtil.md5(builder.toString());
    }
}