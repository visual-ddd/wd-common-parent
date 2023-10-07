package com.wakedata.common;

import com.wakedata.common.userinfo.session.SessionHelper;

/**
 * @author: hhf
 * @date: 2021/12/7
 **/
public class Test {

    public static void main(String[] args) {
        String s = SessionHelper.COOKIE_SESSION_KEY;
        System.out.println("s = " + s);
        System.out.println("SessionHelper.getAppEmployeeSessionId() = " + SessionHelper.getAppEmployeeSessionRedisKey(s));
    }
}
