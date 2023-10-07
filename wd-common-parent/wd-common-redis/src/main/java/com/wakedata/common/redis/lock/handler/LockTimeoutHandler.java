package com.wakedata.common.redis.lock.handler;

import com.wakedata.common.redis.lock.lock.Lock;
import com.wakedata.common.redis.lock.module.LockInfo;
import org.aspectj.lang.JoinPoint;

/**
 * 获取锁超时的处理逻辑接口
 *
 * @author hhf
 * @date 2020/12/28
 */
public interface LockTimeoutHandler {

    /**
     * 获取锁超时的处理方法
     *
     * @param lockInfo
     * @param lock
     * @param joinPoint
     */
    void handle(LockInfo lockInfo, Lock lock, JoinPoint joinPoint);
}