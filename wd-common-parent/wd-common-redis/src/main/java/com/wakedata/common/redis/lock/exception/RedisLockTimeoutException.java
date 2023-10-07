package com.wakedata.common.redis.lock.exception;

/**
 * @author hhf
 * @date 2020/12/28
 */
public class RedisLockTimeoutException extends RuntimeException {

    public RedisLockTimeoutException() {
    }

    public RedisLockTimeoutException(String message) {
        super(message);
    }

    public RedisLockTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
