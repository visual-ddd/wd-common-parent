package com.wakedata.common.mq.exception;


import com.wakedata.common.core.resultcode.CommonResultCode;
import com.wakedata.common.core.resultcode.ResultCode;

/**
 * 消息错误异常
 *
 * @author chenshaopeng
 * @date 2021/11/10
 */
public class MessageErrorException extends RuntimeException {

    private static final long serialVersionUID = 8452169671975818032L;

    private Integer code;

    public MessageErrorException() {
    }

    public MessageErrorException(String message) {
        super(message);
        this.code = CommonResultCode.BUSINESS_ERROR.getCode();
    }

    public MessageErrorException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }

    public MessageErrorException(String message, Throwable cause) {
        super(message, cause);
        this.code = CommonResultCode.BUSINESS_ERROR.getCode();
    }

    public MessageErrorException(Throwable cause) {
        super(cause);
        this.code = CommonResultCode.BUSINESS_ERROR.getCode();
    }

    public MessageErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = CommonResultCode.BUSINESS_ERROR.getCode();
    }

    public Integer getCode() {
        return this.code;
    }
}