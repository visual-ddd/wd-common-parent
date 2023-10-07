package com.wakedata.common.domainevent.exception;


import com.wakedata.common.core.resultcode.CommonResultCode;
import com.wakedata.common.core.resultcode.ResultCode;

/**
 * 领域事件创建异常
 *
 * @author chenshaopeng
 * @date 2021/12/23
 */
public class DomainEventCreateException extends RuntimeException {

    private static final long serialVersionUID = 8229239613879193480L;

    private Integer code;

    public DomainEventCreateException() {
    }

    public DomainEventCreateException(String message) {
        super(message);
        this.code = CommonResultCode.BUSINESS_ERROR.getCode();
    }

    public DomainEventCreateException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }

    public DomainEventCreateException(String message, Throwable cause) {
        super(message, cause);
        this.code = CommonResultCode.BUSINESS_ERROR.getCode();
    }

    public DomainEventCreateException(Throwable cause) {
        super(cause);
        this.code = CommonResultCode.BUSINESS_ERROR.getCode();
    }

    public DomainEventCreateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = CommonResultCode.BUSINESS_ERROR.getCode();
    }

    public Integer getCode() {
        return this.code;
    }
}