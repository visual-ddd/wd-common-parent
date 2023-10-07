package com.wakedata.common.redis.lock.handler;

import com.wakedata.common.redis.lock.module.LockInfo;

/**
 * 释放锁超时的处理逻辑接口
 *
 * @author hhf
 * @date 2020/12/28
 */
public interface ReleaseTimeoutHandler {

    /**
     * 释放锁超时的处理方法
     *
     * @param lockInfo
     */
    void handle(LockInfo lockInfo);
}