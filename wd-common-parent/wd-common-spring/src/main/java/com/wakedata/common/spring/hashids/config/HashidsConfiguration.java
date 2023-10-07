package com.wakedata.common.spring.hashids.config;

import com.wakedata.common.spring.hashids.HashidConvertParamArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author luomeng
 * @Description 添加hashids参数解析器
 * @createTime 2022-05-18 16:50:00
 */
@Configuration
public class HashidsConfiguration implements WebMvcConfigurer {

    @Bean
    HashidConvertParamArgumentResolver hashidConvertParamArgumentResolver(){
        return new HashidConvertParamArgumentResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(hashidConvertParamArgumentResolver());
    }
}
