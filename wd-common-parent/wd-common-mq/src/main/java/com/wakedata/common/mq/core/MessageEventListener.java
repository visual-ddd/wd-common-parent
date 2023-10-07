package com.wakedata.common.mq.core;

import com.wakedata.common.mq.model.BusinessParamWrapper;

import java.lang.reflect.Method;

/**
 * @author: hhf
 * @date: 2022/6/14
 **/
public interface MessageEventListener {
    /**
     * 错误时回调
     * @param bean
     * @param method
     * @param wrapper
     */
    void onError(Object bean, Method method, BusinessParamWrapper wrapper);
}
