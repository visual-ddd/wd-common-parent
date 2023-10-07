package com.wakedata.common.userinfo.session;

import com.wakedata.common.core.constants.UserInfoConstant;
import com.wakedata.common.redis.util.RedisUtil;
import java.util.Objects;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Session工具类，根据request获取session信息
 *
 * @author chenshaopeng
 * @date 2021/9/23
 */
@UtilityClass
public class SessionHelper implements UserInfoConstant {

    /**
     * 获取sessionId
     *
     * @param request 请求对象
     * @return sessionId
     */
    public String getSessionId(ServerHttpRequest request) {
        return getCookieValueByName(request, COOKIE_SESSION_KEY);
    }

    /**
     * 获取cookie的值
     *
     * @param request    请求
     * @param cookieName cookie的名称
     * @return cookie的值
     */
    private static String getCookieValueByName(ServerHttpRequest request, String cookieName) {
        HttpCookie httpCookie = request.getCookies().getFirst(cookieName);
        return Objects.nonNull(httpCookie) ? httpCookie.getValue() : null;
    }

    /**
     * 获取app管理端redis id
     * @param request
     * @return
     */
    public static String getAppManageSid(ServerHttpRequest request){
        return request.getHeaders().getFirst(APP_MANAGE_KEY);
    }

    /**
     * 获取app管理端redis id
     * @param request
     * @return
     */
    public static String getAppManageSid(HttpServletRequest request){
        return request.getHeader(APP_MANAGE_KEY);
    }


    /**
     * 获取sessionId
     *
     * @param request 请求对象
     * @return sessionId
     */
    public String getAppSessionId(ServerHttpRequest request) {
        String sessionId = request.getHeaders().getFirst(APP_SESSION_KEY);
        return StringUtils.isEmpty(sessionId) ? getCookieValueByName(request, APP_SESSION_KEY) : null;
    }

    /**
     * 获取sessionId
     *
     * @param request 请求对象
     * @return sessionId
     */
    public String getSessionId(HttpServletRequest request) {
        return getCookieValueByName(request, COOKIE_SESSION_KEY);
    }


    /**
     * 权限后台sessionId
     * @param request
     * @return
     */
    public String getManageSessionId(ServerHttpRequest request) {
        return getCookieValueByName(request, COOKIE_MANAGE_SESSION_KEY);
    }

    /**
     * 权限后台sessionId
     *
     * @param request 请求对象
     * @return sessionId
     */
    public String getManageSessionId(HttpServletRequest request) {
        return getCookieValueByName(request, COOKIE_MANAGE_SESSION_KEY);
    }


    /**
     * 获取cookie的值
     *
     * @param request    请求
     * @param cookieName cookie的名称
     * @return cookie的值
     */
    private static String getCookieValueByName(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (Objects.nonNull(cookies)) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 获取C端sessionId
     *
     * @param request 请求对象
     * @return sessionId
     */
    public String getAppSessionId(HttpServletRequest request) {
        String sessionId = request.getParameter(APP_SESSION_KEY);
        return StringUtils.isNotBlank(sessionId) ? sessionId : getCookieValueByName(request, APP_SESSION_KEY);
    }

    /**
     * 获取sessionId
     *
     * @return sessionId
     */
    public String getSessionId() {
        return getSessionId(((ServletRequestAttributes) Objects
            .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest());
    }

    /**
     * 获取app员工sessionId
     *
     * @return sessionId
     */
    public String getAppEmployeeSessionId() {
        return getAppEmployeeSessionId(((ServletRequestAttributes) Objects
            .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest());
    }

    /**
     * 刷新用户信息过期时间
     *
     * @param sessionId sessionId
     * @return 成功返回true
     */
    public boolean refreshLeaseTime(String sessionId) {
        return RedisUtil.getInstance().expire(REDIS_SESSION_PREFIX + sessionId, REDIS_SESSION_EXPIRE_TIME);
    }

    /**
     * 刷新c端用户信息过期时间
     *
     * @param sessionId sessionId
     * @return 成功返回true
     */
    public boolean refreshAppLeaseTime(String sessionId) {
        return RedisUtil.getInstance().expire(sessionId, APP_REDIS_SESSION_EXPIRE_TIME);
    }

    /**
     * 获取c端员工sessionId
     * @param request
     * @return
     */
    public String getAppEmployeeSessionId(HttpServletRequest request) {
        return getSessionId(request, APP_EMPLOYEE_SESSION_KEY);
    }

    /**
     * 刷新c端员工信息过期时间
     * @param sessionId
     * @return
     */
    public boolean refreshAppEmployeeLeaseTime(String sessionId) {
        return RedisUtil.getInstance().expire(getAppEmployeeSessionRedisKey(sessionId), APP_EMPLOYEE_REDIS_SESSION_EXPIRE_TIME);
    }

    /**
     * 获取c端员工信息缓存key
     * @param sessionId
     * @return
     */
    public String getAppEmployeeSessionRedisKey(String sessionId){
        return APP_EMPLOYEE_REDIS_SESSION_PREFIX + sessionId;
    }

    /**
     * 获取sessionId
     * @param request
     * @param sessionKey
     * @return
     */
    public String getSessionId(HttpServletRequest request,String sessionKey){
        String sessionId = request.getParameter(sessionKey);
        if (StringUtils.isNotBlank(sessionId)) {
            return sessionId;
        }
        return getCookieValueByName(request,sessionKey);
    }

}
