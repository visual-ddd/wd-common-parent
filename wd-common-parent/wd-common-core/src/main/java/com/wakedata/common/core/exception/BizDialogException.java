package com.wakedata.common.core.exception;


import com.wakedata.common.core.resultcode.CommonResultCode;
import com.wakedata.common.core.resultcode.ResultCode;

/**
 * 业务逻辑异常,全局异常拦截后统一返回ResponseCode.BUSINESS_DIALOG_ERROR
 * 前端使用对话框的方式提示错误消息
 *
 * @author hhf
 * @date 2021/1/28
 */
public class BizDialogException extends RuntimeException {

    /**
     * 错误编码
     */
    private Integer code;

    /**
     * 动态参数
     */
    private String[] args;

    public BizDialogException() {

    }

    public BizDialogException(String message) {
        super(message);
        this.code = CommonResultCode.BUSINESS_DIALOG_ERROR.getCode();
    }

    public BizDialogException(String message, String[] args){
        super(message);
        this.code = CommonResultCode.BUSINESS_DIALOG_ERROR.getCode();
        this.args = args;
    }

    public BizDialogException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = CommonResultCode.BUSINESS_DIALOG_ERROR.getCode();
    }


    public BizDialogException(ResultCode resultCode, String[] args) {
        super(resultCode.getMsg());
        this.code = CommonResultCode.BUSINESS_DIALOG_ERROR.getCode();
        this.args = args;
    }


    public BizDialogException(ResultCode resultCode, String message) {
        super(message);
        this.code = CommonResultCode.BUSINESS_DIALOG_ERROR.getCode();
    }

    public BizDialogException(String message, Throwable cause) {
        super(message, cause);
        this.code = CommonResultCode.BUSINESS_DIALOG_ERROR.getCode();
    }

    public BizDialogException(Throwable cause) {
        super(cause);
        this.code = CommonResultCode.BUSINESS_DIALOG_ERROR.getCode();
    }

    public BizDialogException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = CommonResultCode.BUSINESS_DIALOG_ERROR.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public String[] getArgs() {
        return args;
    }
}
