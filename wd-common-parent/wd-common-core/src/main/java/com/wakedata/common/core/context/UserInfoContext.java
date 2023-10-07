package com.wakedata.common.core.context;

/**
 * @date 2022/1/4 15:27
 */
public class UserInfoContext {

    private static final ThreadLocal<BaseUserInfo> USER_INFO_THREAD_LOCAL = new ThreadLocal<>();


    public UserInfoContext() {
    }

    @SuppressWarnings("unchecked")
    public static <T extends BaseUserInfo> T getUser() {
        return (T) USER_INFO_THREAD_LOCAL.get();
    }

    public static <T extends BaseUserInfo> void setUser(T user) {
        USER_INFO_THREAD_LOCAL.set(user);
    }

    public static void removeUserInfoContext() {
        USER_INFO_THREAD_LOCAL.remove();
    }

}
