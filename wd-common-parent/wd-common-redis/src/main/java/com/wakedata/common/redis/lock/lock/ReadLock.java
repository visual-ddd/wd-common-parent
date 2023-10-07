package com.wakedata.common.redis.lock.lock;

import com.wakedata.common.redis.lock.module.LockInfo;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 读锁
 *
 * @author hhf
 * @date 2020/12/25
 */
public class ReadLock implements Lock {

    private RReadWriteLock lock;

    private final LockInfo lockInfo;

    private RedissonClient redissonClient;

    public ReadLock(RedissonClient redissonClient, LockInfo info) {
        this.redissonClient = redissonClient;
        this.lockInfo = info;
    }

    @Override
    public boolean acquire() {

        try {
            lock = redissonClient.getReadWriteLock(lockInfo.getName());
            return lock.readLock().tryLock(lockInfo.getWaitTime(), lockInfo.getLeaseTime(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public boolean release() {
        if (lock.readLock().isHeldByCurrentThread()) {
            try {
                return lock.readLock().forceUnlockAsync().get();
            } catch (InterruptedException | ExecutionException e) {
                return false;
            }
        }

        return false;
    }
}
