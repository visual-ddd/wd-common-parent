package com.wakedata.common.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;

/**
 * @author hhf
 * @date 2021/12/21
 */
@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan(excludeFilters =
        {
                @ComponentScan.Filter(type = FilterType.REGEX,pattern = "com.wakedata.common.redis.jedis.*")
        })
public class RedisTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisTestApplication.class, args);
    }

}
