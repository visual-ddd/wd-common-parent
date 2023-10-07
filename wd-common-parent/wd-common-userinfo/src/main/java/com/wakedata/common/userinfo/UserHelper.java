package com.wakedata.common.userinfo;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wakedata.common.core.GlobalApplicationContext;
import com.wakedata.common.core.context.*;
import com.wakedata.common.redis.lock.lock.Lock;
import com.wakedata.common.redis.lock.lock.LockFactory;
import com.wakedata.common.redis.lock.module.LockInfo;
import com.wakedata.common.redis.lock.module.LockType;
import com.wakedata.common.redis.util.RedisUtil;
import com.wakedata.common.userinfo.session.SessionHelper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.http.server.reactive.ServerHttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 用户信息工具类 - 提供多种方式获取用户信息
 *
 * @author chenshaopeng
 * @date 2021/9/9
 */
@Slf4j
@UtilityClass
public class UserHelper {

    /**
     * 当前用户有权限的buId
     */
    private static final String HAS_PERMISSION_BU_ID_LIST = "HAS_PERMISSION_BU_ID_LIST_%s";


    /**
     * 当前用户有权限管辖的所有门店业务单元id
     */
    private static final String HAS_PERMISSION_STORE_BU_ID_LIST = "HAS_PERMISSION_STORE_BU_ID_LIST_%s_%s";

    /**
     * 默认系统用户id
     */
    private static final Long DEFAULT_SYS_ID = -1L;

    /**
     * 无参获取用户信息 需要注意：必须在spring上下文中才可获取信息 对于非spring上下文获取可能会出现空指针异常
     *
     * @return 用户信息对象
     */
    public UserInfoWrapper getUserInfo() {
        try {
            return Optional.ofNullable(SessionHelper.getSessionId())
                .map(sessionId -> RedisUtil.getInstance().getObject(
                    SessionHelper.REDIS_SESSION_PREFIX + sessionId, UserInfoWrapper.class))
                .orElse(null);
        } catch (Exception ex) {
            log.error("Failed to obtain user information!", ex);
            return null;
        }
    }

    /**
     * 获取用户信息
     *
     * @param sessionId session id
     * @return 用户信息
     */
    public UserInfoWrapper getUserInfo(String sessionId) {
        try {
            return Optional.ofNullable(sessionId)
                .map(curSessionId -> RedisUtil.getInstance().getObject(
                    SessionHelper.REDIS_SESSION_PREFIX + curSessionId, UserInfoWrapper.class))
                .orElse(null);
        } catch (Exception ex) {
            log.error("Failed to obtain user information!", ex);
            return null;
        }
    }


    /**
     * 获取app管理端的用户信息
     * @param appManageSid
     * @return
     */
    public UserInfoWrapper getAppMangeUserInfo(String appManageSid) {
        try {
            return Optional.ofNullable(appManageSid)
                    .map(curSessionId -> RedisUtil.getInstance().getObject(
                            SessionHelper.APP_MANAGE_REDIS_PREFIX + curSessionId, UserInfoWrapper.class))
                    .orElse(null);
        } catch (Exception ex) {
            log.error("Failed to obtain user information!", ex);
            return null;
        }
    }


    /**
     * 获取C端用户信息
     *
     * @param sessionId session id
     * @return 用户信息
     */
    public MemberInfoWrapper getAppUserInfo(String sessionId) {
        return Optional.ofNullable(sessionId)
            .map(curSessionId -> {
                Object obj = RedisUtil.getInstance().getToObject(curSessionId, Object.class);
                if (Objects.isNull(obj)) {
                    return null;
                }
                // 因为set到redis的是带有对象描述符的字符串，导致反序列化时找不到对应的类
                // 这里做个操作将对象描述符截断后，用string的方式反序列化对象
                MemberInfoWrapper result;
                if (obj instanceof String) {
                    String jsonStr = (String) obj;
                    result = JSON.parseObject(jsonStr, MemberInfoWrapper.class);
                } else {
                    result = JSON.parseObject(JSON.toJSONString(obj), MemberInfoWrapper.class);
                }

                return result;
            }).orElse(null);

    }


    /**
     * 设置权限后台用户信息
     * @param user
     * @param sessionId
     * @return
     */
    public static boolean setManageUserInfo(ManageUserInfo user, String sessionId) {
        boolean result = RedisUtil.getInstance().setObject(
                SessionHelper.REDIS_MANAGE_SESSION_PREFIX + sessionId, JSONObject.toJSONString(user), SessionHelper.MANAGE_REDIS_SESSION_EXPIRE_TIME);
        if (result) {
            ManageUserInfoContext.setUser(user);
        }
        return result;
    }

    /**
     * 获取权限后台用户信息
     * @param request
     * @return
     */
    public static ManageUserInfo getManageUserInfo(HttpServletRequest request) {
        String sessionId = SessionHelper.getManageSessionId(request);
        return getManageUserInfo(sessionId);
    }

    /**
     * 获取权限后台用户信息
     * @param sessionId
     * @return
     */
    public static ManageUserInfo getManageUserInfo(String sessionId) {
        return Optional.ofNullable(sessionId)
                .map(curSessionId -> {
                    Object obj = RedisUtil.getInstance().getToObject(SessionHelper.REDIS_MANAGE_SESSION_PREFIX + curSessionId, Object.class);
                    if (Objects.isNull(obj)) {
                        return null;
                    }
                    // 因为set到redis的是带有对象描述符的字符串，导致反序列化时找不到对应的类
                    // 这里做个操作将对象描述符截断后，用string的方式反序列化对象
                    ManageUserInfo result;
                    if (obj instanceof String) {
                        String jsonStr = (String) obj;
                        result = JSON.parseObject(jsonStr, ManageUserInfo.class);
                    } else {
                        result = JSON.parseObject(JSON.toJSONString(obj), ManageUserInfo.class);
                    }
                    return result;
                }).orElse(null);
    }


    /**
     * 根据request获取用户信息
     *
     * @param request 请求对象
     * @return 用户信息对象
     */
    public UserInfoWrapper getUserInfo(ServerHttpRequest request) {
        return getUserInfo(SessionHelper.getSessionId(request));
    }

    /**
     * 根据request获取用户信息
     *
     * @param request 请求对象
     * @return 用户信息对象
     */
    public UserInfoWrapper getUserInfo(HttpServletRequest request) {
        return getUserInfo(SessionHelper.getSessionId(request));
    }

    /**
     * 根据request获取c端用户信息
     *
     * @param request 请求对象
     * @return 用户信息对象
     */
    public MemberInfoWrapper getAppUserInfo(HttpServletRequest request) {
        return getAppUserInfo(SessionHelper.getAppSessionId(request));
    }

    /**
     * 设置用户信息(要添加分布式锁配置, 否则会保错)
     *
     * @param newUserInfo 用户信息
     * @param sessionId   sessionId
     * @return 设置成功返回true
     */
    public boolean setUserInfo(UserInfoWrapper newUserInfo, String sessionId) {
        return setUserInfo(newUserInfo,sessionId, (long) SessionHelper.REDIS_SESSION_EXPIRE_TIME,true);
    }

    /**
     * 设置app管理端用户信息
     * @param newUserInfo
     * @param sessionId
     * @param expireTime
     * @return
     */
    public boolean setAppMangeUserInfo(UserInfoWrapper newUserInfo, String sessionId,Long expireTime) {
        return setUserInfo(newUserInfo,sessionId, expireTime,false);
    }

    private boolean setUserInfo(UserInfoWrapper newUserInfo, String sessionId,Long expireTime,boolean isWeb) {
        Validate.notNull(newUserInfo, "The userInfo must not be null");
        Validate.notBlank(sessionId, "The sessionId must not be blank");

        LockFactory lockFactory = GlobalApplicationContext.getBean(LockFactory.class);
        Validate.notNull(lockFactory, "The lockFactory must not be null");

        String redisPrefix = isWeb ? SessionHelper.REDIS_SESSION_PREFIX : SessionHelper.APP_MANAGE_REDIS_PREFIX ;

        Lock lock = lockFactory.getLock(new LockInfo(LockType.Write, redisPrefix + sessionId, 3000, 100));
        try {
            //加锁失败
            if (!lock.acquire()) {
                if (log.isWarnEnabled()) {
                    log.warn("Timeout while acquiring set user info Lock");
                }
                return false;
            }
            //先从redis获取旧的用户信息
            Optional.ofNullable((isWeb ? getUserInfo(sessionId) : getAppMangeUserInfo(sessionId))).ifPresent(oldUser -> {
                //旧的other用户信息不为空,
                if (CollUtil.isNotEmpty(oldUser.getOther())) {
                    //新的other用户信息不为空，则合并用户的other信息
                    if (CollUtil.isNotEmpty(newUserInfo.getOther())) {
                        oldUser.getOther().putAll(newUserInfo.getOther());
                    }
                    newUserInfo.setOther(oldUser.getOther());
                }
            });
            //保存用户信息到redis
            boolean success = RedisUtil.getInstance().set(
                        redisPrefix + sessionId,
                            JSON.toJSONString(newUserInfo)
                            ,expireTime);
            //保存用户信息成功则设置用户信息到上下文
            if (success) {
                UserInfoContext.setUser(newUserInfo);
            }
            return success;
        } finally {
            lock.release();
        }
    }

    /**
     * 设置C端用户信息
     *
     * @param user 用户信息
     * @return 成功返回true
     */
    public static boolean setAppUserInfo(MemberInfoWrapper user, String sessionId) {
        boolean result = RedisUtil.getInstance().setObject(
            sessionId, JSONObject.toJSONString(user), SessionHelper.APP_REDIS_SESSION_EXPIRE_TIME);
        if (result) {
            UserInfoContext.setUser(user);
        }
        return result;
    }

    /**
     * 设置用户信息
     *
     * @param userInfo 用户信息
     * @return 成功返回true
     */
    public boolean setUserInfo(UserInfoWrapper userInfo) {
        return setUserInfo(userInfo, SessionHelper.getSessionId());
    }

    /**
     * 设置有权限的业务单元id
     *
     * @param permissionBuIds 权限业务单元列表
     * @param employeeId      员工id
     * @return 成功返回true
     */
    public boolean setPermissionBuIds(Set<Long> permissionBuIds, Long employeeId) {
        Objects.requireNonNull(employeeId, "employeeId not null");

        String key = String.format(HAS_PERMISSION_BU_ID_LIST, employeeId);
        return RedisUtil.getInstance().set(
            key, JSON.toJSONString(permissionBuIds), SessionHelper.REDIS_SESSION_EXPIRE_TIME);
    }

    /**
     * 获取有权限的业务单元id
     *
     * @param employeeId 员工id
     * @return 列表
     */
    public List<Long> getPermissionBuIds(Long employeeId) {
        if (Objects.isNull(employeeId)) {
            log.error("get permissionBuIds employeeId is null");
            return Collections.emptyList();
        }

        String key = String.format(HAS_PERMISSION_BU_ID_LIST, employeeId);
        return RedisUtil.getInstance().getList(key, Long.class);
    }

    /**
     * 刷新权限的业务单元id过期时间
     *
     * @param employeeId 员工id
     * @return 成功返回true
     */
    public boolean refreshPerBuIdsLeaseTime(Long employeeId) {
        if (Objects.isNull(employeeId)) {
            return false;
        }
        String key = String.format(HAS_PERMISSION_BU_ID_LIST, employeeId);
        return RedisUtil.getInstance().expire(key, SessionHelper.REDIS_SESSION_EXPIRE_TIME);
    }

    /**
     * 设置用户有权限管辖的所有门店业务单元id
     *
     * @param leafBuIdSet 权限业务单元列表
     * @param employeeId  员工id
     * @return 成功返回true
     */
    public boolean setAllPermissionStoreBuIds(Set<Long> leafBuIdSet, Long employeeId) {
        Objects.requireNonNull(employeeId, "employeeId not null");

        return Optional.ofNullable(UserInfoContext.getUser()).map(user -> {
            String key = String.format(HAS_PERMISSION_STORE_BU_ID_LIST, ((BaseUserInfo) user).getAppBuId(), employeeId);
            return RedisUtil.getInstance().set(
                key, JSON.toJSONString(leafBuIdSet), SessionHelper.REDIS_SESSION_EXPIRE_TIME);
        }).orElse(false);
    }

    /**
     * 获取用户有权限管辖的所有门店业务单元id
     *
     * @param employeeId 员工id
     * @return 列表
     */
    public List<Long> getAllPermissionStoreBuIds(Long employeeId) {
        if (Objects.isNull(employeeId)) {
            log.error("get permissionBuIds employeeId is null");
            return Collections.emptyList();
        }

        BaseUserInfo user = UserInfoContext.getUser();
        Long appBuId = Objects.nonNull(user) && Objects.nonNull(user.getAppBuId()) ? user.getAppBuId() : -1L;
        String key = String.format(HAS_PERMISSION_STORE_BU_ID_LIST, appBuId, employeeId);
        log.info("get permission buIds key={}", key);

        return RedisUtil.getInstance().getList(key, Long.class);
    }

    /**
     * 刷新用户有权限管辖的所有门店业务单元id过期时间
     *
     * @param employeeId 员工id
     * @param appBuId    appBuId
     * @return 成功返回true
     */
    public boolean refreshAllStoreBuIdsLeaseTime(Long employeeId, Long appBuId) {
        if (Objects.isNull(employeeId) || Objects.isNull(appBuId)) {
            return false;
        }
        String key = String.format(HAS_PERMISSION_STORE_BU_ID_LIST, appBuId, employeeId);
        return RedisUtil.getInstance().expire(key, SessionHelper.REDIS_SESSION_EXPIRE_TIME);
    }

    /**
     * 创建系统用户
     *
     * @return 系统用户
     */
    public static BaseUserInfo createSysUser() {
        return BaseUserInfo.builder().userId(DEFAULT_SYS_ID.toString())
            .buId(DEFAULT_SYS_ID).appBuId(DEFAULT_SYS_ID).tenantId(DEFAULT_SYS_ID).build();
    }

    /**
     * 获取c端员工信息
     * @param sessionId
     * @return
     */
    public EmployeeInfoWrapper getAppEmployeeInfo(String sessionId) {
        try {
            return Optional.ofNullable(sessionId)
                    .map(curSessionId -> RedisUtil.getInstance().getObject(
                            SessionHelper.getAppEmployeeSessionRedisKey(curSessionId), EmployeeInfoWrapper.class))
                    .orElse(null);
        } catch (Exception ex) {
            log.error("Failed to obtain user information!", ex);
            return null;
        }
    }

    /**
     * 设置C端员工信息
     *
     * @param user 用户信息
     * @return 成功返回true
     */
    public static boolean setAppEmployeeInfo(EmployeeInfoWrapper user, String sessionId) {
        boolean result = RedisUtil.getInstance().set(
                SessionHelper.getAppEmployeeSessionRedisKey(sessionId)
                , JSON.toJSONString(user), SessionHelper.APP_EMPLOYEE_REDIS_SESSION_EXPIRE_TIME);
        if (result) {
            UserInfoContext.setUser(user);
        }
        return result;
    }

}
