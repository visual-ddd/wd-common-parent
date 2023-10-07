package com.wakedata.common.redis.lock;
import com.wakedata.common.redis.lock.config.LockConfig;
import com.wakedata.common.redis.lock.core.BusinessKeyProvider;
import com.wakedata.common.redis.lock.core.LockInfoProvider;
import com.wakedata.common.redis.lock.core.RedisLockAspectHandler;
import com.wakedata.common.redis.lock.lock.LockFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 适用于内部低版本spring mvc项目配置,redisson外化配置
 *
 * @author hhf
 * @date 2020/12/28
 */
@Configuration
@Import({RedisLockAspectHandler.class})
public class RedisLockConfiguration {
    @Bean
    public LockInfoProvider lockInfoProvider() {
        return new LockInfoProvider();
    }

    @Bean
    public BusinessKeyProvider businessKeyProvider() {
        return new BusinessKeyProvider();
    }

    @Bean
    public LockFactory lockFactory() {
        return new LockFactory();
    }

    @Bean
    public LockConfig lockConfig() {
        return new LockConfig();
    }
}
