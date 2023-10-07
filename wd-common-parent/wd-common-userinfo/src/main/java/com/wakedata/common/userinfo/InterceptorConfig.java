package com.wakedata.common.userinfo;

import com.wakedata.common.userinfo.interceptor.*;

import javax.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author yixiaonan
 * @date 2022/1/24
 */
@Configuration
@Import({ WebUserInfoInterceptor.class, AppManageUserInfoInterceptor.class})
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * app端员工的接口路径前缀
     */
    private static final String APP_STAFF_INTERCEPT_URI = "/app/staff/**";

    @Resource
    private WebUserInfoInterceptor webUserInfoInterceptor;

    @Resource
    private AppManageUserInfoInterceptor appManageUserInfoInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //pc端拦截器
        registry.addInterceptor(webUserInfoInterceptor).addPathPatterns("/web/**");
        //app管理端员工拦截器
        registry.addInterceptor(appManageUserInfoInterceptor).addPathPatterns("/appm/**");

    }
}
