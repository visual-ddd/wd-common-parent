package com.wakedata.common.redis.lock.lock;

import com.wakedata.common.redis.lock.module.LockInfo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 可重入锁
 *
 * @author hhf
 * @date 2020/12/25
 */
public class ReentrantLock implements Lock {

    private RLock lock;

    private final LockInfo lockInfo;

    private RedissonClient redissonClient;

    public ReentrantLock(RedissonClient redissonClient, LockInfo lockInfo) {
        this.redissonClient = redissonClient;
        this.lockInfo = lockInfo;
    }

    @Override
    public boolean acquire() {
        try {
            lock = redissonClient.getLock(lockInfo.getName());
            return lock.tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public boolean release() {
        if (lock.isHeldByCurrentThread()) {
            try {
                return lock.forceUnlockAsync().get();
            } catch (InterruptedException | ExecutionException e) {
                return false;
            }
        }
        return false;
    }

    public String getKey() {
        return this.lockInfo.getName();
    }
}
