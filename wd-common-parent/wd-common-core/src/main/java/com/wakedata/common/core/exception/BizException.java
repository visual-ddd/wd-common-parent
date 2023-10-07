package com.wakedata.common.core.exception;


import com.wakedata.common.core.resultcode.CommonResultCode;
import com.wakedata.common.core.resultcode.ResultCode;

/**
 * 业务逻辑异常,全局异常拦截后统一返回ResponseCode.BUSINESS_ERROR
 *
 * @date 2021/1/28
 */
public class BizException extends RuntimeException {

    /**
     * 错误编码
     */
    private Integer code;

    /**
     * 动态参数
     */
    private String[] args;

    public BizException() {

    }

    public BizException(String message) {
        super(message);
        this.code = CommonResultCode.BUSINESS_ERROR.getCode();
    }

    public BizException(String message,String[] args){
        super(message);
        this.code = CommonResultCode.BUSINESS_ERROR.getCode();
        this.args = args;
    }

    public BizException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }


    public BizException(ResultCode resultCode,String[] args) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
        this.args = args;
    }


    public BizException(ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
        this.code = CommonResultCode.BUSINESS_ERROR.getCode();
    }

    public BizException(Throwable cause) {
        super(cause);
        this.code = CommonResultCode.BUSINESS_ERROR.getCode();
    }

    public BizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = CommonResultCode.BUSINESS_ERROR.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public String[] getArgs() {
        return args;
    }
}
