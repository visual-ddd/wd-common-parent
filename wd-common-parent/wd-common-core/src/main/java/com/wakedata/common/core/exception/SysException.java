package com.wakedata.common.core.exception;


import com.wakedata.common.core.resultcode.CommonResultCode;

/**
 * 服务端异常
 * 业务逻辑异常,全局异常拦截后统一返回ResponseCode.BUSINESS_ERROR
 *
 * @date: 2021/1/28
 **/
public class SysException extends RuntimeException {
    private Integer code;

    public SysException() {
    }

    public SysException(String message) {
        super(message);
        this.code = CommonResultCode.SYSTEM_ERROR.getCode();
    }

    public SysException(String message, Throwable cause) {
        super(message, cause);
        this.code = CommonResultCode.SYSTEM_ERROR.getCode();
    }

    public SysException(Throwable cause) {
        super(cause);
        this.code = CommonResultCode.SYSTEM_ERROR.getCode();
    }

    public SysException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = CommonResultCode.SYSTEM_ERROR.getCode();
    }

    public Integer getCode() {
        return code;
    }
}
