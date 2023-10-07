package com.wakedata.common.spring;

import com.wakedata.common.spring.hashids.config.HashidsConfiguration;
import com.wakedata.common.spring.i18n.I18nDataCookieLocaleResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;

/**
 * @author hhf
 * @date 2021/12/21
 */
@SpringBootApplication
public class SpringTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringTestApplication.class, args);
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new I18nDataCookieLocaleResolver();
    }

    @Bean
    public HashidsConfiguration hashidsConfiguration(){
        return new HashidsConfiguration();
    }

}
