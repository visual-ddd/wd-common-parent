package com.wakedata.common.core.context;


public class ManageUserInfoContext {

    private static final ThreadLocal<ManageUserInfo> USER_INFO_THREAD_LOCAL = new ThreadLocal<>();


    /**
     * 获取权限后台用户信息
     * @return
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T extends ManageUserInfo> T getUser() {
        return (T) USER_INFO_THREAD_LOCAL.get();
    }

    /**
     * 添加用户信息
     * @param user
     * @param <T>
     */
    public static <T extends ManageUserInfo> void setUser(T user) {
        USER_INFO_THREAD_LOCAL.set(user);
    }

    /**
     * 移除用户信息
     */
    public static void removeUser() {
        USER_INFO_THREAD_LOCAL.remove();
    }
}
