package com.wakedata.common.redis.lock.module;

/**
 * 锁类型
 *
 * @author hhf
 * @date 2020/12/28
 */
public enum LockType {
    /**
     * 可重入锁
     */
    Reentrant,
    /**
     * 公平锁
     */
    Fair,
    /**
     * 读锁
     */
    Read,
    /**
     * 写锁
     */
    Write;

    LockType() {
    }

}
