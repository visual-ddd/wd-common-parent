package com.wakedata.common.redis.jedis.redis;


import com.wakedata.common.redis.jedis.redis.pubsub.IPubSubRedisClient;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

/**
 * @author pengxu
 * @Date 2019/8/2.
 */
@Deprecated
public class PubSubRedisClientImpl extends WRedisClientImpl implements IPubSubRedisClient {
    private static final Logger LOG = LoggerFactory.getLogger(PubSubRedisClientImpl.class);

    public PubSubRedisClientImpl(GenericObjectPoolConfig config, String host) {
        super(config, host);
    }

    public PubSubRedisClientImpl(String host, Integer port) {
        super(host, port);
    }

    public PubSubRedisClientImpl(String host, Integer port, WRedisClientConfig clientConfig) {
        super(host, port, clientConfig);
    }

    public PubSubRedisClientImpl(String host, Integer port, String password, WRedisClientConfig clientConfig) {
        super(host, port, password, clientConfig);
    }

    @Override
    public Long publish(String channel, String message) {
        byte[] c = SafeEncoder.encode(channel);
        byte[] m = SafeEncoder.encode(message);
        Jedis jedis = null;
        Long ret = null;
        try {
            jedis = pool.getResource();
            ret = jedis.publish(c, m);
            pool.returnResource(jedis);
            return ret;
        } catch (JedisException e) {
            if (jedis != null) {
                if (e instanceof JedisConnectionException) {
                    pool.returnBrokenResource(jedis);
                } else {
                    pool.returnResource(jedis);
                }
            }
            LOG.error(e.getMessage(), e);
            throw e;
        } finally {
        }

    }

    @Override
    public void subscribe(JedisPubSub jedisPubSub, String... channels) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.subscribe(jedisPubSub, channels);
            pool.returnResource(jedis);
        } catch (JedisException e) {
            if (jedis != null) {
                if (e instanceof JedisConnectionException) {
                    pool.returnBrokenResource(jedis);
                } else {
                    pool.returnResource(jedis);
                }
            }
            LOG.error(e.getMessage(), e);
            throw e;
        } finally {
        }
    }
}
