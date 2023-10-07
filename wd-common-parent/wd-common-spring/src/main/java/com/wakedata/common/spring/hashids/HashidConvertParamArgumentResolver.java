package com.wakedata.common.spring.hashids;

import cn.hutool.core.util.StrUtil;
import com.wakedata.common.core.hashids.HashidsUtil;
import com.wakedata.common.core.hashids.annotation.HashidsConvert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;


/**
 * @author luomeng
 * @Description 自定义hashids参数解析器，支持对request param的参数进行解析
 * @createTime 2022-05-18 16:59:00
 */
@Slf4j
public class HashidConvertParamArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //支持的参数类型
        if (!parameter.hasParameterAnnotation(HashidsConvert.class)) {
            return false;
        }
        return HashidsUtil.checkProperty(parameter.nestedIfOptional().getNestedParameterType());
    }

    @Override
    public Object resolveArgument(@Nullable MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer
            , @Nullable NativeWebRequest request, WebDataBinderFactory webDataBinderFactory) throws Exception {

        //识别方法的参数名字 基于ASM
        DefaultParameterNameDiscoverer defaultParameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        methodParameter.initParameterNameDiscovery(defaultParameterNameDiscoverer);
        //获取参数值
        HttpServletRequest servletRequest = (HttpServletRequest) request.getNativeRequest(HttpServletRequest.class);
        String value = servletRequest.getParameter(methodParameter.getParameterName());
        log.info("hash convert id info，param：{},value：{}", methodParameter.getParameterName(), value);
        if (StrUtil.isBlank(value)) {
            return null;
        }
        HashidsConvert converter = methodParameter.getParameterAnnotation(HashidsConvert.class);
        //参数值转换
        return HashidsUtil.convertId(methodParameter.getParameterType(), HashidsUtil.decode(value, converter.salt(), converter.minHashLength()));
    }

}
