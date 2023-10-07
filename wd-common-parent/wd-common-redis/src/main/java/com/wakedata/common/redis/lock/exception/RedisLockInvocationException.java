package com.wakedata.common.redis.lock.exception;

/**
 * @author hhf
 * @date 2020/12/28
 */
public class RedisLockInvocationException extends RuntimeException {

    public RedisLockInvocationException() {
    }

    public RedisLockInvocationException(String message) {
        super(message);
    }

    public RedisLockInvocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
