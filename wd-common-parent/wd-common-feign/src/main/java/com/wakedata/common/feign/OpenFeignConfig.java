package com.wakedata.common.feign;

import feign.Logger;
import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * Feign全局配置
 *
 * @author wangcan
 * @date 2022/1/5 11:17
 */
@Import(FeignInterceptorConfig.class)
public class OpenFeignConfig {

    @Value("${feign.pool.readTimeOut:10000}")
    private int readTimeOut;

    @Value("${feign.pool.connectTimeOut:10000}")
    private int connectTimeOut;

    @Value("${feign.pool.writeTimeOut:10000}")
    private int writeTimeOut;

    @Value("${feign.pool.maxIdleConnectionPool:20}")
    private int maxIdleConnectionPool;

    @Value("${feign.pool.keepAliveDuration:20}")
    private Long keepAliveDuration;


    @Bean
    Logger.Level feignLoggerLeave() {
        return Logger.Level.FULL;
    }

    @Bean
    public okhttp3.OkHttpClient okHttpClient() {
        return new okhttp3.OkHttpClient.Builder()
            .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
            .connectTimeout(connectTimeOut, TimeUnit.MILLISECONDS)
            .writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS)
            .connectionPool(new ConnectionPool(maxIdleConnectionPool, keepAliveDuration, TimeUnit.MINUTES))
            .build();
    }
}
