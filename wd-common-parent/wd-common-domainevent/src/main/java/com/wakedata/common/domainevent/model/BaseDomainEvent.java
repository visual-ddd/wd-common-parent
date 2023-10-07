package com.wakedata.common.domainevent.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wakedata.common.core.GlobalApplicationContext;
import com.wakedata.common.core.context.BaseUserInfo;
import com.wakedata.common.domainevent.exception.DomainEventParamException;
import com.wakedata.common.mq.core.SpringElExpressionResolve;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Objects;
import java.util.regex.Pattern;


/**
 * 领域事件基类
 *
 * @author chenshaopeng
 * @date 2021/12/26
 */
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseDomainEvent {

    private static final long serialVersionUID = 136140408223080182L;

    private static final String EVENT_CODE_REGEX = "^[a-zA-Z][a-zA-Z0-9_]{5,47}$";

    /**
     * 用户信息上下文
     */
    @JSONField(serialzeFeatures = {SerializerFeature.WriteClassName})
    protected BaseUserInfo userInfoContext;


    /**
     * eventCode抽象方法
     * @return eventCode
     */
    public abstract String eventCode();

    public final String getEventCode() {
        return checkInvalidEventCodeError(this.getClass()
                , Objects.requireNonNull(GlobalApplicationContext.getBean(SpringElExpressionResolve.class))
                .resolveString(checkNonEventCodeError(this.getClass(), eventCode()))
        );
    }

    private static String checkNonEventCodeError(Class<?> classes, String eventCode){
        if (Objects.isNull(eventCode)) {
            throw new DomainEventParamException("eventCode不能为空! CLASS PATH: [" + classes.getName() + "]");
        }
        return eventCode;
    }

    private static String checkInvalidEventCodeError(Class<?> classes, String eventCode) {
        if (!Pattern.matches(EVENT_CODE_REGEX, eventCode)) {
            throw new DomainEventParamException("事件编码`" + eventCode + "`格式错误! " +
                    "只允许以字母开头的6至48位字母、数字和下划线组成的字符. CLASS PATH: [" + classes.getName() + "]");
        }
        return eventCode;
    }

    public void setUserInfoContext(BaseUserInfo userInfoContext) {
        this.userInfoContext = userInfoContext;
    }

    public BaseUserInfo getUserInfoContext() {
        return userInfoContext;
    }

}
