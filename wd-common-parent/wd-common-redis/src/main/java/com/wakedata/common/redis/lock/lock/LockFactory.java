package com.wakedata.common.redis.lock.lock;

import com.wakedata.common.redis.lock.module.LockInfo;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 获取锁的工厂类
 *
 * @author hhf
 * @date 2020/12/28
 */
public class LockFactory {

    @Autowired
    private RedissonClient redissonClient;

    public Lock getLock(LockInfo lockInfo) {
        switch (lockInfo.getType()) {
            case Fair:
                return new FairLock(redissonClient, lockInfo);
            case Read:
                return new ReadLock(redissonClient, lockInfo);
            case Write:
                return new WriteLock(redissonClient, lockInfo);
            default:
                return new ReentrantLock(redissonClient, lockInfo);
        }
    }

}
