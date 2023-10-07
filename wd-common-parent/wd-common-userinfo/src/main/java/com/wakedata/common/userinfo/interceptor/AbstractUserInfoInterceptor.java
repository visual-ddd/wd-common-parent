package com.wakedata.common.userinfo.interceptor;


import com.wakedata.common.core.context.UserInfoContext;
import com.wakedata.common.userinfo.Tmgmt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author wangcan
 * @date 2021/9/23 15:08
 */
@Slf4j
public abstract class AbstractUserInfoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            Method method = ((HandlerMethod) handler).getMethod();
            Tmgmt annotation = method.getAnnotation(Tmgmt.class);
            if (Objects.nonNull(annotation) && annotation.isAdmin()) {
                log.info("管理员接口 不需要处理sesssion");
                return annotation.isAdmin();
            }
        }
        return setUserToContext(request);
    }

    /**
     * 设置用户信息到上下文中
     *
     * @param request 请求
     * @return 设置结果，默认为true
     */
    public abstract boolean setUserToContext(HttpServletRequest request);


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {

        removeUserInfoContext();
    }


    /**
     * 移除用户上下文
     * @return
     */
    public abstract void removeUserInfoContext();

}
