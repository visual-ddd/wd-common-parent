package com.wakedata.common.feign;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 拦截器配置
 *
 * @author chenshaopeng
 * @date 2022/2/15
 */
@Configuration
@Import({OpenFeignInterceptor.class})
public class FeignInterceptorConfig implements WebMvcConfigurer {

    @Resource
    private OpenFeignInterceptor openFeignInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册rpc端拦截器
        registry.addInterceptor(openFeignInterceptor).addPathPatterns("/rpc/**");
    }

}
