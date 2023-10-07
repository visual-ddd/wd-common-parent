package com.wakedata.common.redis.jedis.redis;


import com.wakedata.common.redis.jedis.redis.pubsub.IPubSubRedisClient;
import org.springframework.beans.FatalBeanException;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public class RedisUtilWrapper {

    private static final String REDIS_CLIENT_BEAN = "redisClient";
    private static Map<String, IRedisClient> redisClientMap = new HashMap<String, IRedisClient>();
    private static Map<String, IPubSubRedisClient> wedisClientMap = new HashMap<String, IPubSubRedisClient>();

    public RedisUtilWrapper() {
    }

    public static IRedisClient getRedisClient() throws FatalBeanException{
        if(redisClientMap.containsKey(REDIS_CLIENT_BEAN)){
            return redisClientMap.get(REDIS_CLIENT_BEAN);
        }else{
            IRedisClient redisClient = (IRedisClient)BaseApplicationContext.getBeanById(REDIS_CLIENT_BEAN);
            if(null != redisClient){
                redisClientMap.put(REDIS_CLIENT_BEAN, redisClient);
                return redisClient;
            }else{
                throw new FatalBeanException("Redis client bean redisClient was not found");
            }
        }
    }

    public static IPubSubRedisClient getPubSubRedisClient() throws FatalBeanException{
        if(wedisClientMap.containsKey(REDIS_CLIENT_BEAN)){
            return wedisClientMap.get(REDIS_CLIENT_BEAN);
        }else{
            IPubSubRedisClient redisClient = BaseApplicationContext.getBeanById(REDIS_CLIENT_BEAN);
            if(null != redisClient){
                wedisClientMap.put(REDIS_CLIENT_BEAN, redisClient);
                return redisClient;
            }else{
                throw new FatalBeanException("Redis client bean redisClient was not found");
            }
        }
    }

}
