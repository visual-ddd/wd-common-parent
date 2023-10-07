package com.wakedata.common.mq.exception;


import com.wakedata.common.core.resultcode.CommonResultCode;
import com.wakedata.common.core.resultcode.ResultCode;

/**
 * 连接异常
 *
 * @author chenshaopeng
 * @date 2021/11/10
 */
public class ConnectException extends RuntimeException {

    private static final long serialVersionUID = 8229239613879193480L;

    private Integer code;

    public ConnectException() {
    }

    public ConnectException(String message) {
        super(message);
        this.code = CommonResultCode.BUSINESS_ERROR.getCode();
    }

    public ConnectException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }

    public ConnectException(String message, Throwable cause) {
        super(message, cause);
        this.code = CommonResultCode.BUSINESS_ERROR.getCode();
    }

    public ConnectException(Throwable cause) {
        super(cause);
        this.code = CommonResultCode.BUSINESS_ERROR.getCode();
    }

    public ConnectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = CommonResultCode.BUSINESS_ERROR.getCode();
    }

    public Integer getCode() {
        return this.code;
    }
}