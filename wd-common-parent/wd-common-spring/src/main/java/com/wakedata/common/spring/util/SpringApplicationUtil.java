package com.wakedata.common.spring.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Spring工具类
 *
 * @author chenshaopeng
 * @date 2022/3/2
 */
public class SpringApplicationUtil {

    /**
     * 获取request
     *
     * @return request
     */
    public static HttpServletRequest getRequest(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if(Objects.isNull(requestAttributes)){
            return null;
        }
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

}
