package com.wakedata.common.redis.lock.core;


import com.wakedata.common.redis.lock.exception.RedisLockTimeoutException;
import com.wakedata.common.redis.lock.handler.ReleaseTimeoutHandler;
import com.wakedata.common.redis.lock.module.LockInfo;

/**
 * 释放锁超时策略
 *
 * @author hhf
 * @date 2020/12/28
 */
public enum ReleaseTimeoutStrategy implements ReleaseTimeoutHandler {

    /**
     * 继续执行业务逻辑，不做任何处理
     */
    NO_OPERATION() {
        @Override
        public void handle(LockInfo lockInfo) {

        }
    },
    /**
     * 快速失败
     */
    FAIL_FAST() {
        @Override
        public void handle(LockInfo lockInfo) {

            String errorMsg = String.format("Found Lock(%s) already been released while lock lease time is %d s", lockInfo.getName(), lockInfo.getLeaseTime());
            throw new RedisLockTimeoutException(errorMsg);
        }
    }
}
