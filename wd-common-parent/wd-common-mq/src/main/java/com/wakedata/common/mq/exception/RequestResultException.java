package com.wakedata.common.mq.exception;


import com.wakedata.common.core.resultcode.CommonResultCode;
import com.wakedata.common.core.resultcode.ResultCode;

/**
 * Http请求结果异常
 *
 * @author chenshaopeng
 * @date 2021/11/10
 */
public class RequestResultException extends RuntimeException {

    private static final long serialVersionUID = 3147056898105735L;

    private Integer code;

    public RequestResultException() {
    }

    public RequestResultException(String message) {
        super(message);
        this.code = CommonResultCode.BUSINESS_ERROR.getCode();
    }

    public RequestResultException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }

    public RequestResultException(String message, Throwable cause) {
        super(message, cause);
        this.code = CommonResultCode.BUSINESS_ERROR.getCode();
    }

    public RequestResultException(Throwable cause) {
        super(cause);
        this.code = CommonResultCode.BUSINESS_ERROR.getCode();
    }

    public RequestResultException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = CommonResultCode.BUSINESS_ERROR.getCode();
    }

    public Integer getCode() {
        return this.code;
    }
}