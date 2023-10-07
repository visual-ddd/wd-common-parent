package com.wakedata.common.redis.lock.lock;

import com.wakedata.common.redis.lock.module.LockInfo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 公平锁实现
 *
 * @author hhf
 * @date 2020/12/25
 */
public class FairLock implements Lock {

    private RLock lock;

    private final LockInfo lockInfo;

    private RedissonClient redissonClient;

    public FairLock(RedissonClient redissonClient, LockInfo info) {
        this.redissonClient = redissonClient;
        this.lockInfo = info;
    }

    @Override
    public boolean acquire() {
        try {
            lock = redissonClient.getFairLock(lockInfo.getName());
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
}
