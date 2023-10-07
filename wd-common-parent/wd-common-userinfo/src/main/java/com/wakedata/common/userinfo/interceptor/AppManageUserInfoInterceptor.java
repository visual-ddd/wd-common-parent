package com.wakedata.common.userinfo.interceptor;


import com.wakedata.common.core.context.UserInfoContext;
import com.wakedata.common.core.context.UserInfoWrapper;
import com.wakedata.common.userinfo.UserHelper;
import com.wakedata.common.userinfo.session.SessionHelper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * app管理端用户信息填充
 * @author wangcan
 * @date 2021/9/23 15:08
 */
@Slf4j
public class AppManageUserInfoInterceptor extends AbstractUserInfoInterceptor {

    @Override
    public boolean setUserToContext(HttpServletRequest request) {
        String appManageSid = SessionHelper.getAppManageSid(request);
        UserInfoWrapper userinfoWrapper = UserHelper.getAppMangeUserInfo(appManageSid);
        if (Objects.isNull(userinfoWrapper)) {
            log.warn("get app manager userInfo is null, UserInfoContext set null, req uri={}", request.getRequestURI());
            return true;
        }
        UserInfoContext.setUser(userinfoWrapper);
        return true;
    }

    @Override
    public void removeUserInfoContext() {
        UserInfoContext.removeUserInfoContext();
    }

}
