package com.wakedata.common.mq.exception;


import com.wakedata.common.core.resultcode.CommonResultCode;
import com.wakedata.common.core.resultcode.ResultCode;

/**
 * 消费者已存在异常
 *
 * @author chenshaopeng
 * @date 2021/11/10
 */
public class AlreadyExistConsumerException extends RuntimeException {

    private static final long serialVersionUID = -964322540031084286L;

    private Integer code;

    public AlreadyExistConsumerException() {
    }

    public AlreadyExistConsumerException(String message) {
        super(message);
        this.code = CommonResultCode.BUSINESS_ERROR.getCode();
    }

    public AlreadyExistConsumerException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }

    public AlreadyExistConsumerException(String message, Throwable cause) {
        super(message, cause);
        this.code = CommonResultCode.BUSINESS_ERROR.getCode();
    }

    public AlreadyExistConsumerException(Throwable cause) {
        super(cause);
        this.code = CommonResultCode.BUSINESS_ERROR.getCode();
    }

    public AlreadyExistConsumerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = CommonResultCode.BUSINESS_ERROR.getCode();
    }

    public Integer getCode() {
        return this.code;
    }
}