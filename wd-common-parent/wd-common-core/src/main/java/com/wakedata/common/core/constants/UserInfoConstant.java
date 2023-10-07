package com.wakedata.common.core.constants;

/**
 * 用户信息相关常量
 * @author luomeng
 * @date 2022/6/27 10:25
 */
public interface UserInfoConstant {

    /**
     * WEB端身份认证key
     */
    String COOKIE_SESSION_KEY = "wdjsid";


    /**
     * 权限后台web端身份认证key
     */
    String COOKIE_MANAGE_SESSION_KEY = "dpjsid";

    /**
     * app端身份认证key
     */
    String APP_SESSION_KEY = "sessionId";

    /**
     * app端员工身份认证key
     */
    String APP_EMPLOYEE_SESSION_KEY = "appEmployeeSid";

    /**
     * app管理端身份认证key
     */
    String APP_MANAGE_KEY = "appManageSid";

    /**
     * 登录账号id
     */
    String LOGIN_USER_ID = "loginUserId";

    /**
     * web端身份信息缓存key前缀
     */
    String REDIS_SESSION_PREFIX = "US_";

    /**
     * 权限后台缓存key
     */
    String REDIS_MANAGE_SESSION_PREFIX = "SS_";

    /**
     * app端员工身份信息缓存key前缀
     */
    String APP_EMPLOYEE_REDIS_SESSION_PREFIX = "APP_EMPLOYEE_";

    /**
     * app管理端身份信息缓存key前缀
     */
    String APP_MANAGE_REDIS_PREFIX = "APP_M_";

    /**
     * web端session缓存时间
     */
    int REDIS_SESSION_EXPIRE_TIME = 30 * 60;

    /**
     * app端session缓存时间
     */
    int APP_REDIS_SESSION_EXPIRE_TIME = 30 * 60;

    /**
     * 权限后台session缓存时间
     */
    int MANAGE_REDIS_SESSION_EXPIRE_TIME = 30 * 60;

    /**
     * app端员工session缓存时间
     */
    int APP_EMPLOYEE_REDIS_SESSION_EXPIRE_TIME = 30 * 60;

    /**
     * token验证头信息
     */
    String WD_OAUTH_TOKEN_KEY = "wd-oauth";

    /**
     * license信息缓存key
     */
    String WD_LICENSE_INFO_KEY = "wdCloud:licenseInfo";



}
