package com.wakedata.common.redis.jedis.redis.pubsub;


import com.wakedata.common.redis.jedis.redis.IRedisClient;
import redis.clients.jedis.JedisPubSub;

@Deprecated
public interface IPubSubRedisClient extends IRedisClient {
    Long publish(String channel, String message);
    void subscribe(JedisPubSub jedisPubSub, String... channels);
}