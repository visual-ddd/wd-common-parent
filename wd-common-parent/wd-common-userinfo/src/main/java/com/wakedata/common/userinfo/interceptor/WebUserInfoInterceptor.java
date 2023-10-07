package com.wakedata.common.userinfo.interceptor;


import com.wakedata.common.core.context.UserInfoContext;
import com.wakedata.common.core.context.UserInfoWrapper;
import com.wakedata.common.userinfo.UserHelper;
import com.wakedata.common.userinfo.session.SessionHelper;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangcan
 * @date 2021/9/23 15:08
 */
@Slf4j
public class WebUserInfoInterceptor extends AbstractUserInfoInterceptor {

    @Override
    public boolean setUserToContext(HttpServletRequest request) {
        String sessionId = SessionHelper.getSessionId(request);
        UserInfoWrapper userinfoWrapper = UserHelper.getUserInfo(sessionId);
        if (Objects.isNull(userinfoWrapper)) {
            log.warn("get web userInfo is null, UserInfoContext set null, req uri={}", request.getRequestURI());
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
