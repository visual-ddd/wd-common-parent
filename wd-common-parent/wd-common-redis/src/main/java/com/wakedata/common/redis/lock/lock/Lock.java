package com.wakedata.common.redis.lock.lock;

/**
 * @author hhf
 * @date 2020/12/25
 */
public interface Lock {

    /**
     * 获取锁
     *
     * @return
     */
    boolean acquire();

    /**
     * 释放锁
     *
     * @return
     */
    boolean release();
}