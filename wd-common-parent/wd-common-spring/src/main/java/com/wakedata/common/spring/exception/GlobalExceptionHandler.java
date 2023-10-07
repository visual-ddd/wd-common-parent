package com.wakedata.common.spring.exception;

import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.wakedata.common.core.constants.CommonConstant;
import com.wakedata.common.core.dto.ResultDTO;
import com.wakedata.common.core.exception.BizDialogException;
import com.wakedata.common.core.exception.BizException;
import com.wakedata.common.core.resultcode.CommonResultCode;
import com.wakedata.common.spring.i18n.MessageSourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * 全局异常处理类-项目使用可继承该类
 * @author luomeng
 * @date 2021/12/30 22:06
 */
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    private MessageSourceUtil messageSourceUtil;

    @ExceptionHandler(Exception.class)
    public ResultDTO<?> exceptionHandler(HttpServletRequest request,Exception ex) {
        log.error("sys error, message={}", ex.getMessage(), ex);
        return ResultDTO.fail(CommonResultCode.SYSTEM_ERROR.getCode()
                ,getI18nMessage(request,CommonResultCode.SYSTEM_ERROR.getMsg()));
    }

    @ExceptionHandler(BizException.class)
    public ResultDTO<?> bizExceptionHandler(HttpServletRequest request, BizException ex) {
        String message = getI18nMessage(request, ex.getMessage(), ex.getArgs());
        log.error("BizException message={}", message);
        return ResultDTO.fail(ex.getCode(), message);
    }

    @ExceptionHandler(BizDialogException.class)
    public ResultDTO<?> bizDialogExceptionHandler(HttpServletRequest request, BizDialogException ex) {
        String message = getI18nMessage(request, ex.getMessage(), ex.getArgs());
        log.error("BizException message={}", message);
        return ResultDTO.fail(ex.getCode(), message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultDTO<?> methodArgumentNotValidExceptionHandler(HttpServletRequest request,MethodArgumentNotValidException ex) {
        log.error("methodArgumentNotValid message={}", ex.getMessage(), ex);
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> msgList = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return getMessageList(request, msgList);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResultDTO<?> constraintViolationExceptionHandler(HttpServletRequest request,ConstraintViolationException ex) {
        log.error("constraintViolation message={}", ex.getMessage(), ex);
        List<String> msgList = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        return getMessageList(request, msgList);
    }

    @ExceptionHandler({BindException.class})
    public ResultDTO<?> bindExceptionHandler(HttpServletRequest request, BindException ex) {
        log.error("bindExceptionHandler message={}", ex.getMessage(), ex);
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> msgList = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return getMessageList(request, msgList);
    }

    private ResultDTO<?> getMessageList(HttpServletRequest request, List<String> msgList) {
        List<String> messageList = new ArrayList<>();
        for (String key : msgList) {
            messageList.add(this.getI18nMessage(request, key));
        }
        return ResultDTO.fail(String.join(StrPool.COMMA,messageList));
    }


    @ExceptionHandler(TypeMismatchException.class)
    public ResultDTO<?> typeMismatchExceptionHandler(HttpServletRequest request,TypeMismatchException ex) {
        log.error("param type error , message={}", ex.getMessage(), ex);
        return ResultDTO.fail(CommonResultCode.ERROR_PARAM.getCode()
                ,getI18nMessage(request,CommonResultCode.ERROR_PARAM.getMsg()));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResultDTO<?> httpRequestMethodNotSupportedExceptionHandler(HttpServletRequest request,HttpRequestMethodNotSupportedException ex) {
        log.error("httpRequestMethodNotSupported message={}", ex.getMessage(), ex);
        return ResultDTO.fail(CommonResultCode.REQUEST_METHOD_ERROR.getCode()
                ,getI18nMessage(request,CommonResultCode.REQUEST_METHOD_ERROR.getMsg()));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResultDTO<?> httpMediaTypeNotSupportedExceptionHandler(HttpServletRequest request,HttpMediaTypeNotSupportedException ex) {
        log.error("httpMediaTypeNotSupported message={}", ex.getMessage(), ex);
        return ResultDTO.fail(CommonResultCode.REQUEST_METHOD_ERROR.getCode()
                ,getI18nMessage(request,CommonResultCode.REQUEST_METHOD_ERROR.getMsg()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResultDTO<?> httpMessageNotReadableExceptionHandler(HttpServletRequest request,HttpMessageNotReadableException ex) {
        log.error("httpMessageNotReadable message={}", ex.getMessage(), ex);
        return ResultDTO.fail(CommonResultCode.ILLEGAL_PARAM.getCode()
                ,getI18nMessage(request,CommonResultCode.ILLEGAL_PARAM.getMsg()));
    }

    /**
     * 获取国际化信息
     * @param request
     * @param messageResourceKey
     * @return
     */
    public String getI18nMessage(HttpServletRequest request,String messageResourceKey){
        if(StrUtil.isBlank(messageResourceKey)){
            return null;
        }
        Locale locale = RequestContextUtils.getLocale(request);
        return messageSourceUtil.getMessageByCurrentLocale(messageResourceKey,messageResourceKey,locale);
    }

    /**
     * 获取国际化信息，带参数
     * @param request
     * @param messageResourceKey
     * @param args
     * @return
     */
    public String getI18nMessage(HttpServletRequest request,String messageResourceKey,String[] args){
        if(args == null || args.length == 0){
            return getI18nMessage(request, messageResourceKey);
        }
        if(StrUtil.isBlank(messageResourceKey)){
            return null;
        }
        final Locale locale = RequestContextUtils.getLocale(request);
        String[] params = Arrays.stream(args).map(param->{
            if(param != null && param.contains(CommonConstant.POINT)){
                param = messageSourceUtil.getMessageByCurrentLocale(param,param,locale);
            }
            return param;
        }).toArray(String[]::new);
        return messageSourceUtil.getMessageByCurrentLocale(messageResourceKey,messageResourceKey,locale,params);
    }


}
