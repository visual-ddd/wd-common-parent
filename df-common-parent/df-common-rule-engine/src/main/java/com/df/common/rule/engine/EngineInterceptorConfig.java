package com.df.common.rule.engine;

import com.df.common.rule.engine.core.EngineRuleExecutorService;
import com.df.common.rule.engine.filter.EngineRuleFilter;
import com.df.common.rule.engine.core.EngineParametersInterceptor;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author focus
 */
@Configuration
@Import({ EngineParametersInterceptor.class,
          EngineRuleFilter.class,
          EngineRuleExecutorService.class})
@Slf4j
public class EngineInterceptorConfig implements WebMvcConfigurer {

    @Resource
    private EngineParametersInterceptor appManageUserInfoInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(appManageUserInfoInterceptor).addPathPatterns("/**");
        registry.addInterceptor(appManageUserInfoInterceptor).addPathPatterns("/EngineRule/**");
    }

    @Bean
    public FilterRegistrationBean<EngineRuleFilter> registrationBean() {
        FilterRegistrationBean<EngineRuleFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new EngineRuleFilter());
//        registration.addUrlPatterns("/*");
        registration.addUrlPatterns("/EngineRule/*");
        // 设置过滤器的顺序
        registration.setOrder(1);
        return registration;
    }
}
