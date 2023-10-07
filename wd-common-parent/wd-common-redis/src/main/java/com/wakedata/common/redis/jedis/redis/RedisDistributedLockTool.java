package com.wakedata.common.redis.jedis.redis;


import java.util.Collections;
import java.util.Random;

/**
 *
 * @author wujiang
 * @date 2019/2/13
 */
@Deprecated
public class RedisDistributedLockTool {

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * 尝试获取分布式锁
     * @param redisClient Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public static boolean getDistributedLock(IRedisClient redisClient, String lockKey, String requestId, int expireTime, int timeout) {

        try {
            long currentTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - currentTime < timeout){
                String result = redisClient.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
                if (LOCK_SUCCESS.equals(result)) {
                    return true;
                }
                //短暂休眠，避免可能的活锁
                Thread.sleep(5, new Random().nextInt(999999));
            }
        } catch (Exception e){
            throw new RuntimeException("locking error",e);
        }
        return false;

    }

    /**
     * 释放分布式锁
     * @param redisClient Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(IRedisClient redisClient, String lockKey, String requestId) {

        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = redisClient.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));

        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;

    }

}
