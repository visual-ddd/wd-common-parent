package com.wakedata.common.core.dto;

import com.wakedata.common.core.GlobalApplicationContext;
import com.wakedata.common.core.resultcode.CommonResultCode;
import com.wakedata.common.core.resultcode.MessageSourceSupport;
import com.wakedata.common.core.resultcode.ResultCode;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.io.Serializable;
import java.util.Objects;

/**
 * 返回类
 *
 * @param <T>
 */
public class ResultDTO<T> implements Serializable {

    private static final long serialVersionUID = -3430603013914181383L;
    private Integer code;

    private String msg;

    protected T data;

    private Boolean success;


    public ResultDTO() {
        init(CommonResultCode.SUCCESS.getCode(), null, null);
    }

    public ResultDTO(ResultCode resultCode, String msg) {
        init(resultCode.getCode(), msg, null);
    }

    public ResultDTO(ResultCode resultCode, T data) {
        init(resultCode.getCode(), resultCode.getMsg(), data);
    }

    public ResultDTO(ResultCode resultCode, T data, String msg) {
        init(resultCode.getCode(), msg, data);
    }

    public ResultDTO(Integer code, T data, String msg) {
        init(code, msg, data);
    }

    private ResultDTO(ResultCode resultCode) {
        init(resultCode.getCode(), resultCode.getMsg(), null);
    }

    private ResultDTO(Integer resultCode, String msg) {
        init(resultCode, msg, null);
    }

    public ResultDTO(ResultDTO<T> resultDTO) {
        init(resultDTO.getCode(), resultDTO.getMsg(), null);
    }

    private void init(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        if(Objects.nonNull(this.code)){
            this.success = this.code.equals(CommonResultCode.SUCCESS.getCode()) ? Boolean.TRUE : Boolean.FALSE;
        }
    }

    public static <T> ResultDTO<T> success() {
        return new ResultDTO<>(CommonResultCode.SUCCESS);
    }

    public static <T> ResultDTO<T> success(T data) {
        return new ResultDTO<>(CommonResultCode.SUCCESS, data);
    }

    public static <T> ResultDTO<T> success(T data, String msg) {
        return new ResultDTO<>(CommonResultCode.SUCCESS, data, msg);
    }

    public static <T> ResultDTO<T> fail() {
        return new ResultDTO<>(CommonResultCode.SYSTEM_ERROR);
    }

    public static <T> ResultDTO<T> fail(ResultCode resultCode) {
        try {
            resultCode.setMsg(Objects.requireNonNull(GlobalApplicationContext
                    .getBean(MessageSourceSupport.class)).getMessageByDefaultLocale(resultCode.getMsg())
            );
        } catch (NoSuchBeanDefinitionException ignored) {
        }
        return new ResultDTO<>(resultCode);
    }

    public static <T> ResultDTO<T> fail(String msg) {
        return new ResultDTO<>(CommonResultCode.SYSTEM_ERROR.getCode(), msg);
    }

    public static <T> ResultDTO<T> fail(Integer code, String msg) {
        return new ResultDTO<>(code, msg);
    }

    public static <T> ResultDTO<T> fail(Integer code,T data,String msg) {
        return new ResultDTO<>(code,data,msg);
    }

    public String getMsg() {
        return msg;
    }

    public ResultDTO<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public ResultDTO<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        if (null == success) {
            return Objects.nonNull(this.code) && CommonResultCode.SUCCESS.getCode()
                .equals(this.code);
        }
        return success;
    }

    @Override
    public String toString() {
        return "ResultDTO{" + "core=" + code + ", msg='" + msg + '\'' + ", success=" + isSuccess() + ", data=" + data +
            '}';
    }
}
