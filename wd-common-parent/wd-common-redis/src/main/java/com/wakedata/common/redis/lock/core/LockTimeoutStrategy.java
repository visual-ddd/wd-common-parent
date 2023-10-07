package com.wakedata.common.redis.lock.core;


import com.wakedata.common.redis.lock.exception.RedisLockTimeoutException;
import com.wakedata.common.redis.lock.handler.LockTimeoutHandler;
import com.wakedata.common.redis.lock.lock.Lock;
import com.wakedata.common.redis.lock.module.LockInfo;
import org.aspectj.lang.JoinPoint;

import java.util.concurrent.TimeUnit;

/**
 * 锁超时时的执行策略
 *
 * @author hhf
 * @date 2020/12/28
 */
public enum LockTimeoutStrategy implements LockTimeoutHandler {

    /**
     * 继续执行业务逻辑，不做任何处理
     */
    NO_OPERATION() {
        @Override
        public void handle(LockInfo lockInfo, Lock lock, JoinPoint joinPoint) {

        }
    },

    /**
     * 快速失败
     */
    FAIL_FAST() {
        @Override
        public void handle(LockInfo lockInfo, Lock lock, JoinPoint joinPoint) {

            String errorMsg = String.format("Failed to acquire Lock(%s) with timeout(%ds)", lockInfo.getName(), lockInfo.getWaitTime());
            throw new RedisLockTimeoutException(errorMsg);
        }
    },

    /**
     * 一直阻塞，直到获得锁，在太多的尝试后，仍会报错
     */
    KEEP_ACQUIRE() {

        private static final long DEFAULT_INTERVAL = 100L;

        private static final long DEFAULT_MAX_INTERVAL = 3 * 60 * 1000L;

        @Override
        public void handle(LockInfo lockInfo, Lock lock, JoinPoint joinPoint) {

            long interval = DEFAULT_INTERVAL;

            while (!lock.acquire()) {

                if (interval > DEFAULT_MAX_INTERVAL) {
                    String errorMsg = String.format("Failed to acquire Lock(%s) after too many times, this may because dead lock occurs.",
                            lockInfo.getName());
                    throw new RedisLockTimeoutException(errorMsg);
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(interval);
                    interval <<= 1;
                } catch (InterruptedException e) {
                    throw new RedisLockTimeoutException("Failed to acquire Lock", e);
                }
            }
        }
    }
}