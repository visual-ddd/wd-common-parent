package com.wakedata.common.redis.lock;

import cn.hutool.core.util.StrUtil;

import com.wakedata.common.redis.lock.config.LockConfig;
import com.wakedata.common.redis.lock.core.BusinessKeyProvider;
import com.wakedata.common.redis.lock.core.LockInfoProvider;
import com.wakedata.common.redis.lock.core.RedisLockAspectHandler;
import com.wakedata.common.redis.lock.lock.LockFactory;
import io.netty.channel.nio.NioEventLoopGroup;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.ClassUtils;

/**
 * SpringBoot自动装配
 *
 * @author hhf
 * @date 2020/12/28
 */
@Configuration
@ConditionalOnProperty(prefix = LockConfig.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(LockConfig.class)
@Import({RedisLockAspectHandler.class})
public class RedisLockAutoConfiguration {

    @Autowired
    private LockConfig lockConfig;

    @Value("${redis.host:}")
    private String host;
    @Value("${redis.port:}")
    private String port;
    @Value("${redis.password:}")
    private String password;

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean
    RedissonClient redisson() throws Exception {
        String redisAddress = lockConfig.getAddress();
        String redisPass = lockConfig.getPassword();
        if (StrUtil.isBlank(redisAddress)) {
            redisAddress = "redis://"+host+":"+port;
        }
        if (StrUtil.isBlank(redisPass)) {
            redisPass = password;
        }

        Config config = new Config();
        if (lockConfig.getClusterServer() != null) {
            config.useClusterServers().setPassword(redisPass)
                    .addNodeAddress(lockConfig.getClusterServer().getNodeAddresses());
        } else {
            config.useSingleServer().setAddress(redisAddress)
                    .setDatabase(lockConfig.getDatabase())
                    .setPassword(redisPass);
        }
        Codec codec = (Codec) ClassUtils.forName(lockConfig.getCodec(), ClassUtils.getDefaultClassLoader()).newInstance();
        config.setCodec(codec);
        config.setEventLoopGroup(new NioEventLoopGroup());
        return Redisson.create(config);
    }

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
}
