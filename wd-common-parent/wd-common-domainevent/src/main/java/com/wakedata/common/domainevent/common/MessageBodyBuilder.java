package com.wakedata.common.domainevent.common;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import com.wakedata.common.core.GlobalApplicationContext;
import com.wakedata.common.core.constants.IndustryEnum;
import com.wakedata.common.core.context.BaseUserInfo;
import com.wakedata.common.domainevent.model.BaseDomainEvent;
import com.wakedata.common.domainevent.model.MessageBodyWrapper;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 消息体构造器
 *
 * @author chenshaopeng
 * @date 2021/12/16
 */
public class MessageBodyBuilder {

    /**
     * 构造统一消息格式
     */
    public static MessageBodyWrapper build(BaseDomainEvent event) {
        MessageBodyWrapper messageBodyWrapper = new MessageBodyWrapper();
        messageBodyWrapper.setEpId(Optional.ofNullable(event.getUserInfoContext())
                .map(BaseUserInfo::getTenantId).orElse(-1L));
        messageBodyWrapper.setAppId(Optional.ofNullable(event.getUserInfoContext())
                .map(BaseUserInfo::getAppBuId).orElse(-1L));
        messageBodyWrapper.setEventCode(event.getEventCode());
        messageBodyWrapper.setInfoData(JSON.toJSONString(event));
        messageBodyWrapper.setSourceEventId(UUID.fastUUID().toString(true));
        messageBodyWrapper.setSourceSystemCode(GlobalApplicationContext.getApplicationContext().getId());
        messageBodyWrapper.setEventHappenTimestamp(System.currentTimeMillis());
        messageBodyWrapper.setMessageSendTimestamp(messageBodyWrapper.getEventHappenTimestamp());
        messageBodyWrapper.setTags("*");
        messageBodyWrapper.setIndustryKey(findIndustryKey(event));
        return messageBodyWrapper;
    }

    private static String findIndustryKey(BaseDomainEvent event) {
        BaseUserInfo baseUserInfo = event.getUserInfoContext();
        if (Objects.isNull(baseUserInfo)) {
            return IndustryEnum.DEFAULT.getIndustryKey();
        }
        Map<String, Object> otherMap = baseUserInfo.getOther();
        if (Objects.isNull(otherMap)) {
            return IndustryEnum.DEFAULT.getIndustryKey();
        }
        return (String) otherMap.getOrDefault("appKeyFlag", IndustryEnum.DEFAULT.getIndustryKey());
    }

}
