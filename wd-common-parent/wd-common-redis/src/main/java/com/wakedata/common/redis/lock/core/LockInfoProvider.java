package com.wakedata.common.redis.lock.core;

import com.wakedata.common.redis.lock.annotation.RedisLock;
import com.wakedata.common.redis.lock.config.LockConfig;
import com.wakedata.common.redis.lock.module.LockInfo;
import com.wakedata.common.redis.lock.module.LockType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;


/**
 * LockInfo解析
 *
 * @author hhf
 * @date 2020/12/28
 */
public class LockInfoProvider {

    private static final String LOCK_NAME_PREFIX = "lock";
    private static final String LOCK_NAME_SEPARATOR = ".";

    @Autowired
    private LockConfig lockConfig;

    @Autowired
    private BusinessKeyProvider businessKeyProvider;

    private static final Logger logger = LoggerFactory.getLogger(LockInfoProvider.class);

    LockInfo get(JoinPoint joinPoint, RedisLock redisLock) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        LockType type = redisLock.lockType();
        String businessKeyName = businessKeyProvider.getKeyName(joinPoint, redisLock);
        //锁的名字，锁的粒度就是这里控制的
        String lockName = LOCK_NAME_PREFIX + LOCK_NAME_SEPARATOR + getName(redisLock.name(), signature) + businessKeyName;
        long waitTime = getWaitTime(redisLock);
        long leaseTime = getLeaseTime(redisLock);
        //如果占用锁的时间设计不合理，则打印相应的警告提示
        if (leaseTime == -1) {
            logger.warn("Trying to acquire Lock({}) with no expiration, " +
                    "RedisLock will keep prolong the lock expiration while the lock is still holding by current thread. " +
                    "This may cause dead lock in some circumstances.", lockName);
        }
        return new LockInfo(type, lockName, waitTime, leaseTime);
    }

    /**
     * 获取锁的name，如果没有指定，则按全类名拼接方法名处理
     *
     * @param annotationName
     * @param signature
     * @return
     */
    private String getName(String annotationName, MethodSignature signature) {
        if (StringUtils.isEmpty(annotationName)) {
            return String.format("%s.%s", signature.getDeclaringTypeName(), signature.getMethod().getName());
        } else {
            return annotationName;
        }
    }


    private long getWaitTime(RedisLock lock) {
        return lock.waitTime() == Long.MIN_VALUE ?
                lockConfig.getWaitTime() : lock.waitTime();
    }

    private long getLeaseTime(RedisLock lock) {
        return lock.leaseTime() == Long.MIN_VALUE ?
                lockConfig.getLeaseTime() : lock.leaseTime();
    }
}