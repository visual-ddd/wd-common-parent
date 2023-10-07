package com.wakedata.common.core.util;


import com.wakedata.common.core.dto.ResultDTO;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * web 包的工具类
 *
 * @author neexz
 * @date 2021/05/28
 */
public class WebFrameworkUtils {

    private static final String REQUEST_ATTRIBUTE_LOGIN_USER_ID = "login_user_id";
    private static final String REQUEST_ATTRIBUTE_RESULT_DTO = "result_dto";

    public static void setLoginUserId(ServletRequest request, Long userId) {
        request.setAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_ID, userId);
    }

    /**
     * 获得当前用户的编号，从请求中
     *
     * @param request 请求
     * @return 用户编号
     */
    public static Long getLoginUserId(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return (Long) request.getAttribute(REQUEST_ATTRIBUTE_LOGIN_USER_ID);
    }

    public static Long getLoginUserId() {
        HttpServletRequest request = getRequest();
        return getLoginUserId(request);
    }

    public static void setCommonResult(ServletRequest request, ResultDTO<?> result) {
        request.setAttribute(REQUEST_ATTRIBUTE_RESULT_DTO, result);
    }

    public static ResultDTO<?> getCommonResult(ServletRequest request) {
        return (ResultDTO<?>) request.getAttribute(REQUEST_ATTRIBUTE_RESULT_DTO);
    }

    private static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return null;
        }
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        return servletRequestAttributes.getRequest();
    }

}
