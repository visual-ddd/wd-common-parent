package com.wakedata.common.redis.jedis.redis;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

import java.io.IOException;
import java.util.*;

/**
 * @author pengxu
 * @Date 2019/8/2.
 */
@Deprecated
public class WRedisClientImpl implements IRedisClient {


    private static final Logger LOG = LoggerFactory.getLogger(WRedisClientImpl.class);

    protected JedisPool pool;

    protected WRedisClientConfig clientConfig;

    /**
     * 根据RedisClientConfig的配置构造一个RedisClient
     *
     * @param config
     */
    public WRedisClientImpl(GenericObjectPoolConfig config, String host) {
        if (config == null) {
            throw new IllegalArgumentException(
                    "RedisClientConfig could not be null!");
        }
        this.pool = new JedisPool(config, host);

    }


    /**
     * 根据host/port配置构造一个RedisClient
     */
    public WRedisClientImpl(String host, Integer port) {
        if (StringUtils.isEmpty(host) || null == port) {
            throw new IllegalArgumentException(
                    "args could not be null!");
        }
        this.pool = new JedisPool(host, port);
    }

    /**
     * 根据RedisClientConfig的配置构造一个RedisClient
     */
    public WRedisClientImpl(String host, Integer port, WRedisClientConfig clientConfig) {
        if (StringUtils.isEmpty(host) || null == port) {
            throw new IllegalArgumentException(
                    "args could not be null!");
        }
        this.pool = new JedisPool(host, port);
        this.clientConfig = clientConfig;
    }

    /**
     * 根据RedisClientConfig的配置构造一个RedisClient
     */
    public WRedisClientImpl(String host, Integer port, String password, WRedisClientConfig clientConfig) {
        if (StringUtils.isEmpty(host) || null == port) {
            throw new IllegalArgumentException(
                    "args could not be null!");
        }
        this.pool = new JedisPool(new GenericObjectPoolConfig(), host, port,
                Protocol.DEFAULT_TIMEOUT, password, Protocol.DEFAULT_DATABASE, null);
        this.clientConfig = clientConfig;
    }

    /**
     * 根据RedisClientConfig的配置构造一个RedisClient
     */
    public WRedisClientImpl(String host, Integer port, String password, JedisPoolConfig jedisConfig, WRedisClientConfig clientConfig) {
        if (StringUtils.isEmpty(host) || null == port) {
            throw new IllegalArgumentException(
                    "args could not be null!");
        }
        this.pool = new JedisPool(jedisConfig, host, port, Protocol.DEFAULT_TIMEOUT, password, Protocol.DEFAULT_DATABASE, null);
        this.clientConfig = clientConfig;
    }

    /**
     * Set the string value as value of the key. The string can't be longer than
     * 1073741824 bytes (1 GB).
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param value
     * @return Status code reply
     */
    @Override
    public String set(final String key, String value) {
        return set(SafeEncoder.encode(key), SafeEncoder.encode(value));
    }


    /**
     * Set the string value as value of the key. The string can't be longer than
     * 1073741824 bytes (1 GB).
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param value
     * @return Status code reply
     */
    @Override
    public String set(final byte[] key, final byte[] value) {
        Jedis jedis = null;
        String ret = null;

        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.set(finallyKey, value);
            pool.returnResource(jedis);
            return ret;
        } catch (JedisException e) {
            return catchReturnAndThrow(jedis, e);
        } finally {

        }
    }

    private String catchReturnAndThrow(Jedis jedis, JedisException e) {
        if (jedis != null) {
            if (e instanceof JedisConnectionException) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }
        }
        LOG.error(e.getMessage(), e);
        throw e;
    }

    /**
     * Set the string value as value of the key. The string can't be longer than
     * 1073741824 bytes (1 GB).
     *
     * @param key
     * @param value
     * @param nxxx  NX|XX, NX -- Only set the key if it does not already exist. XX
     *              -- Only set the key if it already exist.
     * @param expx  EX|PX, expire time units: EX = seconds; PX = milliseconds
     * @param time  expire time in the units of {@param #expx}
     * @return Status code reply
     */
    public String set(final String key, final String value, final String nxxx,
                      final String expx, final long time) {
        return this.set(SafeEncoder.encode(key), SafeEncoder.encode(value),
                SafeEncoder.encode(nxxx), SafeEncoder.encode(expx), time);
    }

    /**
     * Set the string value as value of the key. The string can't be longer than
     * 1073741824 bytes (1 GB).
     *
     * @param key
     * @param value
     * @param nxxx  NX|XX, NX -- Only set the key if it does not already exist. XX
     *              -- Only set the key if it already exist.
     * @param expx  EX|PX, expire time units: EX = seconds; PX = milliseconds
     * @param time  expire time in the units of {@param #expx}
     * @return Status code reply
     */
    public String set(final byte[] key, final byte[] value, final byte[] nxxx,
                      final byte[] expx, final long time) {
        Jedis jedis = null;
        String ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.set(finallyKey, value, nxxx, expx, time);
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

    /**
     * Get the value of the specified key. If the key does not exist null is
     * returned. If the value stored at key is not a string an error is returned
     * because GET can only handle string values.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @return Bulk reply
     */
    @Override
    public String get(final String key) {
        return SafeEncoder.encode(get(SafeEncoder.encode(key)));
    }

    /**
     * Get the value of the specified key. If the key does not exist null is
     * returned. If the value stored at key is not a string an error is returned
     * because GET can only handle string values.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @return Bulk reply
     */
    @Override
    public byte[] get(final byte[] key) {
        Jedis jedis = null;
        byte[] ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.get(finallyKey);
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

    /**
     * Test if the specified key exists. The command returns "1" if the key
     * exists, otherwise "0" is returned. Note that even keys set with an empty
     * string as value will return "1". Time complexity: O(1)
     *
     * @param key
     * @return Boolean reply, true if the key exists, otherwise false
     */
    @Override
    public Boolean exists(final String key) {
        return exists(SafeEncoder.encode(key));
    }

    /**
     * Test if the specified key exists. The command returns "1" if the key
     * exists, otherwise "0" is returned. Note that even keys set with an empty
     * string as value will return "1". Time complexity: O(1)
     *
     * @param key
     * @return Boolean reply, true if the key exists, otherwise false
     */
    @Override
    public Boolean exists(final byte[] key) {
        Jedis jedis = null;
        Boolean ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.exists(finallyKey);
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

    /**
     * Remove the specified keys. If a given key does not exist no operation is
     * performed for this key. The command returns the number of keys removed.
     * Time complexity: O(1)
     *
     * @param keys
     * @return Integer reply, specifically: an integer greater than 0 if one or
     * more keys were removed 0 if none of the specified key existed
     */
    @Override
    public Long del(final String... keys) {
        return del(SafeEncoder.encodeMany(keys));
    }


    /**
     * Remove the specified keys. If a given key does not exist no operation is
     * performed for this key. The command returns the number of keys removed.
     * Time complexity: O(1)
     *
     * @param keys
     * @return Integer reply, specifically: an integer greater than 0 if one or
     * more keys were removed 0 if none of the specified key existed
     */
    @Override
    public Long del(final byte[]... keys) {
        Jedis jedis = null;
        Long ret = null;
        byte[][] finallyKeys = WRedisUtil.concatKeys(clientConfig, keys);
        try {
            jedis = pool.getResource();
            ret = jedis.del(finallyKeys);
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
    public Long del(String key) {
        return del(SafeEncoder.encode(key));
    }

    @Override
    public Long del(byte[] key) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.del(finallyKey);
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

    /**
     * Return the type of the value stored at key in form of a string. The type
     * can be one of "none", "string", "list", "set". "none" is returned if the
     * key does not exist. Time complexity: O(1)
     *
     * @param key
     * @return Status code reply, specifically: "none" if the key does not exist
     * "string" if the key contains a String value "list" if the key
     * contains a List value "set" if the key contains a Set value
     * "zset" if the key contains a Sorted Set value "hash" if the key
     * contains a Hash value
     */
    @Override
    public String type(final String key) {
        return type(SafeEncoder.encode(key));
    }

    /**
     * Return the type of the value stored at key in form of a string. The type
     * can be one of "none", "string", "list", "set". "none" is returned if the
     * key does not exist. Time complexity: O(1)
     *
     * @param key
     * @return Status code reply, specifically: "none" if the key does not exist
     * "string" if the key contains a String value "list" if the key
     * contains a List value "set" if the key contains a Set value
     * "zset" if the key contains a Sorted Set value "hash" if the key
     * contains a Hash value
     */
    @Override
    public String type(final byte[] key) {
        Jedis jedis = null;
        String ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.type(finallyKey);
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

    /**
     * Set a timeout on the specified key. After the timeout the key will be
     * automatically deleted by the server. A key with an associated timeout is
     * said to be volatile in Redis terminology.
     * <p>
     * Voltile keys are stored on disk like the other keys, the timeout is
     * persistent too like all the other aspects of the dataset. Saving a
     * dataset containing expires and stopping the server does not stop the flow
     * of time as Redis stores on disk the time when the key will no longer be
     * available as Unix time, and not the remaining seconds.
     * <p>
     * Since Redis 2.1.3 you can update the value of the timeout of a key
     * already having an expire set. It is also possible to undo the expire at
     * all turning the key into a normal key using the {@link #persist(String)
     * PERSIST} command.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param seconds
     * @return Integer reply, specifically: 1: the timeout was set. 0: the
     * timeout was not set since the key already has an associated
     * timeout (this may happen only in Redis versions < 2.1.3, Redis >=
     * 2.1.3 will happily update the timeout), or the key does not
     * exist.
     * @see <ahref="http://code.google.com/p/redis/wiki/ExpireCommand">ExpireCommand</a>
     */
    @Override
    public Long expire(final String key, final int seconds) {
        return expire(SafeEncoder.encode(key), seconds);
    }

    /**
     * Set a timeout on the specified key. After the timeout the key will be
     * automatically deleted by the server. A key with an associated timeout is
     * said to be volatile in Redis terminology.
     * <p>
     * Voltile keys are stored on disk like the other keys, the timeout is
     * persistent too like all the other aspects of the dataset. Saving a
     * dataset containing expires and stopping the server does not stop the flow
     * of time as Redis stores on disk the time when the key will no longer be
     * available as Unix time, and not the remaining seconds.
     * <p>
     * Since Redis 2.1.3 you can update the value of the timeout of a key
     * already having an expire set. It is also possible to undo the expire at
     * all turning the key into a normal key using the {@link #persist(String)
     * PERSIST} command.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param seconds
     * @return Integer reply, specifically: 1: the timeout was set. 0: the
     * timeout was not set since the key already has an associated
     * timeout (this may happen only in Redis versions < 2.1.3, Redis >=
     * 2.1.3 will happily update the timeout), or the key does not
     * exist.
     * @see <ahref="http://code.google.com/p/redis/wiki/ExpireCommand">ExpireCommand</a>
     */
    @Override
    public Long expire(final byte[] key, final int seconds) {
        Jedis jed = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jed = pool.getResource();
            ret = jed.expire(finallyKey, seconds);
            pool.returnResource(jed);
            return ret;
        } catch (JedisException e) {
            if (jed != null) {
                if (e instanceof JedisConnectionException) {
                    pool.returnBrokenResource(jed);
                } else {
                    pool.returnResource(jed);
                }
            }
            LOG.error(e.getMessage(), e);
            throw e;
        } finally {
        }
    }

    /**
     * EXPIREAT works exctly like {@link #expire(String, int) EXPIRE} but
     * instead to get the number of seconds representing the Time To Live of the
     * key as a second argument (that is a relative way of specifing the TTL),
     * it takes an absolute one in the form of a UNIX timestamp (Number of
     * seconds elapsed since 1 Gen 1970).
     * <p>
     * EXPIREAT was introduced in order to implement the Append Only File
     * persistence mode so that EXPIRE commands are automatically translated
     * into EXPIREAT commands for the append only file. Of course EXPIREAT can
     * also used by programmers that need a way to simply specify that a given
     * key should expire at a given time in the future.
     * <p>
     * Since Redis 2.1.3 you can update the value of the timeout of a key
     * already having an expire set. It is also possible to undo the expire at
     * all turning the key into a normal key using the {@link #persist(String)
     * PERSIST} command.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param unixTime
     * @return Integer reply, specifically: 1: the timeout was set. 0: the
     * timeout was not set since the key already has an associated
     * timeout (this may happen only in Redis versions < 2.1.3, Redis >=
     * 2.1.3 will happily update the timeout), or the key does not
     * exist.
     * @see <ahref="http://code.google.com/p/redis/wiki/ExpireCommand">ExpireCommand</a>
     */
    @Override
    public Long expireAt(final String key, final long unixTime) {
        return expireAt(SafeEncoder.encode(key), unixTime);
    }

    /**
     * EXPIREAT works exctly like {@link #expire(String, int) EXPIRE} but
     * instead to get the number of seconds representing the Time To Live of the
     * key as a second argument (that is a relative way of specifing the TTL),
     * it takes an absolute one in the form of a UNIX timestamp (Number of
     * seconds elapsed since 1 Gen 1970).
     * <p>
     * EXPIREAT was introduced in order to implement the Append Only File
     * persistence mode so that EXPIRE commands are automatically translated
     * into EXPIREAT commands for the append only file. Of course EXPIREAT can
     * also used by programmers that need a way to simply specify that a given
     * key should expire at a given time in the future.
     * <p>
     * Since Redis 2.1.3 you can update the value of the timeout of a key
     * already having an expire set. It is also possible to undo the expire at
     * all turning the key into a normal key using the {@link #persist(String)
     * PERSIST} command.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param unixTime
     * @return Integer reply, specifically: 1: the timeout was set. 0: the
     * timeout was not set since the key already has an associated
     * timeout (this may happen only in Redis versions < 2.1.3, Redis >=
     * 2.1.3 will happily update the timeout), or the key does not
     * exist.
     * @see <ahref="http://code.google.com/p/redis/wiki/ExpireCommand">ExpireCommand</a>
     */
    @Override
    public Long expireAt(final byte[] key, final long unixTime) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.expireAt(finallyKey, unixTime);
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

    /**
     * The TTL command returns the remaining time to live in seconds of a key
     * that has an {@link #expire(String, int) EXPIRE} set. This introspection
     * capability allows a Redis client to check how many seconds a given key
     * will continue to be part of the dataset.
     *
     * @param key
     * @return Integer reply, returns the remaining time to live in seconds of a
     * key that has an EXPIRE. In Redis 2.6 or older, if the Key does
     * not exists or does not have an associated expire, -1 is returned.
     * In Redis 2.8 or newer, if the Key does not have an associated
     * expire, -1 is returned or if the Key does not exists, -2 is
     * returned.
     */
    @Override
    public Long ttl(final String key) {
        return ttl(SafeEncoder.encode(key));
    }

    /**
     * The TTL command returns the remaining time to live in seconds of a key
     * that has an {@link #expire(String, int) EXPIRE} set. This introspection
     * capability allows a Redis client to check how many seconds a given key
     * will continue to be part of the dataset.
     *
     * @param key
     * @return Integer reply, returns the remaining time to live in seconds of a
     * key that has an EXPIRE. In Redis 2.6 or older, if the Key does
     * not exists or does not have an associated expire, -1 is returned.
     * In Redis 2.8 or newer, if the Key does not have an associated
     * expire, -1 is returned or if the Key does not exists, -2 is
     * returned.
     */
    @Override
    public Long ttl(final byte[] key) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.ttl(finallyKey);
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

    /**
     * GETSET is an atomic set this value and return the old value command. Set
     * key to the string value and return the old value stored at key. The
     * string can't be longer than 1073741824 bytes (1 GB).
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param value
     * @return Bulk reply
     */
    @Override
    public String getSet(final String key, final String value) {
        return SafeEncoder.encode(getSet(SafeEncoder.encode(key),
                SafeEncoder.encode(value)));
    }

    /**
     * GETSET is an atomic set this value and return the old value command. Set
     * key to the string value and return the old value stored at key. The
     * string can't be longer than 1073741824 bytes (1 GB).
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param value
     * @return Bulk reply
     */
    @Override
    public byte[] getSet(final byte[] key, final byte[] value) {
        Jedis jedis = null;
        byte[] ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis
                    .getSet(finallyKey, value);
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

    /**
     * Get the values of all the specified keys. If one or more keys dont exist
     * or is not of type String, a 'nil' value is returned instead of the value
     * of the specified key, but the operation never fails.
     * <p>
     * Time complexity: O(1) for every key
     *
     * @param keys
     * @return Multi bulk reply
     */
    @Override
    public List<String> mget(final String... keys) {
        List<byte[]> list = mget(SafeEncoder.encodeMany(keys));
        return Arrays.asList(SafeEncoder.encodeMany(list.toArray(new byte[list
                .size()][])));
    }

    /**
     * Get the values of all the specified keys. If one or more keys dont exist
     * or is not of type String, a 'nil' value is returned instead of the value
     * of the specified key, but the operation never fails.
     * <p>
     * Time complexity: O(1) for every key
     *
     * @param keys
     * @return Multi bulk reply
     */
    @Override
    public List<byte[]> mget(final byte[]... keys) {
        Jedis jedis = null;
        List<byte[]> ret = null;
        byte[][] finallyKeys = WRedisUtil.concatKeys(clientConfig, keys);
        try {
            jedis = pool.getResource();
            ret = jedis.mget(finallyKeys);
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

    /**
     * SETNX works exactly like {@link #set(String, String) SET} with the only
     * difference that if the key already exists no operation is performed.
     * SETNX actually means "SET if Not eXists".
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param value
     * @return Integer reply, specifically: 1 if the key was set 0 if the key
     * was not set
     */
    @Override
    public Long setnx(final String key, final String value) {
        return setnx(SafeEncoder.encode(key), SafeEncoder.encode(value));
    }

    /**
     * SETNX works exactly like {@link #set(String, String) SET} with the only
     * difference that if the key already exists no operation is performed.
     * SETNX actually means "SET if Not eXists".
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param value
     * @return Integer reply, specifically: 1 if the key was set 0 if the key
     * was not set
     */
    @Override
    public Long setnx(final byte[] key, final byte[] value) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.setnx(finallyKey, value);
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

    /**
     * The command is exactly equivalent to the following group of commands:
     * {@link #set(String, String) SET} + {@link #expire(String, int) EXPIRE}.
     * The operation is atomic.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param seconds
     * @param value
     * @return Status code reply
     */
    @Override
    public String setex(final String key, final int seconds, final String value) {
        return setex(SafeEncoder.encode(key), seconds,
                SafeEncoder.encode(value));
    }

    /**
     * The command is exactly equivalent to the following group of commands:
     * {@link #set(String, String) SET} + {@link #expire(String, int) EXPIRE}.
     * The operation is atomic.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param seconds
     * @param value
     * @return Status code reply
     */
    @Override
    public String setex(final byte[] key, final int seconds, final byte[] value) {
        Jedis jedis = null;
        String ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.setex(finallyKey, seconds, value);
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

    /**
     * IDECRBY work just like {@link #decr(String) INCR} but instead to
     * decrement by 1 the decrement is integer.
     * <p>
     * INCR commands are limited to 64 bit signed integers.
     * <p>
     * Note: this is actually a string operation, that is, in Redis there are
     * not "integer" types. Simply the string stored at the key is parsed as a
     * base 10 64 bit signed integer, incremented, and then converted back as a
     * string.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param integer
     * @return Integer reply, this commands will reply with the new value of key
     * after the increment.
     * @see #incr(String)
     * @see #decr(String)
     * @see #incrBy(String, long)
     */
    @Override
    public Long decrBy(final String key, final long integer) {
        return decrBy(SafeEncoder.encode(key), integer);
    }

    /**
     * IDECRBY work just like {@link #decr(String) INCR} but instead to
     * decrement by 1 the decrement is integer.
     * <p>
     * INCR commands are limited to 64 bit signed integers.
     * <p>
     * Note: this is actually a string operation, that is, in Redis there are
     * not "integer" types. Simply the string stored at the key is parsed as a
     * base 10 64 bit signed integer, incremented, and then converted back as a
     * string.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param integer
     * @return Integer reply, this commands will reply with the new value of key
     * after the increment.
     * @see #incr(String)
     * @see #decr(String)
     * @see #incrBy(String, long)
     */
    @Override
    public Long decrBy(final byte[] key, final long integer) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.decrBy(finallyKey, integer);
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

    /**
     * Decrement the number stored at key by one. If the key does not exist or
     * contains a value of a wrong type, set the key to the value of "0" before
     * to perform the decrement operation.
     * <p>
     * INCR commands are limited to 64 bit signed integers.
     * <p>
     * Note: this is actually a string operation, that is, in Redis there are
     * not "integer" types. Simply the string stored at the key is parsed as a
     * base 10 64 bit signed integer, incremented, and then converted back as a
     * string.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @return Integer reply, this commands will reply with the new value of key
     * after the increment.
     * @see #incr(String)
     * @see #incrBy(String, long)
     * @see #decrBy(String, long)
     */
    @Override
    public Long decr(final String key) {
        return decr(SafeEncoder.encode(key));
    }

    /**
     * Decrement the number stored at key by one. If the key does not exist or
     * contains a value of a wrong type, set the key to the value of "0" before
     * to perform the decrement operation.
     * <p>
     * INCR commands are limited to 64 bit signed integers.
     * <p>
     * Note: this is actually a string operation, that is, in Redis there are
     * not "integer" types. Simply the string stored at the key is parsed as a
     * base 10 64 bit signed integer, incremented, and then converted back as a
     * string.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @return Integer reply, this commands will reply with the new value of key
     * after the increment.
     * @see #incr(String)
     * @see #incrBy(String, long)
     * @see #decrBy(String, long)
     */
    @Override
    public Long decr(final byte[] key) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.decr(finallyKey);
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

    /**
     * INCRBY work just like {@link #incr(String) INCR} but instead to increment
     * by 1 the increment is integer.
     * <p>
     * INCR commands are limited to 64 bit signed integers.
     * <p>
     * Note: this is actually a string operation, that is, in Redis there are
     * not "integer" types. Simply the string stored at the key is parsed as a
     * base 10 64 bit signed integer, incremented, and then converted back as a
     * string.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param integer
     * @return Integer reply, this commands will reply with the new value of key
     * after the increment.
     * @see #incr(String)
     * @see #decr(String)
     * @see #decrBy(String, long)
     */
    @Override
    public Long incrBy(final String key, final long integer) {
        return incrBy(SafeEncoder.encode(key), integer);
    }

    /**
     * INCRBY work just like {@link #incr(String) INCR} but instead to increment
     * by 1 the increment is integer.
     * <p>
     * INCR commands are limited to 64 bit signed integers.
     * <p>
     * Note: this is actually a string operation, that is, in Redis there are
     * not "integer" types. Simply the string stored at the key is parsed as a
     * base 10 64 bit signed integer, incremented, and then converted back as a
     * string.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param integer
     * @return Integer reply, this commands will reply with the new value of key
     * after the increment.
     * @see #incr(String)
     * @see #decr(String)
     * @see #decrBy(String, long)
     */
    @Override
    public Long incrBy(final byte[] key, final long integer) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.incrBy(finallyKey, integer);
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

    /**
     * INCRBYFLOAT
     * <p>
     * INCRBYFLOAT commands are limited to double precision floating point
     * values.
     * <p>
     * Note: this is actually a string operation, that is, in Redis there are
     * not "double" types. Simply the string stored at the key is parsed as a
     * base double precision floating point value, incremented, and then
     * converted back as a string. There is no DECRYBYFLOAT but providing a
     * negative value will work as expected.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param value
     * @return Double reply, this commands will reply with the new value of key
     * after the increment.
     */
    @Override
    public Double incrByFloat(final String key, final double value) {
        return incrByFloat(SafeEncoder.encode(key), value);
    }

    /**
     * INCRBYFLOAT
     * <p>
     * INCRBYFLOAT commands are limited to double precision floating point
     * values.
     * <p>
     * Note: this is actually a string operation, that is, in Redis there are
     * not "double" types. Simply the string stored at the key is parsed as a
     * base double precision floating point value, incremented, and then
     * converted back as a string. There is no DECRYBYFLOAT but providing a
     * negative value will work as expected.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param value
     * @return Double reply, this commands will reply with the new value of key
     * after the increment.
     */
    @Override
    public Double incrByFloat(final byte[] key, final double value) {
        Jedis jedis = null;
        Double ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.incrByFloat(finallyKey, value);
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

    /**
     * Increment the number stored at key by one. If the key does not exist or
     * contains a value of a wrong type, set the key to the value of "0" before
     * to perform the increment operation.
     * <p>
     * INCR commands are limited to 64 bit signed integers.
     * <p>
     * Note: this is actually a string operation, that is, in Redis there are
     * not "integer" types. Simply the string stored at the key is parsed as a
     * base 10 64 bit signed integer, incremented, and then converted back as a
     * string.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @return Integer reply, this commands will reply with the new value of key
     * after the increment.
     * @see #incrBy(String, long)
     * @see #decr(String)
     * @see #decrBy(String, long)
     */
    @Override
    public Long incr(final String key) {
        return incr(SafeEncoder.encode(key));
    }

    /**
     * Increment the number s`tored at key by one. If the key does not exist or
     * contains a value of a wrong type, set the key to the value of "0" before
     * to perform the increment operation.
     * <p>
     * INCR commands are limited to 64 bit signed integers.
     * <p>
     * Note: this is actually a string operation, that is, in Redis there are
     * not "integer" types. Simply the string stored at the key is parsed as a
     * base 10 64 bit signed integer, incremented, and then converted back as a
     * string.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @return Integer reply, this commands will reply with the new value of key
     * after the increment.
     * @see #incrBy(String, long)
     * @see #decr(String)
     * @see #decrBy(String, long)
     */
    @Override
    public Long incr(final byte[] key) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.incr(finallyKey);
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

    /**
     * If the key already exists and is a string, this command appends the
     * provided value at the end of the string. If the key does not exist it is
     * created and set as an empty string, so APPEND will be very similar to SET
     * in this special case.
     * <p>
     * Time complexity: O(1). The amortized time complexity is O(1) assuming the
     * appended value is small and the already present value is of any size,
     * since the dynamic string library used by Redis will double the free space
     * available on every reallocation.
     *
     * @param key
     * @param value
     * @return Integer reply, specifically the total length of the string after
     * the append operation.
     */
    @Override
    public Long append(final String key, final String value) {
        return append(SafeEncoder.encode(key), SafeEncoder.encode(value));
    }

    /**
     * If the key already exists and is a string, this command appends the
     * provided value at the end of the string. If the key does not exist it is
     * created and set as an empty string, so APPEND will be very similar to SET
     * in this special case.
     * <p>
     * Time complexity: O(1). The amortized time complexity is O(1) assuming the
     * appended value is small and the already present value is of any size,
     * since the dynamic string library used by Redis will double the free space
     * available on every reallocation.
     *
     * @param key
     * @param value
     * @return Integer reply, specifically the total length of the string after
     * the append operation.
     */
    @Override
    public Long append(final byte[] key, final byte[] value) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis
                    .append(finallyKey, value);
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

    /**
     * Return a subset of the string from offset start to offset end (both
     * offsets are inclusive). Negative offsets can be used in order to provide
     * an offset starting from the end of the string. So -1 means the last char,
     * -2 the penultimate and so forth.
     * <p>
     * The function handles out of range requests without raising an error, but
     * just limiting the resulting range to the actual length of the string.
     * <p>
     * Time complexity: O(start+n) (with start being the start index and n the
     * total length of the requested range). Note that the lookup part of this
     * command is O(1) so for small strings this is actually an O(1) command.
     *
     * @param key
     * @param start
     * @param end
     * @return Bulk reply
     */
    @Override
    public String substr(final String key, final int start, final int end) {
        return SafeEncoder.encode(substr(SafeEncoder.encode(key), start, end));
    }

    /**
     * Return a subset of the string from offset start to offset end (both
     * offsets are inclusive). Negative offsets can be used in order to provide
     * an offset starting from the end of the string. So -1 means the last char,
     * -2 the penultimate and so forth.
     * <p>
     * The function handles out of range requests without raising an error, but
     * just limiting the resulting range to the actual length of the string.
     * <p>
     * Time complexity: O(start+n) (with start being the start index and n the
     * total length of the requested range). Note that the lookup part of this
     * command is O(1) so for small strings this is actually an O(1) command.
     *
     * @param key
     * @param start
     * @param end
     * @return Bulk reply
     */
    @Override
    public byte[] substr(final byte[] key, final int start, final int end) {
        Jedis jedis = null;
        byte[] ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.substr(finallyKey, start, end);
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

    /**
     * Set the specified hash field to the specified value.
     * <p>
     * If key does not exist, a new key holding a hash is created.
     * <p>
     * <b>Time complexity:</b> O(1)
     *
     * @param key
     * @param field
     * @param value
     * @return If the field already exists, and the HSET just produced an update
     * of the value, 0 is returned, otherwise if a new field is created
     * 1 is returned.
     */
    @Override
    public Long hset(final String key, final String field, final String value) {
        return hset(SafeEncoder.encode(key), SafeEncoder.encode(field),
                SafeEncoder.encode(value));
    }

    /**
     * Set the specified hash field to the specified value.
     * <p>
     * If key does not exist, a new key holding a hash is created.
     * <p>
     * <b>Time complexity:</b> O(1)
     *
     * @param key
     * @param field
     * @param value
     * @return If the field already exists, and the HSET just produced an update
     * of the value, 0 is returned, otherwise if a new field is created
     * 1 is returned.
     */
    @Override
    public Long hset(final byte[] key, final byte[] field, final byte[] value) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.hset(finallyKey, field, value);
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

    /**
     * If key holds a hash, retrieve the value associated to the specified
     * field.
     * <p>
     * If the field is not found or the key does not exist, a special 'nil'
     * value is returned.
     * <p>
     * <b>Time complexity:</b> O(1)
     *
     * @param key
     * @param field
     * @return Bulk reply
     */
    @Override
    public String hget(final String key, final String field) {
        return SafeEncoder.encode(hget(SafeEncoder.encode(key),
                SafeEncoder.encode(field)));
    }

    /**
     * If key holds a hash, retrieve the value associated to the specified
     * field.
     * <p>
     * If the field is not found or the key does not exist, a special 'nil'
     * value is returned.
     * <p>
     * <b>Time complexity:</b> O(1)
     *
     * @param key
     * @param field
     * @return Bulk reply
     */
    @Override
    public byte[] hget(final byte[] key, final byte[] field) {
        Jedis jedis = null;
        byte[] ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.hget(finallyKey, field);
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

    /**
     * Set the specified hash field to the specified value if the field not
     * exists. <b>Time complexity:</b> O(1)
     *
     * @param key
     * @param field
     * @param value
     * @return If the field already exists, 0 is returned, otherwise if a new
     * field is created 1 is returned.
     */
    @Override
    public Long hsetnx(final String key, final String field, final String value) {
        return hsetnx(SafeEncoder.encode(key), SafeEncoder.encode(field),
                SafeEncoder.encode(value));
    }

    /**
     * Set the specified hash field to the specified value if the field not
     * exists. <b>Time complexity:</b> O(1)
     *
     * @param key
     * @param field
     * @param value
     * @return If the field already exists, 0 is returned, otherwise if a new
     * field is created 1 is returned.
     */
    @Override
    public Long hsetnx(final byte[] key, final byte[] field, final byte[] value) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.hsetnx(finallyKey, field,
                    value);
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

    /**
     * Set the respective fields to the respective values. HMSET replaces old
     * values with new values.
     * <p>
     * If key does not exist, a new key holding a hash is created.
     * <p>
     * <b>Time complexity:</b> O(N) (with N being the number of fields)
     *
     * @param key
     * @param hash
     * @return Return OK or Exception if hash is empty
     */
    @Override
    public String hmset(final String key, final Map<String, String> hash) {
        return hmset(SafeEncoder.encode(key), SafeEncoder.encodeMap(hash));
    }

    /**
     * Set the respective fields to the respective values. HMSET replaces old
     * values with new values.
     * <p>
     * If key does not exist, a new key holding a hash is created.
     * <p>
     * <b>Time complexity:</b> O(N) (with N being the number of fields)
     *
     * @param key
     * @param hash
     * @return Return OK or Exception if hash is empty
     */
    @Override
    public String hmset(final byte[] key, final Map<byte[], byte[]> hash) {
        Jedis jedis = null;
        String ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.hmset(finallyKey, hash);
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

    /**
     * Retrieve the values associated to the specified fields.
     * <p>
     * If some of the specified fields do not exist, nil values are returned.
     * Non existing keys are considered like empty hashes.
     * <p>
     * <b>Time complexity:</b> O(N) (with N being the number of fields)
     *
     * @param key
     * @param fields
     * @return Multi Bulk Reply specifically a list of all the values associated
     * with the specified fields, in the same order of the request.
     */
    @Override
    public List<String> hmget(final String key, final String... fields) {
        return SafeEncoder.encodeList(hmget(SafeEncoder.encode(key),
                SafeEncoder.encodeMany(fields)));
    }

    /**
     * Retrieve the values associated to the specified fields.
     * <p>
     * If some of the specified fields do not exist, nil values are returned.
     * Non existing keys are considered like empty hashes.
     * <p>
     * <b>Time complexity:</b> O(N) (with N being the number of fields)
     *
     * @param key
     * @param fields
     * @return Multi Bulk Reply specifically a list of all the values associated
     * with the specified fields, in the same order of the request.
     */
    @Override
    public List<byte[]> hmget(final byte[] key, final byte[]... fields) {
        Jedis jedis = null;
        List<byte[]> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.hmget(finallyKey, fields);
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

    /**
     * Increment the number stored at field in the hash at key by value. If key
     * does not exist, a new key holding a hash is created. If field does not
     * exist or holds a string, the value is set to 0 before applying the
     * operation. Since the value argument is signed you can use this command to
     * perform both increments and decrements.
     * <p>
     * The range of values supported by HINCRBY is limited to 64 bit signed
     * integers.
     * <p>
     * <b>Time complexity:</b> O(1)
     *
     * @param key
     * @param field
     * @param value
     * @return Integer reply The new value at field after the increment
     * operation.
     */
    @Override
    public Long hincrBy(final String key, final String field, final long value) {
        return hincrBy(SafeEncoder.encode(key), SafeEncoder.encode(field),
                value);
    }

    /**
     * Increment the number stored at field in the hash at key by value. If key
     * does not exist, a new key holding a hash is created. If field does not
     * exist or holds a string, the value is set to 0 before applying the
     * operation. Since the value argument is signed you can use this command to
     * perform both increments and decrements.
     * <p>
     * The range of values supported by HINCRBY is limited to 64 bit signed
     * integers.
     * <p>
     * <b>Time complexity:</b> O(1)
     *
     * @param key
     * @param field
     * @param value
     * @return Integer reply The new value at field after the increment
     * operation.
     */
    @Override
    public Long hincrBy(final byte[] key, final byte[] field, final long value) {
        Jedis jedis = null;
        Long ret = null;
        // without value
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.hincrBy(finallyKey, field, value);
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

    /**
     * Increment the number stored at field in the hash at key by a double
     * precision floating point value. If key does not exist, a new key holding
     * a hash is created. If field does not exist or holds a string, the value
     * is set to 0 before applying the operation. Since the value argument is
     * signed you can use this command to perform both increments and
     * decrements.
     * <p>
     * The range of values supported by HINCRBYFLOAT is limited to double
     * precision floating point values.
     * <p>
     * <b>Time complexity:</b> O(1)
     *
     * @param key
     * @param field
     * @param value
     * @return Double precision floating point reply The new value at field
     * after the increment operation.
     */
    @Override
    public Double hincrByFloat(final String key, final String field,
                               final double value) {
        return hincrByFloat(SafeEncoder.encode(key), SafeEncoder.encode(field),
                value);
    }

    /**
     * Increment the number stored at field in the hash at key by a double
     * precision floating point value. If key does not exist, a new key holding
     * a hash is created. If field does not exist or holds a string, the value
     * is set to 0 before applying the operation. Since the value argument is
     * signed you can use this command to perform both increments and
     * decrements.
     * <p>
     * The range of values supported by HINCRBYFLOAT is limited to double
     * precision floating point values.
     * <p>
     * <b>Time complexity:</b> O(1)
     *
     * @param key
     * @param field
     * @param value
     * @return Double precision floating point reply The new value at field
     * after the increment operation.
     */
    @Override
    public Double hincrByFloat(final byte[] key, final byte[] field,
                               final double value) {
        Jedis jedis = null;
        Double ret = null;
        // without value
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.hincrByFloat(finallyKey, field, value);
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

    /**
     * Test for existence of a specified field in a hash. <b>Time
     * complexity:</b> O(1)
     *
     * @param key
     * @param field
     * @return Return 1 if the hash stored at key contains the specified field.
     * Return 0 if the key is not found or the field is not present.
     */
    @Override
    public Boolean hexists(final String key, final String field) {
        return hexists(SafeEncoder.encode(key), SafeEncoder.encode(field));
    }

    /**
     * Test for existence of a specified field in a hash. <b>Time
     * complexity:</b> O(1)
     *
     * @param key
     * @param field
     * @return Return 1 if the hash stored at key contains the specified field.
     * Return 0 if the key is not found or the field is not present.
     */
    @Override
    public Boolean hexists(final byte[] key, final byte[] field) {
        Jedis jedis = null;
        Boolean ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.hexists(finallyKey, field);
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

    /**
     * Remove the specified field from an hash stored at key.
     * <p>
     * <b>Time complexity:</b> O(1)
     *
     * @param key
     * @param fields
     * @return If the field was present in the hash it is deleted and 1 is
     * returned, otherwise 0 is returned and no operation is performed.
     */
    @Override
    public Long hdel(final String key, final String... fields) {
        return hdel(SafeEncoder.encode(key), SafeEncoder.encodeMany(fields));
    }

    /**
     * Remove the specified field from an hash stored at key.
     * <p>
     * <b>Time complexity:</b> O(1)
     *
     * @param key
     * @param fields
     * @return If the field was present in the hash it is deleted and 1 is
     * returned, otherwise 0 is returned and no operation is performed.
     */
    @Override
    public Long hdel(final byte[] key, final byte[]... fields) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.hdel(finallyKey, fields);
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

    /**
     * Return the number of items in a hash.
     * <p>
     * <b>Time complexity:</b> O(1)
     *
     * @param key
     * @return The number of entries (fields) contained in the hash stored at
     * key. If the specified key does not exist, 0 is returned assuming
     * an empty hash.
     */
    @Override
    public Long hlen(final String key) {
        return hlen(SafeEncoder.encode(key));
    }

    /**
     * Return the number of items in a hash.
     * <p>
     * <b>Time complexity:</b> O(1)
     *
     * @param key
     * @return The number of entries (fields) contained in the hash stored at
     * key. If the specified key does not exist, 0 is returned assuming
     * an empty hash.
     */
    @Override
    public Long hlen(final byte[] key) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.hlen(finallyKey);
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

    /**
     * Return all the fields in a hash.
     * <p>
     * <b>Time complexity:</b> O(N), where N is the total number of entries
     *
     * @param key
     * @return All the fields names contained into a hash.
     */
    @Override
    public Set<String> hkeys(final String key) {
        return SafeEncoder.encodeSet(hkeys(SafeEncoder.encode(key)));
    }

    /**
     * Return all the fields in a hash.
     * <p>
     * <b>Time complexity:</b> O(N), where N is the total number of entries
     *
     * @param key
     * @return All the fields names contained into a hash.
     */
    @Override
    public Set<byte[]> hkeys(final byte[] key) {
        Jedis jedis = null;
        Set<byte[]> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.hkeys(finallyKey);
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

    /**
     * Return all the values in a hash.
     * <p>
     * <b>Time complexity:</b> O(N), where N is the total number of entries
     *
     * @param key
     * @return All the fields values contained into a hash.
     */
    @Override
    public List<String> hvals(final String key) {
        return SafeEncoder.encodeList(hvals(SafeEncoder.encode(key)));
    }

    /**
     * Return all the values in a hash.
     * <p>
     * <b>Time complexity:</b> O(N), where N is the total number of entries
     *
     * @param key
     * @return All the fields values contained into a hash.
     */
    @Override
    public List<byte[]> hvals(final byte[] key) {
        Jedis jedis = null;
        List<byte[]> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.hvals(finallyKey);
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

    /**
     * Return all the fields and associated values in a hash.
     * <p>
     * <b>Time complexity:</b> O(N), where N is the total number of entries
     *
     * @param key
     * @return All the fields and values contained into a hash.
     */
    @Override
    public Map<String, String> hgetAll(final String key) {
        return SafeEncoder.encodeByteMap(hgetAll(SafeEncoder.encode(key)));
    }

    /**
     * Return all the fields and associated values in a hash.
     * <p>
     * <b>Time complexity:</b> O(N), where N is the total number of entries
     *
     * @param key
     * @return All the fields and values contained into a hash.
     */
    @Override
    public Map<byte[], byte[]> hgetAll(final byte[] key) {
        Jedis jedis = null;
        Map<byte[], byte[]> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.hgetAll(finallyKey);
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

    /**
     * Add the string value to the head (LPUSH) or tail (RPUSH) of the list
     * stored at key. If the key does not exist an empty list is created just
     * before the append operation. If the key exists but is not a List an error
     * is returned.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param strings
     * @return Integer reply, specifically, the number of elements inside the
     * list after the push operation.
     */
    @Override
    public Long rpush(final String key, final String... strings) {
        return rpush(SafeEncoder.encode(key), SafeEncoder.encodeMany(strings));
    }

    /**
     * Add the string value to the head (LPUSH) or tail (RPUSH) of the list
     * stored at key. If the key does not exist an empty list is created just
     * before the append operation. If the key exists but is not a List an error
     * is returned.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param strings
     * @return Integer reply, specifically, the number of elements inside the
     * list after the push operation.
     */
    @Override
    public Long rpush(final byte[] key, final byte[]... strings) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis
                    .rpush(finallyKey, strings);
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

    /**
     * Add the string value to the head (LPUSH) or tail (RPUSH) of the list
     * stored at key. If the key does not exist an empty list is created just
     * before the append operation. If the key exists but is not a List an error
     * is returned.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param strings
     * @return Integer reply, specifically, the number of elements inside the
     * list after the push operation.
     */
    @Override
    public Long lpush(final String key, final String... strings) {
        return lpush(SafeEncoder.encode(key), SafeEncoder.encodeMany(strings));
    }

    /**
     * Add the string value to the head (LPUSH) or tail (RPUSH) of the list
     * stored at key. If the key does not exist an empty list is created just
     * before the append operation. If the key exists but is not a List an error
     * is returned.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @param strings
     * @return Integer reply, specifically, the number of elements inside the
     * list after the push operation.
     */
    @Override
    public Long lpush(final byte[] key, final byte[]... strings) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis
                    .lpush(finallyKey, strings);
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

    /**
     * Return the length of the list stored at the specified key. If the key
     * does not exist zero is returned (the same behaviour as for empty lists).
     * If the value stored at key is not a list an error is returned.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @return The length of the list.
     */
    @Override
    public Long llen(final String key) {
        return llen(SafeEncoder.encode(key));
    }

    /**
     * Return the length of the list stored at the specified key. If the key
     * does not exist zero is returned (the same behaviour as for empty lists).
     * If the value stored at key is not a list an error is returned.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @return The length of the list.
     */
    @Override
    public Long llen(final byte[] key) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.llen(finallyKey);
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

    /**
     * Return the specified elements of the list stored at the specified key.
     * Start and end are zero-based indexes. 0 is the first element of the list
     * (the list head), 1 the next element and so on.
     * <p>
     * For example LRANGE foobar 0 2 will return the first three elements of the
     * list.
     * <p>
     * start and end can also be negative numbers indicating offsets from the
     * end of the list. For example -1 is the last element of the list, -2 the
     * penultimate element and so on.
     * <p>
     * <b>Consistency with range functions in various programming languages</b>
     * <p>
     * Note that if you have a list of numbers from 0 to 100, LRANGE 0 10 will
     * return 11 elements, that is, rightmost item is included. This may or may
     * not be consistent with behavior of range-related functions in your
     * programming language of choice (think Ruby's Range.new, Array#slice or
     * Python's range() function).
     * <p>
     * LRANGE behavior is consistent with one of Tcl.
     * <p>
     * <b>Out-of-range indexes</b>
     * <p>
     * Indexes out of range will not produce an error: if start is over the end
     * of the list, or start > end, an empty list is returned. If end is over
     * the end of the list Redis will threat it just like the last element of
     * the list.
     * <p>
     * Time complexity: O(start+n) (with n being the length of the range and
     * start being the start offset)
     *
     * @param key
     * @param start
     * @param end
     * @return Multi bulk reply, specifically a list of elements in the
     * specified range.
     */
    @Override
    public List<String> lrange(final String key, final long start,
                               final long end) {
        return SafeEncoder.encodeList(lrange(SafeEncoder.encode(key), start,
                end));
    }

    /**
     * Return the specified elements of the list stored at the specified key.
     * Start and end are zero-based indexes. 0 is the first element of the list
     * (the list head), 1 the next element and so on.
     * <p>
     * For example LRANGE foobar 0 2 will return the first three elements of the
     * list.
     * <p>
     * start and end can also be negative numbers indicating offsets from the
     * end of the list. For example -1 is the last element of the list, -2 the
     * penultimate element and so on.
     * <p>
     * <b>Consistency with range functions in various programming languages</b>
     * <p>
     * Note that if you have a list of numbers from 0 to 100, LRANGE 0 10 will
     * return 11 elements, that is, rightmost item is included. This may or may
     * not be consistent with behavior of range-related functions in your
     * programming language of choice (think Ruby's Range.new, Array#slice or
     * Python's range() function).
     * <p>
     * LRANGE behavior is consistent with one of Tcl.
     * <p>
     * <b>Out-of-range indexes</b>
     * <p>
     * Indexes out of range will not produce an error: if start is over the end
     * of the list, or start > end, an empty list is returned. If end is over
     * the end of the list Redis will threat it just like the last element of
     * the list.
     * <p>
     * Time complexity: O(start+n) (with n being the length of the range and
     * start being the start offset)
     *
     * @param key
     * @param start
     * @param end
     * @return Multi bulk reply, specifically a list of elements in the
     * specified range.
     */
    @Override
    public List<byte[]> lrange(final byte[] key, final long start,
                               final long end) {
        Jedis jedis = null;
        List<byte[]> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.lrange(finallyKey, start, end);
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

    /**
     * Trim an existing list so that it will contain only the specified range of
     * elements specified. Start and end are zero-based indexes. 0 is the first
     * element of the list (the list head), 1 the next element and so on.
     * <p>
     * For example LTRIM foobar 0 2 will modify the list stored at foobar key so
     * that only the first three elements of the list will remain.
     * <p>
     * start and end can also be negative numbers indicating offsets from the
     * end of the list. For example -1 is the last element of the list, -2 the
     * penultimate element and so on.
     * <p>
     * Indexes out of range will not produce an error: if start is over the end
     * of the list, or start > end, an empty list is left as value. If end over
     * the end of the list Redis will threat it just like the last element of
     * the list.
     * <p>
     * Hint: the obvious use of LTRIM is together with LPUSH/RPUSH. For example:
     * <p>
     * {@code lpush("mylist", "someelement"); ltrim("mylist", 0, 99); * }
     * <p>
     * The above two commands will push elements in the list taking care that
     * the list will not grow without limits. This is very useful when using
     * Redis to store logs for example. It is important to note that when used
     * in this way LTRIM is an O(1) operation because in the average case just
     * one element is removed from the tail of the list.
     * <p>
     * Time complexity: O(n) (with n being len of list - len of range)
     *
     * @param key
     * @param start
     * @param end
     * @return Status code reply
     */
    @Override
    public String ltrim(final String key, final long start, final long end) {
        return ltrim(SafeEncoder.encode(key), start, end);
    }

    /**
     * Trim an existing list so that it will contain only the specified range of
     * elements specified. Start and end are zero-based indexes. 0 is the first
     * element of the list (the list head), 1 the next element and so on.
     * <p>
     * For example LTRIM foobar 0 2 will modify the list stored at foobar key so
     * that only the first three elements of the list will remain.
     * <p>
     * start and end can also be negative numbers indicating offsets from the
     * end of the list. For example -1 is the last element of the list, -2 the
     * penultimate element and so on.
     * <p>
     * Indexes out of range will not produce an error: if start is over the end
     * of the list, or start > end, an empty list is left as value. If end over
     * the end of the list Redis will threat it just like the last element of
     * the list.
     * <p>
     * Hint: the obvious use of LTRIM is together with LPUSH/RPUSH. For example:
     * <p>
     * {@code lpush("mylist", "someelement"); ltrim("mylist", 0, 99); * }
     * <p>
     * The above two commands will push elements in the list taking care that
     * the list will not grow without limits. This is very useful when using
     * Redis to store logs for example. It is important to note that when used
     * in this way LTRIM is an O(1) operation because in the average case just
     * one element is removed from the tail of the list.
     * <p>
     * Time complexity: O(n) (with n being len of list - len of range)
     *
     * @param key
     * @param start
     * @param end
     * @return Status code reply
     */
    @Override
    public String ltrim(final byte[] key, final long start, final long end) {
        Jedis jedis = null;
        String ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.ltrim(finallyKey, start, end);
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

    /**
     * Return the specified element of the list stored at the specified key. 0
     * is the first element, 1 the second and so on. Negative indexes are
     * supported, for example -1 is the last element, -2 the penultimate and so
     * on.
     * <p>
     * If the value stored at key is not of list type an error is returned. If
     * the index is out of range a 'nil' reply is returned.
     * <p>
     * Note that even if the average time complexity is O(n) asking for the
     * first or the last element of the list is O(1).
     * <p>
     * Time complexity: O(n) (with n being the length of the list)
     *
     * @param key
     * @param index
     * @return Bulk reply, specifically the requested element
     */
    @Override
    public String lindex(final String key, final long index) {
        return SafeEncoder.encode(lindex(SafeEncoder.encode(key), index));
    }

    /**
     * Return the specified element of the list stored at the specified key. 0
     * is the first element, 1 the second and so on. Negative indexes are
     * supported, for example -1 is the last element, -2 the penultimate and so
     * on.
     * <p>
     * If the value stored at key is not of list type an error is returned. If
     * the index is out of range a 'nil' reply is returned.
     * <p>
     * Note that even if the average time complexity is O(n) asking for the
     * first or the last element of the list is O(1).
     * <p>
     * Time complexity: O(n) (with n being the length of the list)
     *
     * @param key
     * @param index
     * @return Bulk reply, specifically the requested element
     */
    @Override
    public byte[] lindex(final byte[] key, final long index) {
        Jedis jedis = null;
        byte[] ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.lindex(finallyKey, index);
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

    /**
     * Set a new value as the element at index position of the List at key.
     * <p>
     * Out of range indexes will generate an error.
     * <p>
     * Similarly to other list commands accepting indexes, the index can be
     * negative to access elements starting from the end of the list. So -1 is
     * the last element, -2 is the penultimate, and so forth.
     * <p>
     * <b>Time complexity:</b>
     * <p>
     * O(N) (with N being the length of the list), setting the first or last
     * elements of the list is O(1).
     *
     * @param key
     * @param index
     * @param value
     * @return Status code reply
     * @see #lindex(String, long)
     */
    @Override
    public String lset(final String key, final long index, final String value) {
        return lset(SafeEncoder.encode(key), index, SafeEncoder.encode(value));
    }

    /**
     * Set a new value as the element at index position of the List at key.
     * <p>
     * Out of range indexes will generate an error.
     * <p>
     * Similarly to other list commands accepting indexes, the index can be
     * negative to access elements starting from the end of the list. So -1 is
     * the last element, -2 is the penultimate, and so forth.
     * <p>
     * <b>Time complexity:</b>
     * <p>
     * O(N) (with N being the length of the list), setting the first or last
     * elements of the list is O(1).
     *
     * @param key
     * @param index
     * @param value
     * @return Status code reply
     * @see #lindex(String, long)
     */
    @Override
    public String lset(final byte[] key, final long index, final byte[] value) {
        Jedis jedis = null;
        String ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.lset(finallyKey, index,
                    value);
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

    /**
     * Remove the first count occurrences of the value element from the list. If
     * count is zero all the elements are removed. If count is negative elements
     * are removed from tail to head, instead to go from head to tail that is
     * the normal behaviour. So for example LREM with count -2 and hello as
     * value to remove against the list (a,b,c,hello,x,hello,hello) will lave
     * the list (a,b,c,hello,x). The number of removed elements is returned as
     * an integer, see below for more information about the returned value. Note
     * that non existing keys are considered like empty lists by LREM, so LREM
     * against non existing keys will always return 0.
     * <p>
     * Time complexity: O(N) (with N being the length of the list)
     *
     * @param key
     * @param count
     * @param value
     * @return Integer Reply, specifically: The number of removed elements if
     * the operation succeeded
     */
    @Override
    public Long lrem(final String key, final long count, final String value) {
        return lrem(SafeEncoder.encode(key), count, SafeEncoder.encode(value));
    }

    /**
     * Remove the first count occurrences of the value element from the list. If
     * count is zero all the elements are removed. If count is negative elements
     * are removed from tail to head, instead to go from head to tail that is
     * the normal behaviour. So for example LREM with count -2 and hello as
     * value to remove against the list (a,b,c,hello,x,hello,hello) will lave
     * the list (a,b,c,hello,x). The number of removed elements is returned as
     * an integer, see below for more information about the returned value. Note
     * that non existing keys are considered like empty lists by LREM, so LREM
     * against non existing keys will always return 0.
     * <p>
     * Time complexity: O(N) (with N being the length of the list)
     *
     * @param key
     * @param count
     * @param value
     * @return Integer Reply, specifically: The number of removed elements if
     * the operation succeeded
     */
    @Override
    public Long lrem(final byte[] key, final long count, final byte[] value) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.lrem(finallyKey, count, value);
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

    /**
     * Atomically return and remove the first (LPOP) or last (RPOP) element of
     * the list. For example if the list contains the elements "a","b","c" LPOP
     * will return "a" and the list will become "b","c".
     * <p>
     * If the key does not exist or the list is already empty the special value
     * 'nil' is returned.
     *
     * @param key
     * @return Bulk reply
     * @see #rpop(String)
     */
    @Override
    public String lpop(final String key) {
        return SafeEncoder.encode(lpop(SafeEncoder.encode(key)));
    }

    /**
     * Atomically return and remove the first (LPOP) or last (RPOP) element of
     * the list. For example if the list contains the elements "a","b","c" LPOP
     * will return "a" and the list will become "b","c".
     * <p>
     * If the key does not exist or the list is already empty the special value
     * 'nil' is returned.
     *
     * @param key
     * @return Bulk reply
     * @see #rpop(String)
     */
    @Override
    public byte[] lpop(final byte[] key) {
        Jedis jedis = null;
        byte[] ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.lpop(finallyKey);
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

    /**
     * Atomically return and remove the first (LPOP) or last (RPOP) element of
     * the list. For example if the list contains the elements "a","b","c" RPOP
     * will return "c" and the list will become "a","b".
     * <p>
     * If the key does not exist or the list is already empty the special value
     * 'nil' is returned.
     *
     * @param key
     * @return Bulk reply
     * @see #lpop(String)
     */
    @Override
    public String rpop(final String key) {
        return SafeEncoder.encode(rpop(SafeEncoder.encode(key)));
    }

    /**
     * Atomically return and remove the first (LPOP) or last (RPOP) element of
     * the list. For example if the list contains the elements "a","b","c" RPOP
     * will return "c" and the list will become "a","b".
     * <p>
     * If the key does not exist or the list is already empty the special value
     * 'nil' is returned.
     *
     * @param key
     * @return Bulk reply
     * @see #lpop(String)
     */
    @Override
    public byte[] rpop(final byte[] key) {
        Jedis jedis = null;
        byte[] ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.rpop(finallyKey);
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

    /**
     * Atomically return and remove the last (tail) element of the srckey list,
     * and push the element as the first (head) element of the dstkey list. For
     * example if the source list contains the elements "a","b","c" and the
     * destination list contains the elements "foo","bar" after an RPOPLPUSH
     * command the content of the two lists will be "a","b" and "c","foo","bar".
     * <p>
     * If the key does not exist or the list is already empty the special value
     * 'nil' is returned. If the srckey and dstkey are the same the operation is
     * equivalent to removing the last element from the list and pusing it as
     * first element of the list, so it's a "list rotation" command.
     * <p>
     * Time complexity: O(1)
     *
     * @param srckey
     * @param dstkey
     * @return Bulk reply
     */
    @Override
    public String rpoplpush(final String srckey, final String dstkey) {
        return SafeEncoder.encode(rpoplpush(SafeEncoder.encode(srckey),
                SafeEncoder.encode(dstkey)));
    }

    /**
     * Atomically return and remove the last (tail) element of the srckey list,
     * and push the element as the first (head) element of the dstkey list. For
     * example if the source list contains the elements "a","b","c" and the
     * destination list contains the elements "foo","bar" after an RPOPLPUSH
     * command the content of the two lists will be "a","b" and "c","foo","bar".
     * <p>
     * If the key does not exist or the list is already empty the special value
     * 'nil' is returned. If the srckey and dstkey are the same the operation is
     * equivalent to removing the last element from the list and pusing it as
     * first element of the list, so it's a "list rotation" command.
     * <p>
     * Time complexity: O(1)
     *
     * @param srckey
     * @param dstkey
     * @return Bulk reply
     */
    @Override
    public byte[] rpoplpush(final byte[] srckey, final byte[] dstkey) {
        Jedis jedis = null;
        byte[] ret = null;
        try {
            jedis = pool.getResource();
            ret = jedis.rpoplpush(srckey, dstkey);
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

    /**
     * Add the specified member to the set value stored at key. If member is
     * already a member of the set no operation is performed. If key does not
     * exist a new set with the specified member as sole member is created. If
     * the key exists but does not hold a set value an error is returned.
     * <p>
     * Time complexity O(1)
     *
     * @param key
     * @param members
     * @return Integer reply, specifically: 1 if the new element was added 0 if
     * the element was already a member of the set
     */
    @Override
    public Long sadd(final String key, final String... members) {
        return sadd(SafeEncoder.encode(key), SafeEncoder.encodeMany(members));
    }

    /**
     * Add the specified member to the set value stored at key. If member is
     * already a member of the set no operation is performed. If key does not
     * exist a new set with the specified member as sole member is created. If
     * the key exists but does not hold a set value an error is returned.
     * <p>
     * Time complexity O(1)
     *
     * @param key
     * @param members
     * @return Integer reply, specifically: 1 if the new element was added 0 if
     * the element was already a member of the set
     */
    @Override
    public Long sadd(final byte[] key, final byte[]... members) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.sadd(finallyKey, members);
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

    /**
     * Return all the members (elements) of the set value stored at key. This is
     * just syntax glue for {@link #sinter(String...) SINTER}.
     * <p>
     * Time complexity O(N)
     *
     * @param key
     * @return Multi bulk reply
     */
    @Override
    public Set<String> smembers(final String key) {
        return SafeEncoder.encodeSet(smembers(SafeEncoder.encode(key)));
    }

    /**
     * Return all the members (elements) of the set value stored at key. This is
     * just syntax glue for {@link #sinter(String...) SINTER}.
     * <p>
     * Time complexity O(N)
     *
     * @param key
     * @return Multi bulk reply
     */
    @Override
    public Set<byte[]> smembers(final byte[] key) {
        Jedis jedis = null;
        Set<byte[]> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.smembers(finallyKey);
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

    /**
     * Remove the specified member from the set value stored at key. If member
     * was not a member of the set no operation is performed. If key does not
     * hold a set value an error is returned.
     * <p>
     * Time complexity O(1)
     *
     * @param key
     * @param members
     * @return Integer reply, specifically: 1 if the new element was removed 0
     * if the new element was not a member of the set
     */
    @Override
    public Long srem(final String key, final String... members) {
        return srem(SafeEncoder.encode(key), SafeEncoder.encodeMany(members));
    }

    /**
     * Remove the specified member from the set value stored at key. If member
     * was not a member of the set no operation is performed. If key does not
     * hold a set value an error is returned.
     * <p>
     * Time complexity O(1)
     *
     * @param key
     * @param members
     * @return Integer reply, specifically: 1 if the new element was removed 0
     * if the new element was not a member of the set
     */
    @Override
    public Long srem(final byte[] key, final byte[]... members) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.srem(finallyKey, members);
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

    /**
     * Remove a random element from a Set returning it as return value. If the
     * Set is empty or the key does not exist, a nil object is returned.
     * <p>
     * The {@link #srandmember(String)} command does a similar work but the
     * returned element is not removed from the Set.
     * <p>
     * Time complexity O(1)
     *
     * @param key
     * @return Bulk reply
     */
    @Override
    public String spop(final String key) {
        return SafeEncoder.encode(spop(SafeEncoder.encode(key)));
    }

    /**
     * Remove a random element from a Set returning it as return value. If the
     * Set is empty or the key does not exist, a nil object is returned.
     * <p>
     * The {@link #srandmember(String)} command does a similar work but the
     * returned element is not removed from the Set.
     * <p>
     * Time complexity O(1)
     *
     * @param key
     * @return Bulk reply
     */
    @Override
    public byte[] spop(final byte[] key) {
        Jedis jedis = null;
        byte[] ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.spop(finallyKey);
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

    /**
     * Move the specifided member from the set at srckey to the set at dstkey.
     * This operation is atomic, in every given moment the element will appear
     * to be in the source or destination set for accessing clients.
     * <p>
     * If the source set does not exist or does not contain the specified
     * element no operation is performed and zero is returned, otherwise the
     * element is removed from the source set and added to the destination set.
     * On success one is returned, even if the element was already present in
     * the destination set.
     * <p>
     * An error is raised if the source or destination keys contain a non Set
     * value.
     * <p>
     * Time complexity O(1)
     *
     * @param srckey
     * @param dstkey
     * @param member
     * @return Integer reply, specifically: 1 if the element was moved 0 if the
     * element was not found on the first set and no operation was
     * performed
     */
    @Override
    public Long smove(final String srckey, final String dstkey,
                      final String member) {
        return smove(SafeEncoder.encode(srckey), SafeEncoder.encode(dstkey),
                SafeEncoder.encode(member));
    }

    /**
     * Move the specifided member from the set at srckey to the set at dstkey.
     * This operation is atomic, in every given moment the element will appear
     * to be in the source or destination set for accessing clients.
     * <p>
     * If the source set does not exist or does not contain the specified
     * element no operation is performed and zero is returned, otherwise the
     * element is removed from the source set and added to the destination set.
     * On success one is returned, even if the element was already present in
     * the destination set.
     * <p>
     * An error is raised if the source or destination keys contain a non Set
     * value.
     * <p>
     * Time complexity O(1)
     *
     * @param srckey
     * @param dstkey
     * @param member
     * @return Integer reply, specifically: 1 if the element was moved 0 if the
     * element was not found on the first set and no operation was
     * performed
     */
    @Override
    public Long smove(final byte[] srckey, final byte[] dstkey,
                      final byte[] member) {
        Jedis jedis = null;
        Long ret = null;
        try {
            jedis = pool.getResource();
            ret = jedis.smove(srckey, dstkey, member);
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

    /**
     * Return the set cardinality (number of elements). If the key does not
     * exist 0 is returned, like for empty sets.
     *
     * @param key
     * @return Integer reply, specifically: the cardinality (number of elements)
     * of the set as an integer.
     */
    @Override
    public Long scard(final String key) {
        return scard(SafeEncoder.encode(key));
    }

    /**
     * Return the set cardinality (number of elements). If the key does not
     * exist 0 is returned, like for empty sets.
     *
     * @param key
     * @return Integer reply, specifically: the cardinality (number of elements)
     * of the set as an integer.
     */
    @Override
    public Long scard(final byte[] key) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.scard(finallyKey);
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

    /**
     * Return 1 if member is a member of the set stored at key, otherwise 0 is
     * returned.
     * <p>
     * Time complexity O(1)
     *
     * @param key
     * @param member
     * @return Integer reply, specifically: 1 if the element is a member of the
     * set 0 if the element is not a member of the set OR if the key
     * does not exist
     */
    @Override
    public Boolean sismember(final String key, final String member) {
        return sismember(SafeEncoder.encode(key), SafeEncoder.encode(member));
    }

    /**
     * Return 1 if member is a member of the set stored at key, otherwise 0 is
     * returned.
     * <p>
     * Time complexity O(1)
     *
     * @param key
     * @param member
     * @return Integer reply, specifically: 1 if the element is a member of the
     * set 0 if the element is not a member of the set OR if the key
     * does not exist
     */
    @Override
    public Boolean sismember(final byte[] key, final byte[] member) {
        Jedis jedis = null;
        Boolean ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.sismember(finallyKey, member);
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

    /**
     * Return the members of a set resulting from the intersection of all the
     * sets hold at the specified keys. Like in
     * {@link #lrange(String, long, long) LRANGE} the result is sent to the
     * client as a multi-bulk reply (see the protocol specification for more
     * information). If just a single key is specified, then this command
     * produces the same result as {@link #smembers(String) SMEMBERS}. Actually
     * SMEMBERS is just syntax sugar for SINTER.
     * <p>
     * Non existing keys are considered like empty sets, so if one of the keys
     * is missing an empty set is returned (since the intersection with an empty
     * set always is an empty set).
     * <p>
     * Time complexity O(N*M) worst case where N is the cardinality of the
     * smallest set and M the number of sets
     *
     * @param keys
     * @return Multi bulk reply, specifically the list of common elements.
     */
    @Override
    public Set<String> sinter(final String... keys) {
        return SafeEncoder.encodeSet(sinter(SafeEncoder.encodeMany(keys)));
    }

    /**
     * Return the members of a set resulting from the intersection of all the
     * sets hold at the specified keys. Like in
     * {@link #lrange(String, long, long) LRANGE} the result is sent to the
     * client as a multi-bulk reply (see the protocol specification for more
     * information). If just a single key is specified, then this command
     * produces the same result as {@link #smembers(String) SMEMBERS}. Actually
     * SMEMBERS is just syntax sugar for SINTER.
     * <p>
     * Non existing keys are considered like empty sets, so if one of the keys
     * is missing an empty set is returned (since the intersection with an empty
     * set always is an empty set).
     * <p>
     * Time complexity O(N*M) worst case where N is the cardinality of the
     * smallest set and M the number of sets
     *
     * @param keys
     * @return Multi bulk reply, specifically the list of common elements.
     */
    @Override
    public Set<byte[]> sinter(final byte[]... keys) {
        Jedis jedis = null;
        Set<byte[]> ret = null;
        byte[][] finallyKeys = WRedisUtil.concatKeys(clientConfig, keys);
        try {
            jedis = pool.getResource();
            ret = jedis.sinter(finallyKeys);
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

    /**
     * This commnad works exactly like {@link #sinter(String...) SINTER} but
     * instead of being returned the resulting set is sotred as dstkey.
     * <p>
     * Time complexity O(N*M) worst case where N is the cardinality of the
     * smallest set and M the number of sets
     *
     * @param dstkey
     * @param keys
     * @return Status code reply
     */
    @Override
    public Long sinterstore(final String dstkey, final String... keys) {
        return sinterstore(SafeEncoder.encode(dstkey),
                SafeEncoder.encodeMany(keys));
    }

    /**
     * This commnad works exactly like {@link #sinter(String...) SINTER} but
     * instead of being returned the resulting set is sotred as dstkey.
     * <p>
     * Time complexity O(N*M) worst case where N is the cardinality of the
     * smallest set and M the number of sets
     *
     * @param dstkey
     * @param keys
     * @return Status code reply
     */
    @Override
    public Long sinterstore(final byte[] dstkey, final byte[]... keys) {
        Jedis jedis = null;
        Long ret = null;
        try {
            jedis = pool.getResource();
            ret = jedis.sinterstore(dstkey, keys);
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

    /**
     * Return the members of a set resulting from the union of all the sets hold
     * at the specified keys. Like in {@link #lrange(String, long, long) LRANGE}
     * the result is sent to the client as a multi-bulk reply (see the protocol
     * specification for more information). If just a single key is specified,
     * then this command produces the same result as {@link #smembers(String)
     * SMEMBERS}.
     * <p>
     * Non existing keys are considered like empty sets.
     * <p>
     * Time complexity O(N) where N is the total number of elements in all the
     * provided sets
     *
     * @param keys
     * @return Multi bulk reply, specifically the list of common elements.
     */
    @Override
    public Set<String> sunion(final String... keys) {
        return SafeEncoder.encodeSet(sunion(SafeEncoder.encodeMany(keys)));
    }

    /**
     * Return the members of a set resulting from the union of all the sets hold
     * at the specified keys. Like in {@link #lrange(String, long, long) LRANGE}
     * the result is sent to the client as a multi-bulk reply (see the protocol
     * specification for more information). If just a single key is specified,
     * then this command produces the same result as {@link #smembers(String)
     * SMEMBERS}.
     * <p>
     * Non existing keys are considered like empty sets.
     * <p>
     * Time complexity O(N) where N is the total number of elements in all the
     * provided sets
     *
     * @param keys
     * @return Multi bulk reply, specifically the list of common elements.
     */
    @Override
    public Set<byte[]> sunion(final byte[]... keys) {
        Jedis jedis = null;
        Set<byte[]> ret = null;
        byte[][] finallyKeys = WRedisUtil.concatKeys(clientConfig, keys);
        try {
            jedis = pool.getResource();
            ret = jedis.sunion(finallyKeys);
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

    /**
     * This command works exactly like {@link #sunion(String...) SUNION} but
     * instead of being returned the resulting set is stored as dstkey. Any
     * existing value in dstkey will be over-written.
     * <p>
     * Time complexity O(N) where N is the total number of elements in all the
     * provided sets
     *
     * @param dstkey
     * @param keys
     * @return Status code reply
     */
    @Override
    public Long sunionstore(final String dstkey, final String... keys) {
        return sunionstore(SafeEncoder.encode(dstkey),
                SafeEncoder.encodeMany(keys));
    }

    /**
     * This command works exactly like {@link #sunion(String...) SUNION} but
     * instead of being returned the resulting set is stored as dstkey. Any
     * existing value in dstkey will be over-written.
     * <p>
     * Time complexity O(N) where N is the total number of elements in all the
     * provided sets
     *
     * @param dstkey
     * @param keys
     * @return Status code reply
     */
    @Override
    public Long sunionstore(final byte[] dstkey, final byte[]... keys) {
        Jedis jedis = null;
        Long ret = null;
        try {
            jedis = pool.getResource();
            ret = jedis.sunionstore(dstkey, keys);
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

    /**
     * Return the difference between the Set stored at key1 and all the Sets
     * key2, ..., keyN
     * <p>
     * <b>Example:</b>
     *
     * <pre>
     * key1 = [x, a, b, c]
     * key2 = [c]
     * key3 = [a, d]
     * SDIFF key1,key2,key3 => [x, b]
     * </pre>
     * <p>
     * Non existing keys are considered like empty sets.
     * <p>
     * <b>Time complexity:</b>
     * <p>
     * O(N) with N being the total number of elements of all the sets
     *
     * @param keys
     * @return Return the members of a set resulting from the difference between
     * the first set provided and all the successive sets.
     */
    @Override
    public Set<String> sdiff(final String... keys) {
        return SafeEncoder.encodeSet(sdiff(SafeEncoder.encodeMany(keys)));
    }

    /**
     * Return the difference between the Set stored at key1 and all the Sets
     * key2, ..., keyN
     * <p>
     * <b>Example:</b>
     *
     * <pre>
     * key1 = [x, a, b, c]
     * key2 = [c]
     * key3 = [a, d]
     * SDIFF key1,key2,key3 => [x, b]
     * </pre>
     * <p>
     * Non existing keys are considered like empty sets.
     * <p>
     * <b>Time complexity:</b>
     * <p>
     * O(N) with N being the total number of elements of all the sets
     *
     * @param keys
     * @return Return the members of a set resulting from the difference between
     * the first set provided and all the successive sets.
     */
    @Override
    public Set<byte[]> sdiff(final byte[]... keys) {
        Jedis jedis = null;
        Set<byte[]> ret = null;
        byte[][] finallyKeys = WRedisUtil.concatKeys(clientConfig, keys);
        try {
            jedis = pool.getResource();
            ret = jedis.sdiff(finallyKeys);
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

    /**
     * This command works exactly like {@link #sdiff(String...) SDIFF} but
     * instead of being returned the resulting set is stored in dstkey.
     *
     * @param dstkey
     * @param keys
     * @return Status code reply
     */
    @Override
    public Long sdiffstore(final String dstkey, final String... keys) {
        return sdiffstore(SafeEncoder.encode(dstkey),
                SafeEncoder.encodeMany(keys));
    }

    /**
     * This command works exactly like {@link #sdiff(String...) SDIFF} but
     * instead of being returned the resulting set is stored in dstkey.
     *
     * @param dstkey
     * @param keys
     * @return Status code reply
     */
    @Override
    public Long sdiffstore(final byte[] dstkey, final byte[]... keys) {
        Jedis jedis = null;
        Long ret = null;
        try {
            jedis = pool.getResource();
            ret = jedis.sdiffstore(dstkey, keys);
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

    /**
     * Return a random element from a Set, without removing the element. If the
     * Set is empty or the key does not exist, a nil object is returned.
     * <p>
     * The SPOP command does a similar work but the returned element is popped
     * (removed) from the Set.
     * <p>
     * Time complexity O(1)
     *
     * @param key
     * @return Bulk reply
     */
    @Override
    public String srandmember(final String key) {
        return SafeEncoder.encode(srandmember(SafeEncoder.encode(key)));
    }

    /**
     * Return a random element from a Set, without removing the element. If the
     * Set is empty or the key does not exist, a nil object is returned.
     * <p>
     * The SPOP command does a similar work but the returned element is popped
     * (removed) from the Set.
     * <p>
     * Time complexity O(1)
     *
     * @param key
     * @return Bulk reply
     */
    @Override
    public byte[] srandmember(final byte[] key) {
        Jedis jedis = null;
        byte[] ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.srandmember(finallyKey);
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
    public List<String> srandmember(final String key, final int count) {
        return SafeEncoder.encodeList(srandmember(SafeEncoder.encode(key),
                count));
    }

    @Override
    public List<byte[]> srandmember(final byte[] key, final int count) {
        Jedis jedis = null;
        List<byte[]> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.srandmember(finallyKey, count);
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

    /**
     * Add the specified member having the specifeid score to the sorted set
     * stored at key. If member is already a member of the sorted set the score
     * is updated, and the element reinserted in the right position to ensure
     * sorting. If key does not exist a new sorted set with the specified member
     * as sole member is crated. If the key exists but does not hold a sorted
     * set value an error is returned.
     * <p>
     * The score value can be the string representation of a double precision
     * floating point number.
     * <p>
     * Time complexity O(log(N)) with N being the number of elements in the
     * sorted set
     *
     * @param key
     * @param score
     * @param member
     * @return Integer reply, specifically: 1 if the new element was added 0 if
     * the element was already a member of the sorted set and the score
     * was updated
     */
    @Override
    public Long zadd(final String key, final double score, final String member) {
        return zadd(SafeEncoder.encode(key), score, SafeEncoder.encode(member));
    }

    /**
     * Add the specified member having the specifeid score to the sorted set
     * stored at key. If member is already a member of the sorted set the score
     * is updated, and the element reinserted in the right position to ensure
     * sorting. If key does not exist a new sorted set with the specified member
     * as sole member is crated. If the key exists but does not hold a sorted
     * set value an error is returned.
     * <p>
     * The score value can be the string representation of a double precision
     * floating point number.
     * <p>
     * Time complexity O(log(N)) with N being the number of elements in the
     * sorted set
     *
     * @param key
     * @param score
     * @param member
     * @return Integer reply, specifically: 1 if the new element was added 0 if
     * the element was already a member of the sorted set and the score
     * was updated
     */
    @Override
    public Long zadd(final byte[] key, final double score, final byte[] member) {
        Jedis jedis = null;
        Long ret = null;
        // TODO check the value
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zadd(finallyKey, score, member);
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
    public Long zadd(final String key, final Map<String, Double> scoreMembers) {
        return zadd(SafeEncoder.encode(key),
                SafeEncoder.encodeMapWithDouble(scoreMembers));
    }

    @Override
    public Long zadd(final byte[] key, final Map<byte[], Double> scoreMembers) {
        Jedis jedis = null;
        Long ret = null;
        // skip double value
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zadd(finallyKey, scoreMembers);
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
    public Set<String> zrange(final String key, final long start, final long end) {
        return SafeEncoder
                .encodeSet(zrange(SafeEncoder.encode(key), start, end));
    }

    @Override
    public Set<byte[]> zrange(final byte[] key, final long start, final long end) {
        Jedis jedis = null;
        Set<byte[]> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zrange(finallyKey, start, end);
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

    /**
     * Remove the specified member from the sorted set value stored at key. If
     * member was not a member of the set no operation is performed. If key does
     * not not hold a set value an error is returned.
     * <p>
     * Time complexity O(log(N)) with N being the number of elements in the
     * sorted set
     *
     * @param key
     * @param members
     * @return Integer reply, specifically: 1 if the new element was removed 0
     * if the new element was not a member of the set
     */
    @Override
    public Long zrem(final String key, final String... members) {
        return zrem(SafeEncoder.encode(key), SafeEncoder.encodeMany(members));
    }

    /**
     * Remove the specified member from the sorted set value stored at key. If
     * member was not a member of the set no operation is performed. If key does
     * not not hold a set value an error is returned.
     * <p>
     * Time complexity O(log(N)) with N being the number of elements in the
     * sorted set
     *
     * @param key
     * @param members
     * @return Integer reply, specifically: 1 if the new element was removed 0
     * if the new element was not a member of the set
     */
    @Override
    public Long zrem(final byte[] key, final byte[]... members) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zrem(finallyKey, members);
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

    /**
     * If member already exists in the sorted set adds the increment to its
     * score and updates the position of the element in the sorted set
     * accordingly. If member does not already exist in the sorted set it is
     * added with increment as score (that is, like if the previous score was
     * virtually zero). If key does not exist a new sorted set with the
     * specified member as sole member is crated. If the key exists but does not
     * hold a sorted set value an error is returned.
     * <p>
     * The score value can be the string representation of a double precision
     * floating point number. It's possible to provide a negative value to
     * perform a decrement.
     * <p>
     * For an introduction to sorted sets check the Introduction to Redis data
     * types page.
     * <p>
     * Time complexity O(log(N)) with N being the number of elements in the
     * sorted set
     *
     * @param key
     * @param score
     * @param member
     * @return The new score
     */
    @Override
    public Double zincrby(final String key, final double score,
                          final String member) {
        return zincrby(SafeEncoder.encode(key), score,
                SafeEncoder.encode(member));
    }

    /**
     * If member already exists in the sorted set adds the increment to its
     * score and updates the position of the element in the sorted set
     * accordingly. If member does not already exist in the sorted set it is
     * added with increment as score (that is, like if the previous score was
     * virtually zero). If key does not exist a new sorted set with the
     * specified member as sole member is crated. If the key exists but does not
     * hold a sorted set value an error is returned.
     * <p>
     * The score value can be the string representation of a double precision
     * floating point number. It's possible to provide a negative value to
     * perform a decrement.
     * <p>
     * For an introduction to sorted sets check the Introduction to Redis data
     * types page.
     * <p>
     * Time complexity O(log(N)) with N being the number of elements in the
     * sorted set
     *
     * @param key
     * @param score
     * @param member
     * @return The new score
     */
    @Override
    public Double zincrby(final byte[] key, final double score,
                          final byte[] member) {
        Jedis jedis = null;
        Double ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zincrby(finallyKey, score, member);
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

    /**
     * Return the rank (or index) or member in the sorted set at key, with
     * scores being ordered from low to high.
     * <p>
     * When the given member does not exist in the sorted set, the special value
     * 'nil' is returned. The returned rank (or index) of the member is 0-based
     * for both commands.
     * <p>
     * <b>Time complexity:</b>
     * <p>
     * O(log(N))
     *
     * @param key
     * @param member
     * @return Integer reply or a nil bulk reply, specifically: the rank of the
     * element as an integer reply if the element exists. A nil bulk
     * reply if there is no such element.
     * @see #zrevrank(String, String)
     */
    @Override
    public Long zrank(final String key, final String member) {
        return zrank(SafeEncoder.encode(key), SafeEncoder.encode(member));
    }

    /**
     * Return the rank (or index) or member in the sorted set at key, with
     * scores being ordered from low to high.
     * <p>
     * When the given member does not exist in the sorted set, the special value
     * 'nil' is returned. The returned rank (or index) of the member is 0-based
     * for both commands.
     * <p>
     * <b>Time complexity:</b>
     * <p>
     * O(log(N))
     *
     * @param key
     * @param member
     * @return Integer reply or a nil bulk reply, specifically: the rank of the
     * element as an integer reply if the element exists. A nil bulk
     * reply if there is no such element.
     * @see #zrevrank(String, String)
     */
    @Override
    public Long zrank(final byte[] key, final byte[] member) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zrank(finallyKey, member);
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

    /**
     * Return the rank (or index) or member in the sorted set at key, with
     * scores being ordered from high to low.
     * <p>
     * When the given member does not exist in the sorted set, the special value
     * 'nil' is returned. The returned rank (or index) of the member is 0-based
     * for both commands.
     * <p>
     * <b>Time complexity:</b>
     * <p>
     * O(log(N))
     *
     * @param key
     * @param member
     * @return Integer reply or a nil bulk reply, specifically: the rank of the
     * element as an integer reply if the element exists. A nil bulk
     * reply if there is no such element.
     * @see #zrank(String, String)
     */
    @Override
    public Long zrevrank(final String key, final String member) {
        return zrevrank(SafeEncoder.encode(key), SafeEncoder.encode(member));
    }

    /**
     * Return the rank (or index) or member in the sorted set at key, with
     * scores being ordered from high to low.
     * <p>
     * When the given member does not exist in the sorted set, the special value
     * 'nil' is returned. The returned rank (or index) of the member is 0-based
     * for both commands.
     * <p>
     * <b>Time complexity:</b>
     * <p>
     * O(log(N))
     *
     * @param key
     * @param member
     * @return Integer reply or a nil bulk reply, specifically: the rank of the
     * element as an integer reply if the element exists. A nil bulk
     * reply if there is no such element.
     * @see #zrank(String, String)
     */
    @Override
    public Long zrevrank(final byte[] key, final byte[] member) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zrevrank(finallyKey, member);
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
    public Set<String> zrevrange(final String key, final long start,
                                 final long end) {
        return SafeEncoder.encodeSet(zrevrange(SafeEncoder.encode(key), start,
                end));
    }

    @Override
    public Set<byte[]> zrevrange(final byte[] key, final long start,
                                 final long end) {
        Jedis jedis = null;
        Set<byte[]> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zrevrange(finallyKey, start, end);
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
    public Set<Tuple> zrangeWithScores(final String key, final long start,
                                       final long end) {
        return zrangeWithScores(SafeEncoder.encode(key), start, end);
    }

    @Override
    public Set<Tuple> zrangeWithScores(final byte[] key, final long start,
                                       final long end) {
        Jedis jedis = null;
        Set<Tuple> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zrangeWithScores(finallyKey, start, end);
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
    public Set<Tuple> zrevrangeWithScores(final String key, final long start,
                                          final long end) {
        return zrevrangeWithScores(SafeEncoder.encode(key), start, end);
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(final byte[] key, final long start,
                                          final long end) {
        Jedis jedis = null;
        Set<Tuple> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zrevrangeWithScores(finallyKey, start, end);
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

    /**
     * Return the sorted set cardinality (number of elements). If the key does
     * not exist 0 is returned, like for empty sorted sets.
     * <p>
     * Time complexity O(1)
     *
     * @param key
     * @return the cardinality (number of elements) of the set as an integer.
     */
    @Override
    public Long zcard(final String key) {
        return zcard(SafeEncoder.encode(key));
    }

    /**
     * Return the sorted set cardinality (number of elements). If the key does
     * not exist 0 is returned, like for empty sorted sets.
     * <p>
     * Time complexity O(1)
     *
     * @param key
     * @return the cardinality (number of elements) of the set as an integer.
     */
    @Override
    public Long zcard(final byte[] key) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zcard(finallyKey);
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

    /**
     * Return the score of the specified element of the sorted set at key. If
     * the specified element does not exist in the sorted set, or the key does
     * not exist at all, a special 'nil' value is returned.
     * <p>
     * <b>Time complexity:</b> O(1)
     *
     * @param key
     * @param member
     * @return the score
     */
    @Override
    public Double zscore(final String key, final String member) {
        return zscore(SafeEncoder.encode(key), SafeEncoder.encode(member));
    }

    /**
     * Return the score of the specified element of the sorted set at key. If
     * the specified element does not exist in the sorted set, or the key does
     * not exist at all, a special 'nil' value is returned.
     * <p>
     * <b>Time complexity:</b> O(1)
     *
     * @param key
     * @param member
     * @return the score
     */
    @Override
    public Double zscore(final byte[] key, final byte[] member) {
        Jedis jedis = null;
        Double ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zscore(finallyKey, member);
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
    public Long zcount(final String key, final double min, final double max) {
        return zcount(SafeEncoder.encode(key), min, max);
    }

    @Override
    public Long zcount(final byte[] key, final double min, final double max) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zcount(finallyKey, min, max);
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
    public Long zcount(final String key, final String min, final String max) {
        return zcount(SafeEncoder.encode(key), SafeEncoder.encode(min),
                SafeEncoder.encode(max));
    }

    @Override
    public Long zcount(final byte[] key, final byte[] min, final byte[] max) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zcount(finallyKey, min, max);
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

    /**
     * Return the all the elements in the sorted set at key with a score between
     * min and max (including elements with score equal to min or max).
     * <p>
     * The elements having the same score are returned sorted lexicographically
     * as ASCII strings (this follows from a property of Redis sorted sets and
     * does not involve further computation).
     * <p>
     * Using the optional
     * {@link #zrangeByScore(String, double, double, int, int) LIMIT} it's
     * possible to get only a range of the matching elements in an SQL-alike
     * way. Note that if offset is large the commands needs to traverse the list
     * for offset elements and this adds up to the O(M) figure.
     * <p>
     * The {@link #zcount(String, double, double) ZCOUNT} command is similar to
     * {@link #zrangeByScore(String, double, double) ZRANGEBYSCORE} but instead
     * of returning the actual elements in the specified interval, it just
     * returns the number of matching elements.
     * <p>
     * <b>Exclusive intervals and infinity</b>
     * <p>
     * min and max can be -inf and +inf, so that you are not required to know
     * what's the greatest or smallest element in order to take, for instance,
     * elements "up to a given value".
     * <p>
     * Also while the interval is for default closed (inclusive) it's possible
     * to specify open intervals prefixing the score with a "(" character, so
     * for instance:
     * <p>
     * {@code ZRANGEBYSCORE zset (1.3 5}
     * <p>
     * Will return all the values with score > 1.3 and <= 5, while for instance:
     * <p>
     * {@code ZRANGEBYSCORE zset (5 (10}
     * <p>
     * Will return all the values with score > 5 and < 10 (5 and 10 excluded).
     * <p>
     * <b>Time complexity:</b>
     * <p>
     * O(log(N))+O(M) with N being the number of elements in the sorted set and
     * M the number of elements returned by the command, so if M is constant
     * (for instance you always ask for the first ten elements with LIMIT) you
     * can consider it O(log(N))
     *
     * @param key
     * @param min a double or Double.MIN_VALUE for "-inf"
     * @param max a double or Double.MAX_VALUE for "+inf"
     * @return Multi bulk reply specifically a list of elements in the specified
     * score range.
     * @see #zrangeByScore(String, double, double)
     * @see #zrangeByScore(String, double, double, int, int)
     * @see #zrangeByScoreWithScores(String, double, double)
     * @see #zrangeByScoreWithScores(String, String, String)
     * @see #zrangeByScoreWithScores(String, double, double, int, int)
     * @see #zcount(String, double, double)
     */
    @Override
    public Set<String> zrangeByScore(final String key, final double min,
                                     final double max) {
        return SafeEncoder.encodeSet(zrangeByScore(SafeEncoder.encode(key),
                min, max));
    }

    /**
     * Return the all the elements in the sorted set at key with a score between
     * min and max (including elements with score equal to min or max).
     * <p>
     * The elements having the same score are returned sorted lexicographically
     * as ASCII strings (this follows from a property of Redis sorted sets and
     * does not involve further computation).
     * <p>
     * Using the optional
     * {@link #zrangeByScore(String, double, double, int, int) LIMIT} it's
     * possible to get only a range of the matching elements in an SQL-alike
     * way. Note that if offset is large the commands needs to traverse the list
     * for offset elements and this adds up to the O(M) figure.
     * <p>
     * The {@link #zcount(String, double, double) ZCOUNT} command is similar to
     * {@link #zrangeByScore(String, double, double) ZRANGEBYSCORE} but instead
     * of returning the actual elements in the specified interval, it just
     * returns the number of matching elements.
     * <p>
     * <b>Exclusive intervals and infinity</b>
     * <p>
     * min and max can be -inf and +inf, so that you are not required to know
     * what's the greatest or smallest element in order to take, for instance,
     * elements "up to a given value".
     * <p>
     * Also while the interval is for default closed (inclusive) it's possible
     * to specify open intervals prefixing the score with a "(" character, so
     * for instance:
     * <p>
     * {@code ZRANGEBYSCORE zset (1.3 5}
     * <p>
     * Will return all the values with score > 1.3 and <= 5, while for instance:
     * <p>
     * {@code ZRANGEBYSCORE zset (5 (10}
     * <p>
     * Will return all the values with score > 5 and < 10 (5 and 10 excluded).
     * <p>
     * <b>Time complexity:</b>
     * <p>
     * O(log(N))+O(M) with N being the number of elements in the sorted set and
     * M the number of elements returned by the command, so if M is constant
     * (for instance you always ask for the first ten elements with LIMIT) you
     * can consider it O(log(N))
     *
     * @param key
     * @param min a double or Double.MIN_VALUE for "-inf"
     * @param max a double or Double.MAX_VALUE for "+inf"
     * @return Multi bulk reply specifically a list of elements in the specified
     * score range.
     * @see #zrangeByScore(String, double, double)
     * @see #zrangeByScore(String, double, double, int, int)
     * @see #zrangeByScoreWithScores(String, double, double)
     * @see #zrangeByScoreWithScores(String, String, String)
     * @see #zrangeByScoreWithScores(String, double, double, int, int)
     * @see #zcount(String, double, double)
     */
    @Override
    public Set<byte[]> zrangeByScore(final byte[] key, final double min,
                                     final double max) {
        Jedis jedis = null;
        Set<byte[]> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zrangeByScore(finallyKey, min, max);
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
    public Set<String> zrangeByScore(final String key, final String min,
                                     final String max) {
        return SafeEncoder.encodeSet(zrangeByScore(SafeEncoder.encode(key),
                SafeEncoder.encode(min), SafeEncoder.encode(max)));
    }

    @Override
    public Set<byte[]> zrangeByScore(final byte[] key, final byte[] min,
                                     final byte[] max) {
        Jedis jedis = null;
        Set<byte[]> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zrangeByScore(finallyKey, min, max);
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

    /**
     * Return the all the elements in the sorted set at key with a score between
     * min and max (including elements with score equal to min or max).
     * <p>
     * The elements having the same score are returned sorted lexicographically
     * as ASCII strings (this follows from a property of Redis sorted sets and
     * does not involve further computation).
     * <p>
     * Using the optional
     * {@link #zrangeByScore(String, double, double, int, int) LIMIT} it's
     * possible to get only a range of the matching elements in an SQL-alike
     * way. Note that if offset is large the commands needs to traverse the list
     * for offset elements and this adds up to the O(M) figure.
     * <p>
     * The {@link #zcount(String, double, double) ZCOUNT} command is similar to
     * {@link #zrangeByScore(String, double, double) ZRANGEBYSCORE} but instead
     * of returning the actual elements in the specified interval, it just
     * returns the number of matching elements.
     * <p>
     * <b>Exclusive intervals and infinity</b>
     * <p>
     * min and max can be -inf and +inf, so that you are not required to know
     * what's the greatest or smallest element in order to take, for instance,
     * elements "up to a given value".
     * <p>
     * Also while the interval is for default closed (inclusive) it's possible
     * to specify open intervals prefixing the score with a "(" character, so
     * for instance:
     * <p>
     * {@code ZRANGEBYSCORE zset (1.3 5}
     * <p>
     * Will return all the values with score > 1.3 and <= 5, while for instance:
     * <p>
     * {@code ZRANGEBYSCORE zset (5 (10}
     * <p>
     * Will return all the values with score > 5 and < 10 (5 and 10 excluded).
     * <p>
     * <b>Time complexity:</b>
     * <p>
     * O(log(N))+O(M) with N being the number of elements in the sorted set and
     * M the number of elements returned by the command, so if M is constant
     * (for instance you always ask for the first ten elements with LIMIT) you
     * can consider it O(log(N))
     *
     * @param key
     * @param min
     * @param max
     * @return Multi bulk reply specifically a list of elements in the specified
     * score range.
     * @see #zrangeByScore(String, double, double)
     * @see #zrangeByScore(String, double, double, int, int)
     * @see #zrangeByScoreWithScores(String, double, double)
     * @see #zrangeByScoreWithScores(String, double, double, int, int)
     * @see #zcount(String, double, double)
     */
    @Override
    public Set<String> zrangeByScore(final String key, final double min,
                                     final double max, final int offset, final int count) {
        return SafeEncoder.encodeSet(zrangeByScore(SafeEncoder.encode(key),
                min, max, offset, count));
    }

    /**
     * Return the all the elements in the sorted set at key with a score between
     * min and max (including elements with score equal to min or max).
     * <p>
     * The elements having the same score are returned sorted lexicographically
     * as ASCII strings (this follows from a property of Redis sorted sets and
     * does not involve further computation).
     * <p>
     * Using the optional
     * {@link #zrangeByScore(String, double, double, int, int) LIMIT} it's
     * possible to get only a range of the matching elements in an SQL-alike
     * way. Note that if offset is large the commands needs to traverse the list
     * for offset elements and this adds up to the O(M) figure.
     * <p>
     * The {@link #zcount(String, double, double) ZCOUNT} command is similar to
     * {@link #zrangeByScore(String, double, double) ZRANGEBYSCORE} but instead
     * of returning the actual elements in the specified interval, it just
     * returns the number of matching elements.
     * <p>
     * <b>Exclusive intervals and infinity</b>
     * <p>
     * min and max can be -inf and +inf, so that you are not required to know
     * what's the greatest or smallest element in order to take, for instance,
     * elements "up to a given value".
     * <p>
     * Also while the interval is for default closed (inclusive) it's possible
     * to specify open intervals prefixing the score with a "(" character, so
     * for instance:
     * <p>
     * {@code ZRANGEBYSCORE zset (1.3 5}
     * <p>
     * Will return all the values with score > 1.3 and <= 5, while for instance:
     * <p>
     * {@code ZRANGEBYSCORE zset (5 (10}
     * <p>
     * Will return all the values with score > 5 and < 10 (5 and 10 excluded).
     * <p>
     * <b>Time complexity:</b>
     * <p>
     * O(log(N))+O(M) with N being the number of elements in the sorted set and
     * M the number of elements returned by the command, so if M is constant
     * (for instance you always ask for the first ten elements with LIMIT) you
     * can consider it O(log(N))
     *
     * @param key
     * @param min
     * @param max
     * @return Multi bulk reply specifically a list of elements in the specified
     * score range.
     * @see #zrangeByScore(String, double, double)
     * @see #zrangeByScore(String, double, double, int, int)
     * @see #zrangeByScoreWithScores(String, double, double)
     * @see #zrangeByScoreWithScores(String, double, double, int, int)
     * @see #zcount(String, double, double)
     */
    @Override
    public Set<byte[]> zrangeByScore(final byte[] key, final double min,
                                     final double max, final int offset, final int count) {
        Jedis jedis = null;
        Set<byte[]> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zrangeByScore(finallyKey, min, max, offset,
                    count);
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
    public Set<String> zrangeByScore(final String key, final String min,
                                     final String max, final int offset, final int count) {
        return SafeEncoder
                .encodeSet(zrangeByScore(SafeEncoder.encode(key),
                        SafeEncoder.encode(min), SafeEncoder.encode(max),
                        offset, count));
    }

    @Override
    public Set<byte[]> zrangeByScore(final byte[] key, final byte[] min,
                                     final byte[] max, final int offset, final int count) {
        Jedis jedis = null;
        Set<byte[]> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zrangeByScore(finallyKey, min, max, offset,
                    count);
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

    /**
     * Return the all the elements in the sorted set at key with a score between
     * min and max (including elements with score equal to min or max).
     * <p>
     * The elements having the same score are returned sorted lexicographically
     * as ASCII strings (this follows from a property of Redis sorted sets and
     * does not involve further computation).
     * <p>
     * Using the optional
     * {@link #zrangeByScore(String, double, double, int, int) LIMIT} it's
     * possible to get only a range of the matching elements in an SQL-alike
     * way. Note that if offset is large the commands needs to traverse the list
     * for offset elements and this adds up to the O(M) figure.
     * <p>
     * The {@link #zcount(String, double, double) ZCOUNT} command is similar to
     * {@link #zrangeByScore(String, double, double) ZRANGEBYSCORE} but instead
     * of returning the actual elements in the specified interval, it just
     * returns the number of matching elements.
     * <p>
     * <b>Exclusive intervals and infinity</b>
     * <p>
     * min and max can be -inf and +inf, so that you are not required to know
     * what's the greatest or smallest element in order to take, for instance,
     * elements "up to a given value".
     * <p>
     * Also while the interval is for default closed (inclusive) it's possible
     * to specify open intervals prefixing the score with a "(" character, so
     * for instance:
     * <p>
     * {@code ZRANGEBYSCORE zset (1.3 5}
     * <p>
     * Will return all the values with score > 1.3 and <= 5, while for instance:
     * <p>
     * {@code ZRANGEBYSCORE zset (5 (10}
     * <p>
     * Will return all the values with score > 5 and < 10 (5 and 10 excluded).
     * <p>
     * <b>Time complexity:</b>
     * <p>
     * O(log(N))+O(M) with N being the number of elements in the sorted set and
     * M the number of elements returned by the command, so if M is constant
     * (for instance you always ask for the first ten elements with LIMIT) you
     * can consider it O(log(N))
     *
     * @param key
     * @param min
     * @param max
     * @return Multi bulk reply specifically a list of elements in the specified
     * score range.
     * @see #zrangeByScore(String, double, double)
     * @see #zrangeByScore(String, double, double, int, int)
     * @see #zrangeByScoreWithScores(String, double, double)
     * @see #zrangeByScoreWithScores(String, double, double, int, int)
     * @see #zcount(String, double, double)
     */
    @Override
    public Set<Tuple> zrangeByScoreWithScores(final String key,
                                              final double min, final double max) {
        return zrangeByScoreWithScores(SafeEncoder.encode(key), min, max);
    }

    /**
     * Return the all the elements in the sorted set at key with a score between
     * min and max (including elements with score equal to min or max).
     * <p>
     * The elements having the same score are returned sorted lexicographically
     * as ASCII strings (this follows from a property of Redis sorted sets and
     * does not involve further computation).
     * <p>
     * Using the optional
     * {@link #zrangeByScore(String, double, double, int, int) LIMIT} it's
     * possible to get only a range of the matching elements in an SQL-alike
     * way. Note that if offset is large the commands needs to traverse the list
     * for offset elements and this adds up to the O(M) figure.
     * <p>
     * The {@link #zcount(String, double, double) ZCOUNT} command is similar to
     * {@link #zrangeByScore(String, double, double) ZRANGEBYSCORE} but instead
     * of returning the actual elements in the specified interval, it just
     * returns the number of matching elements.
     * <p>
     * <b>Exclusive intervals and infinity</b>
     * <p>
     * min and max can be -inf and +inf, so that you are not required to know
     * what's the greatest or smallest element in order to take, for instance,
     * elements "up to a given value".
     * <p>
     * Also while the interval is for default closed (inclusive) it's possible
     * to specify open intervals prefixing the score with a "(" character, so
     * for instance:
     * <p>
     * {@code ZRANGEBYSCORE zset (1.3 5}
     * <p>
     * Will return all the values with score > 1.3 and <= 5, while for instance:
     * <p>
     * {@code ZRANGEBYSCORE zset (5 (10}
     * <p>
     * Will return all the values with score > 5 and < 10 (5 and 10 excluded).
     * <p>
     * <b>Time complexity:</b>
     * <p>
     * O(log(N))+O(M) with N being the number of elements in the sorted set and
     * M the number of elements returned by the command, so if M is constant
     * (for instance you always ask for the first ten elements with LIMIT) you
     * can consider it O(log(N))
     *
     * @param key
     * @param min
     * @param max
     * @return Multi bulk reply specifically a list of elements in the specified
     * score range.
     * @see #zrangeByScore(String, double, double)
     * @see #zrangeByScore(String, double, double, int, int)
     * @see #zrangeByScoreWithScores(String, double, double)
     * @see #zrangeByScoreWithScores(String, double, double, int, int)
     * @see #zcount(String, double, double)
     */
    @Override
    public Set<Tuple> zrangeByScoreWithScores(final byte[] key,
                                              final double min, final double max) {
        Jedis jedis = null;
        Set<Tuple> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zrangeByScoreWithScores(finallyKey, min,
                    max);
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
    public Set<Tuple> zrangeByScoreWithScores(final String key,
                                              final String min, final String max) {
        return zrangeByScoreWithScores(SafeEncoder.encode(key),
                SafeEncoder.encode(min), SafeEncoder.encode(max));
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(final byte[] key,
                                              final byte[] min, final byte[] max) {
        Jedis jedis = null;
        Set<Tuple> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zrangeByScoreWithScores(finallyKey, min,
                    max);
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

    /**
     * Return the all the elements in the sorted set at key with a score between
     * min and max (including elements with score equal to min or max).
     * <p>
     * The elements having the same score are returned sorted lexicographically
     * as ASCII strings (this follows from a property of Redis sorted sets and
     * does not involve further computation).
     * <p>
     * Using the optional
     * {@link #zrangeByScore(String, double, double, int, int) LIMIT} it's
     * possible to get only a range of the matching elements in an SQL-alike
     * way. Note that if offset is large the commands needs to traverse the list
     * for offset elements and this adds up to the O(M) figure.
     * <p>
     * The {@link #zcount(String, double, double) ZCOUNT} command is similar to
     * {@link #zrangeByScore(String, double, double) ZRANGEBYSCORE} but instead
     * of returning the actual elements in the specified interval, it just
     * returns the number of matching elements.
     * <p>
     * <b>Exclusive intervals and infinity</b>
     * <p>
     * min and max can be -inf and +inf, so that you are not required to know
     * what's the greatest or smallest element in order to take, for instance,
     * elements "up to a given value".
     * <p>
     * Also while the interval is for default closed (inclusive) it's possible
     * to specify open intervals prefixing the score with a "(" character, so
     * for instance:
     * <p>
     * {@code ZRANGEBYSCORE zset (1.3 5}
     * <p>
     * Will return all the values with score > 1.3 and <= 5, while for instance:
     * <p>
     * {@code ZRANGEBYSCORE zset (5 (10}
     * <p>
     * Will return all the values with score > 5 and < 10 (5 and 10 excluded).
     * <p>
     * <b>Time complexity:</b>
     * <p>
     * O(log(N))+O(M) with N being the number of elements in the sorted set and
     * M the number of elements returned by the command, so if M is constant
     * (for instance you always ask for the first ten elements with LIMIT) you
     * can consider it O(log(N))
     *
     * @param key
     * @param min
     * @param max
     * @return Multi bulk reply specifically a list of elements in the specified
     * score range.
     * @see #zrangeByScore(String, double, double)
     * @see #zrangeByScore(String, double, double, int, int)
     * @see #zrangeByScoreWithScores(String, double, double)
     * @see #zrangeByScoreWithScores(String, double, double, int, int)
     * @see #zcount(String, double, double)
     */
    @Override
    public Set<Tuple> zrangeByScoreWithScores(final String key,
                                              final double min, final double max, final int offset,
                                              final int count) {
        return zrangeByScoreWithScores(SafeEncoder.encode(key), min, max,
                offset, count);
    }

    /**
     * Return the all the elements in the sorted set at key with a score between
     * min and max (including elements with score equal to min or max).
     * <p>
     * The elements having the same score are returned sorted lexicographically
     * as ASCII strings (this follows from a property of Redis sorted sets and
     * does not involve further computation).
     * <p>
     * Using the optional
     * {@link #zrangeByScore(String, double, double, int, int) LIMIT} it's
     * possible to get only a range of the matching elements in an SQL-alike
     * way. Note that if offset is large the commands needs to traverse the list
     * for offset elements and this adds up to the O(M) figure.
     * <p>
     * The {@link #zcount(String, double, double) ZCOUNT} command is similar to
     * {@link #zrangeByScore(String, double, double) ZRANGEBYSCORE} but instead
     * of returning the actual elements in the specified interval, it just
     * returns the number of matching elements.
     * <p>
     * <b>Exclusive intervals and infinity</b>
     * <p>
     * min and max can be -inf and +inf, so that you are not required to know
     * what's the greatest or smallest element in order to take, for instance,
     * elements "up to a given value".
     * <p>
     * Also while the interval is for default closed (inclusive) it's possible
     * to specify open intervals prefixing the score with a "(" character, so
     * for instance:
     * <p>
     * {@code ZRANGEBYSCORE zset (1.3 5}
     * <p>
     * Will return all the values with score > 1.3 and <= 5, while for instance:
     * <p>
     * {@code ZRANGEBYSCORE zset (5 (10}
     * <p>
     * Will return all the values with score > 5 and < 10 (5 and 10 excluded).
     * <p>
     * <b>Time complexity:</b>
     * <p>
     * O(log(N))+O(M) with N being the number of elements in the sorted set and
     * M the number of elements returned by the command, so if M is constant
     * (for instance you always ask for the first ten elements with LIMIT) you
     * can consider it O(log(N))
     *
     * @param key
     * @param min
     * @param max
     * @return Multi bulk reply specifically a list of elements in the specified
     * score range.
     * @see #zrangeByScore(String, double, double)
     * @see #zrangeByScore(String, double, double, int, int)
     * @see #zrangeByScoreWithScores(String, double, double)
     * @see #zrangeByScoreWithScores(String, double, double, int, int)
     * @see #zcount(String, double, double)
     */
    @Override
    public Set<Tuple> zrangeByScoreWithScores(final byte[] key,
                                              final double min, final double max, final int offset,
                                              final int count) {
        Jedis jedis = null;
        Set<Tuple> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zrangeByScoreWithScores(finallyKey, min,
                    max, offset, count);
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
    public Set<Tuple> zrangeByScoreWithScores(final String key,
                                              final String min, final String max, final int offset,
                                              final int count) {
        return zrangeByScoreWithScores(SafeEncoder.encode(key),
                SafeEncoder.encode(min), SafeEncoder.encode(max), offset, count);
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(final byte[] key,
                                              final byte[] min, final byte[] max, final int offset,
                                              final int count) {
        Jedis jedis = null;
        Set<Tuple> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zrangeByScoreWithScores(finallyKey, min,
                    max, offset, count);
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
    public Set<String> zrevrangeByScore(final String key, final double max,
                                        final double min) {
        return SafeEncoder.encodeSet(zrevrangeByScore(SafeEncoder.encode(key),
                max, min));
    }

    @Override
    public Set<byte[]> zrevrangeByScore(final byte[] key, final double max,
                                        final double min) {
        Jedis jedis = null;
        Set<byte[]> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zrevrangeByScore(finallyKey, max, min);
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
    public Set<String> zrevrangeByScore(final String key, final String max,
                                        final String min) {
        return SafeEncoder.encodeSet(zrevrangeByScore(SafeEncoder.encode(key),
                SafeEncoder.encode(max), SafeEncoder.encode(min)));
    }

    @Override
    public Set<byte[]> zrevrangeByScore(final byte[] key, final byte[] max,
                                        final byte[] min) {
        Jedis jedis = null;
        Set<byte[]> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zrevrangeByScore(finallyKey, max, min);
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
    public Set<String> zrevrangeByScore(final String key, final double max,
                                        final double min, final int offset, final int count) {
        return SafeEncoder.encodeSet(zrevrangeByScore(SafeEncoder.encode(key),
                max, min, offset, count));
    }

    @Override
    public Set<byte[]> zrevrangeByScore(final byte[] key, final double max,
                                        final double min, final int offset, final int count) {
        Jedis jedis = null;
        Set<byte[]> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zrevrangeByScore(finallyKey, max, min,
                    offset, count);
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
    public Set<Tuple> zrevrangeByScoreWithScores(final String key,
                                                 final double max, final double min) {
        return zrevrangeByScoreWithScores(SafeEncoder.encode(key), max, min);
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(final byte[] key,
                                                 final double max, final double min) {
        Jedis jedis = null;
        Set<Tuple> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zrevrangeByScoreWithScores(finallyKey, max,
                    min);
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
    public Set<Tuple> zrevrangeByScoreWithScores(final String key,
                                                 final double max, final double min, final int offset,
                                                 final int count) {
        return zrevrangeByScoreWithScores(SafeEncoder.encode(key), max, min,
                offset, count);
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(final byte[] key,
                                                 final double max, final double min, final int offset,
                                                 final int count) {
        Jedis jedis = null;
        Set<Tuple> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zrevrangeByScoreWithScores(finallyKey, max,
                    min, offset, count);
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
    public Set<Tuple> zrevrangeByScoreWithScores(final String key,
                                                 final String max, final String min, final int offset,
                                                 final int count) {
        return zrevrangeByScoreWithScores(SafeEncoder.encode(key),
                SafeEncoder.encode(max), SafeEncoder.encode(min), offset, count);
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(final byte[] key,
                                                 final byte[] max, final byte[] min, final int offset,
                                                 final int count) {
        Jedis jedis = null;
        Set<Tuple> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zrevrangeByScoreWithScores(finallyKey, max,
                    min, offset, count);
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
    public Set<String> zrevrangeByScore(final String key, final String max,
                                        final String min, final int offset, final int count) {
        return SafeEncoder
                .encodeSet(zrevrangeByScore(SafeEncoder.encode(key),
                        SafeEncoder.encode(max), SafeEncoder.encode(min),
                        offset, count));
    }

    @Override
    public Set<byte[]> zrevrangeByScore(final byte[] key, final byte[] max,
                                        final byte[] min, final int offset, final int count) {
        Jedis jedis = null;
        Set<byte[]> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zrevrangeByScore(finallyKey, max, min,
                    offset, count);
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
    public Set<Tuple> zrevrangeByScoreWithScores(final byte[] key,
                                                 final byte[] max, final byte[] min) {
        Jedis jedis = null;
        Set<Tuple> ret = null;
        try {
            jedis = pool.getResource();
            ret = jedis.zrevrangeByScoreWithScores(key, max,
                    min);
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

    /**
     * Remove all elements in the sorted set at key with rank between start and
     * end. Start and end are 0-based with rank 0 being the element with the
     * lowest score. Both start and end can be negative numbers, where they
     * indicate offsets starting at the element with the highest rank. For
     * example: -1 is the element with the highest score, -2 the element with
     * the second highest score and so forth.
     * <p>
     * <b>Time complexity:</b> O(log(N))+O(M) with N being the number of
     * elements in the sorted set and M the number of elements removed by the
     * operation
     */
    @Override
    public Long zremrangeByRank(final String key, final long start,
                                final long end) {
        return zremrangeByRank(SafeEncoder.encode(key), start, end);
    }

    /**
     * Remove all elements in the sorted set at key with rank between start and
     * end. Start and end are 0-based with rank 0 being the element with the
     * lowest score. Both start and end can be negative numbers, where they
     * indicate offsets starting at the element with the highest rank. For
     * example: -1 is the element with the highest score, -2 the element with
     * the second highest score and so forth.
     * <p>
     * <b>Time complexity:</b> O(log(N))+O(M) with N being the number of
     * elements in the sorted set and M the number of elements removed by the
     * operation
     */
    @Override
    public Long zremrangeByRank(final byte[] key, final long start,
                                final long end) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zremrangeByRank(finallyKey, start, end);
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

    /**
     * Remove all the elements in the sorted set at key with a score between min
     * and max (including elements with score equal to min or max).
     * <p>
     * <b>Time complexity:</b>
     * <p>
     * O(log(N))+O(M) with N being the number of elements in the sorted set and
     * M the number of elements removed by the operation
     *
     * @param key
     * @param start
     * @param end
     * @return Integer reply, specifically the number of elements removed.
     */
    @Override
    public Long zremrangeByScore(final String key, final double start,
                                 final double end) {
        return zremrangeByScore(SafeEncoder.encode(key), start, end);
    }

    /**
     * Remove all the elements in the sorted set at key with a score between min
     * and max (including elements with score equal to min or max).
     * <p>
     * <b>Time complexity:</b>
     * <p>
     * O(log(N))+O(M) with N being the number of elements in the sorted set and
     * M the number of elements removed by the operation
     *
     * @param key
     * @param start
     * @param end
     * @return Integer reply, specifically the number of elements removed.
     */
    @Override
    public Long zremrangeByScore(final byte[] key, final double start,
                                 final double end) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zremrangeByScore(finallyKey, start, end);
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
    public Long zremrangeByScore(final String key, final String start,
                                 final String end) {
        return zremrangeByScore(SafeEncoder.encode(key),
                SafeEncoder.encode(start), SafeEncoder.encode(end));
    }

    @Override
    public Long zremrangeByScore(final byte[] key, final byte[] start,
                                 final byte[] end) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.zremrangeByScore(finallyKey, start, end);
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

    /**
     * Creates a union or intersection of N sorted sets given by keys k1 through
     * kN, and stores it at dstkey. It is mandatory to provide the number of
     * input keys N, before passing the input keys and the other (optional)
     * arguments.
     * <p>
     * As the terms imply, the {@link #zinterstore(String, String...)
     * ZINTERSTORE} command requires an element to be present in each of the
     * given inputs to be inserted in the result. The
     * {@link #zunionstore(String, String...) ZUNIONSTORE} command inserts all
     * elements across all inputs.
     * <p>
     * Using the WEIGHTS option, it is possible to add weight to each input
     * sorted set. This means that the score of each element in the sorted set
     * is first multiplied by this weight before being passed to the
     * aggregation. When this option is not given, all weights default to 1.
     * <p>
     * With the AGGREGATE option, it's possible to specify how the results of
     * the union or intersection are aggregated. This option defaults to SUM,
     * where the score of an element is summed across the inputs where it
     * exists. When this option is set to be either MIN or MAX, the resulting
     * set will contain the minimum or maximum score of an element across the
     * inputs where it exists.
     * <p>
     * <b>Time complexity:</b> O(N) + O(M log(M)) with N being the sum of the
     * sizes of the input sorted sets, and M being the number of elements in the
     * resulting sorted set
     *
     * @param dstkey
     * @param sets
     * @return Integer reply, specifically the number of elements in the sorted
     * set at dstkey
     * @see #zunionstore(String, String...)
     * @see #zunionstore(String, ZParams, String...)
     * @see #zinterstore(String, String...)
     * @see #zinterstore(String, ZParams, String...)
     */
    @Override
    public Long zunionstore(final String dstkey, final String... sets) {
        return zunionstore(SafeEncoder.encode(dstkey),
                SafeEncoder.encodeMany(sets));
    }

    /**
     * Creates a union or intersection of N sorted sets given by keys k1 through
     * kN, and stores it at dstkey. It is mandatory to provide the number of
     * input keys N, before passing the input keys and the other (optional)
     * arguments.
     * <p>
     * As the terms imply, the {@link #zinterstore(String, String...)
     * ZINTERSTORE} command requires an element to be present in each of the
     * given inputs to be inserted in the result. The
     * {@link #zunionstore(String, String...) ZUNIONSTORE} command inserts all
     * elements across all inputs.
     * <p>
     * Using the WEIGHTS option, it is possible to add weight to each input
     * sorted set. This means that the score of each element in the sorted set
     * is first multiplied by this weight before being passed to the
     * aggregation. When this option is not given, all weights default to 1.
     * <p>
     * With the AGGREGATE option, it's possible to specify how the results of
     * the union or intersection are aggregated. This option defaults to SUM,
     * where the score of an element is summed across the inputs where it
     * exists. When this option is set to be either MIN or MAX, the resulting
     * set will contain the minimum or maximum score of an element across the
     * inputs where it exists.
     * <p>
     * <b>Time complexity:</b> O(N) + O(M log(M)) with N being the sum of the
     * sizes of the input sorted sets, and M being the number of elements in the
     * resulting sorted set
     *
     * @param dstkey
     * @param sets
     * @return Integer reply, specifically the number of elements in the sorted
     * set at dstkey
     * @see #zunionstore(String, String...)
     * @see #zunionstore(String, ZParams, String...)
     * @see #zinterstore(String, String...)
     * @see #zinterstore(String, ZParams, String...)
     */
    @Override
    public Long zunionstore(final byte[] dstkey, final byte[]... sets) {
        Jedis jedis = null;
        Long ret = null;
        try {
            jedis = pool.getResource();
            ret = jedis.zunionstore(dstkey, sets);
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

    /**
     * Creates a union or intersection of N sorted sets given by keys k1 through
     * kN, and stores it at dstkey. It is mandatory to provide the number of
     * input keys N, before passing the input keys and the other (optional)
     * arguments.
     * <p>
     * As the terms imply, the {@link #zinterstore(String, String...)
     * ZINTERSTORE} command requires an element to be present in each of the
     * given inputs to be inserted in the result. The
     * {@link #zunionstore(String, String...) ZUNIONSTORE} command inserts all
     * elements across all inputs.
     * <p>
     * Using the WEIGHTS option, it is possible to add weight to each input
     * sorted set. This means that the score of each element in the sorted set
     * is first multiplied by this weight before being passed to the
     * aggregation. When this option is not given, all weights default to 1.
     * <p>
     * With the AGGREGATE option, it's possible to specify how the results of
     * the union or intersection are aggregated. This option defaults to SUM,
     * where the score of an element is summed across the inputs where it
     * exists. When this option is set to be either MIN or MAX, the resulting
     * set will contain the minimum or maximum score of an element across the
     * inputs where it exists.
     * <p>
     * <b>Time complexity:</b> O(N) + O(M log(M)) with N being the sum of the
     * sizes of the input sorted sets, and M being the number of elements in the
     * resulting sorted set
     *
     * @param dstkey
     * @param sets
     * @param params
     * @return Integer reply, specifically the number of elements in the sorted
     * set at dstkey
     * @see #zunionstore(String, String...)
     * @see #zunionstore(String, ZParams, String...)
     * @see #zinterstore(String, String...)
     * @see #zinterstore(String, ZParams, String...)
     */
    @Override
    public Long zunionstore(final String dstkey, final ZParams params,
                            final String... sets) {
        return zunionstore(SafeEncoder.encode(dstkey), params,
                SafeEncoder.encodeMany(sets));
    }

    /**
     * Creates a union or intersection of N sorted sets given by keys k1 through
     * kN, and stores it at dstkey. It is mandatory to provide the number of
     * input keys N, before passing the input keys and the other (optional)
     * arguments.
     * <p>
     * As the terms imply, the {@link #zinterstore(String, String...)
     * ZINTERSTORE} command requires an element to be present in each of the
     * given inputs to be inserted in the result. The
     * {@link #zunionstore(String, String...) ZUNIONSTORE} command inserts all
     * elements across all inputs.
     * <p>
     * Using the WEIGHTS option, it is possible to add weight to each input
     * sorted set. This means that the score of each element in the sorted set
     * is first multiplied by this weight before being passed to the
     * aggregation. When this option is not given, all weights default to 1.
     * <p>
     * With the AGGREGATE option, it's possible to specify how the results of
     * the union or intersection are aggregated. This option defaults to SUM,
     * where the score of an element is summed across the inputs where it
     * exists. When this option is set to be either MIN or MAX, the resulting
     * set will contain the minimum or maximum score of an element across the
     * inputs where it exists.
     * <p>
     * <b>Time complexity:</b> O(N) + O(M log(M)) with N being the sum of the
     * sizes of the input sorted sets, and M being the number of elements in the
     * resulting sorted set
     *
     * @param dstkey
     * @param sets
     * @param params
     * @return Integer reply, specifically the number of elements in the sorted
     * set at dstkey
     * @see #zunionstore(String, String...)
     * @see #zunionstore(String, ZParams, String...)
     * @see #zinterstore(String, String...)
     * @see #zinterstore(String, ZParams, String...)
     */
    @Override
    public Long zunionstore(final byte[] dstkey, final ZParams params,
                            final byte[]... sets) {
        Jedis jedis = null;
        Long ret = null;
        try {
            jedis = pool.getResource();
            ret = jedis.zunionstore(dstkey, params, sets);
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

    /**
     * Creates a union or intersection of N sorted sets given by keys k1 through
     * kN, and stores it at dstkey. It is mandatory to provide the number of
     * input keys N, before passing the input keys and the other (optional)
     * arguments.
     * <p>
     * As the terms imply, the {@link #zinterstore(String, String...)
     * ZINTERSTORE} command requires an element to be present in each of the
     * given inputs to be inserted in the result. The
     * {@link #zunionstore(String, String...) ZUNIONSTORE} command inserts all
     * elements across all inputs.
     * <p>
     * Using the WEIGHTS option, it is possible to add weight to each input
     * sorted set. This means that the score of each element in the sorted set
     * is first multiplied by this weight before being passed to the
     * aggregation. When this option is not given, all weights default to 1.
     * <p>
     * With the AGGREGATE option, it's possible to specify how the results of
     * the union or intersection are aggregated. This option defaults to SUM,
     * where the score of an element is summed across the inputs where it
     * exists. When this option is set to be either MIN or MAX, the resulting
     * set will contain the minimum or maximum score of an element across the
     * inputs where it exists.
     * <p>
     * <b>Time complexity:</b> O(N) + O(M log(M)) with N being the sum of the
     * sizes of the input sorted sets, and M being the number of elements in the
     * resulting sorted set
     *
     * @param dstkey
     * @param sets
     * @return Integer reply, specifically the number of elements in the sorted
     * set at dstkey
     * @see #zunionstore(String, String...)
     * @see #zunionstore(String, ZParams, String...)
     * @see #zinterstore(String, String...)
     * @see #zinterstore(String, ZParams, String...)
     */
    @Override
    public Long zinterstore(final String dstkey, final String... sets) {
        return zinterstore(SafeEncoder.encode(dstkey),
                SafeEncoder.encodeMany(sets));
    }

    /**
     * Creates a union or intersection of N sorted sets given by keys k1 through
     * kN, and stores it at dstkey. It is mandatory to provide the number of
     * input keys N, before passing the input keys and the other (optional)
     * arguments.
     * <p>
     * As the terms imply, the {@link #zinterstore(String, String...)
     * ZINTERSTORE} command requires an element to be present in each of the
     * given inputs to be inserted in the result. The
     * {@link #zunionstore(String, String...) ZUNIONSTORE} command inserts all
     * elements across all inputs.
     * <p>
     * Using the WEIGHTS option, it is possible to add weight to each input
     * sorted set. This means that the score of each element in the sorted set
     * is first multiplied by this weight before being passed to the
     * aggregation. When this option is not given, all weights default to 1.
     * <p>
     * With the AGGREGATE option, it's possible to specify how the results of
     * the union or intersection are aggregated. This option defaults to SUM,
     * where the score of an element is summed across the inputs where it
     * exists. When this option is set to be either MIN or MAX, the resulting
     * set will contain the minimum or maximum score of an element across the
     * inputs where it exists.
     * <p>
     * <b>Time complexity:</b> O(N) + O(M log(M)) with N being the sum of the
     * sizes of the input sorted sets, and M being the number of elements in the
     * resulting sorted set
     *
     * @param dstkey
     * @param sets
     * @return Integer reply, specifically the number of elements in the sorted
     * set at dstkey
     * @see #zunionstore(String, String...)
     * @see #zunionstore(String, ZParams, String...)
     * @see #zinterstore(String, String...)
     * @see #zinterstore(String, ZParams, String...)
     */
    @Override
    public Long zinterstore(final byte[] dstkey, final byte[]... sets) {
        Jedis jedis = null;
        Long ret = null;
        try {
            jedis = pool.getResource();
            ret = jedis.zinterstore(dstkey, sets);
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

    /**
     * Creates a union or intersection of N sorted sets given by keys k1 through
     * kN, and stores it at dstkey. It is mandatory to provide the number of
     * input keys N, before passing the input keys and the other (optional)
     * arguments.
     * <p>
     * As the terms imply, the {@link #zinterstore(String, String...)
     * ZINTERSTORE} command requires an element to be present in each of the
     * given inputs to be inserted in the result. The
     * {@link #zunionstore(String, String...) ZUNIONSTORE} command inserts all
     * elements across all inputs.
     * <p>
     * Using the WEIGHTS option, it is possible to add weight to each input
     * sorted set. This means that the score of each element in the sorted set
     * is first multiplied by this weight before being passed to the
     * aggregation. When this option is not given, all weights default to 1.
     * <p>
     * With the AGGREGATE option, it's possible to specify how the results of
     * the union or intersection are aggregated. This option defaults to SUM,
     * where the score of an element is summed across the inputs where it
     * exists. When this option is set to be either MIN or MAX, the resulting
     * set will contain the minimum or maximum score of an element across the
     * inputs where it exists.
     * <p>
     * <b>Time complexity:</b> O(N) + O(M log(M)) with N being the sum of the
     * sizes of the input sorted sets, and M being the number of elements in the
     * resulting sorted set
     *
     * @param dstkey
     * @param sets
     * @param params
     * @return Integer reply, specifically the number of elements in the sorted
     * set at dstkey
     * @see #zunionstore(String, String...)
     * @see #zunionstore(String, ZParams, String...)
     * @see #zinterstore(String, String...)
     * @see #zinterstore(String, ZParams, String...)
     */
    @Override
    public Long zinterstore(final String dstkey, final ZParams params,
                            final String... sets) {
        return zinterstore(SafeEncoder.encode(dstkey), params,
                SafeEncoder.encodeMany(sets));
    }

    /**
     * Creates a union or intersection of N sorted sets given by keys k1 through
     * kN, and stores it at dstkey. It is mandatory to provide the number of
     * input keys N, before passing the input keys and the other (optional)
     * arguments.
     * <p>
     * As the terms imply, the {@link #zinterstore(String, String...)
     * ZINTERSTORE} command requires an element to be present in each of the
     * given inputs to be inserted in the result. The
     * {@link #zunionstore(String, String...) ZUNIONSTORE} command inserts all
     * elements across all inputs.
     * <p>
     * Using the WEIGHTS option, it is possible to add weight to each input
     * sorted set. This means that the score of each element in the sorted set
     * is first multiplied by this weight before being passed to the
     * aggregation. When this option is not given, all weights default to 1.
     * <p>
     * With the AGGREGATE option, it's possible to specify how the results of
     * the union or intersection are aggregated. This option defaults to SUM,
     * where the score of an element is summed across the inputs where it
     * exists. When this option is set to be either MIN or MAX, the resulting
     * set will contain the minimum or maximum score of an element across the
     * inputs where it exists.
     * <p>
     * <b>Time complexity:</b> O(N) + O(M log(M)) with N being the sum of the
     * sizes of the input sorted sets, and M being the number of elements in the
     * resulting sorted set
     *
     * @param dstkey
     * @param sets
     * @param params
     * @return Integer reply, specifically the number of elements in the sorted
     * set at dstkey
     * @see #zunionstore(String, String...)
     * @see #zunionstore(String, ZParams, String...)
     * @see #zinterstore(String, String...)
     * @see #zinterstore(String, ZParams, String...)
     */
    @Override
    public Long zinterstore(final byte[] dstkey, final ZParams params,
                            final byte[]... sets) {
        Jedis jedis = null;
        Long ret = null;
        try {
            jedis = pool.getResource();
            ret = jedis.zinterstore(dstkey, params, sets);
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
    public Long strlen(final String key) {
        return strlen(SafeEncoder.encode(key));
    }

    @Override
    public Long strlen(final byte[] key) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.strlen(finallyKey);
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
    public Long lpushx(final String key, final String... string) {
        return lpushx(SafeEncoder.encode(key), SafeEncoder.encodeMany(string));
    }

    @Override
    public Long lpushx(final byte[] key, final byte[]... string) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.lpushx(finallyKey, string);
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

    /**
     * Undo a {@link #expire(String, int) expire} at turning the expire key into
     * a normal key.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @return Integer reply, specifically: 1: the key is now persist. 0: the
     * key is not persist (only happens when key not set).
     */
    @Override
    public Long persist(final String key) {
        return persist(SafeEncoder.encode(key));
    }

    /**
     * Undo a {@link #expire(String, int) expire} at turning the expire key into
     * a normal key.
     * <p>
     * Time complexity: O(1)
     *
     * @param key
     * @return Integer reply, specifically: 1: the key is now persist. 0: the
     * key is not persist (only happens when key not set).
     */
    @Override
    public Long persist(final byte[] key) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.persist(finallyKey);
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
    public Long rpushx(final String key, final String... string) {
        return rpushx(SafeEncoder.encode(key), SafeEncoder.encodeMany(string));
    }

    @Override
    public Long rpushx(final byte[] key, final byte[]... string) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.rpushx(finallyKey, string);
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
    public Long linsert(final String key, final BinaryClient.LIST_POSITION where,
                        final String pivot, final String value) {
        return linsert(SafeEncoder.encode(key), where,
                SafeEncoder.encode(pivot), SafeEncoder.encode(value));
    }

    @Override
    public Long linsert(final byte[] key, final BinaryClient.LIST_POSITION where,
                        final byte[] pivot, final byte[] value) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.linsert(finallyKey, where, pivot,
                    value);
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

    /**
     * Sets or clears the bit at offset in the string value stored at key
     *
     * @param key
     * @param offset
     * @param value
     * @return
     */
    @Override
    public Boolean setbit(String key, long offset, boolean value) {
        return setbit(SafeEncoder.encode(key), offset, value);
    }

    /**
     * Sets or clears the bit at offset in the string value stored at key
     *
     * @param key
     * @param offset
     * @param value
     * @return
     */
    @Override
    public Boolean setbit(byte[] key, long offset, boolean value) {
        Jedis jedis = null;
        Boolean ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.setbit(finallyKey, offset, value);
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
    public Boolean setbit(String key, long offset, String value) {
        return setbit(SafeEncoder.encode(key), offset,
                SafeEncoder.encode(value));
    }

    @Override
    public Boolean setbit(byte[] key, long offset, byte[] value) {
        Jedis jedis = null;
        Boolean ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.setbit(finallyKey, offset,
                    value);
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

    /**
     * Returns the bit value at offset in the string value stored at key
     *
     * @param key
     * @param offset
     * @return
     */
    @Override
    public Boolean getbit(String key, long offset) {
        return getbit(SafeEncoder.encode(key), offset);
    }

    /**
     * Returns the bit value at offset in the string value stored at key
     *
     * @param key
     * @param offset
     * @return
     */
    @Override
    public Boolean getbit(byte[] key, long offset) {
        Jedis jedis = null;
        Boolean ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.getbit(finallyKey, offset);
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
    public Long setrange(String key, long offset, String value) {
        return setrange(SafeEncoder.encode(key), offset,
                SafeEncoder.encode(value));
    }

    @Override
    public Long setrange(byte[] key, long offset, byte[] value) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.setrange(finallyKey, offset,
                    value);
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
    public String getrange(String key, long startOffset, long endOffset) {
        return SafeEncoder.encode(getrange(SafeEncoder.encode(key),
                startOffset, endOffset));
    }

    @Override
    public byte[] getrange(byte[] key, long startOffset, long endOffset) {
        Jedis jedis = null;
        byte[] ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis
                    .getrange(finallyKey, startOffset, endOffset);
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

    public Long bitpos(final String key, final boolean value) {
        return bitpos(SafeEncoder.encode(key), value);
    }

    public Long bitpos(final byte[] key, final boolean value) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.bitpos(finallyKey, value);
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

    public Long bitpos(final String key, final boolean value,
                       final BitPosParams params) {
        return bitpos(SafeEncoder.encode(key), value, params);
    }

    public Long bitpos(final byte[] key, final boolean value,
                       final BitPosParams params) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.bitpos(finallyKey, value, params);
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

    /**
     * 注意：这里没有给key加前后缀，所以可能导致key删除失败
     *
     * @param script
     * @param keyCount
     * @param params
     * @return
     */
    @Override
    @Deprecated
    public Object eval(String script, int keyCount, String... params) {
        Jedis jedis = null;
        Object ret = null;
        // 没有key,script 本身作为session的value
        try {
            jedis = pool.getResource();
            ret = jedis.eval(script, keyCount, params);
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

    /**
     * 注意：这里没有给key加前后缀，所以可能导致key删除失败
     *
     * @param script
     * @param keyCount
     * @param params
     * @return
     */
    @Override
    @Deprecated
    public Object eval(byte[] script, int keyCount, byte[]... params) {
        Jedis jedis = null;
        Object ret = null;
        // 没有key,script 本身作为session的value
        try {
            jedis = pool.getResource();
            ret = jedis.eval(script, keyCount, params);
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
    public Object eval(String script, List<String> keys, List<String> args) {
        Jedis jedis = null;
        Object ret = null;
        // script 没有key
        try {
            jedis = pool.getResource();

            List<String> newKeys = new ArrayList<>(keys.size());
            for (String key : keys) {
                newKeys.add(WRedisUtil.concatKey(clientConfig, key));
            }

            ret = jedis.eval(script, newKeys, args);
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
    public Object eval(byte[] script, List<byte[]> keys, List<byte[]> args) {
        Jedis jedis = null;
        Object ret = null;
        // script 没有key
        try {
            jedis = pool.getResource();

            List<byte[]> newKeys = new ArrayList<byte[]>(keys.size());
            for (byte[] key : keys) {
                newKeys.add(WRedisUtil.concatKey(clientConfig, key));
            }
            ret = jedis.eval(script, newKeys, args);
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

    /**
     * 注意：这里没有给key加前后缀，所以可能导致key删除失败
     *
     * @param script
     * @return
     */
    @Override
    @Deprecated
    public Object eval(String script) {
        Jedis jedis = null;
        Object ret = null;
        // script 没有key
        try {
            jedis = pool.getResource();
            ret = jedis.eval(script);
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

    /**
     * 注意：这里没有给key加前后缀，所以可能导致key删除失败
     *
     * @param script
     * @return
     */
    @Override
    @Deprecated
    public Object eval(byte[] script) {
        Jedis jedis = null;
        Object ret = null;
        // script 没有key
        try {
            jedis = pool.getResource();
            ret = jedis.eval(script);
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

    /**
     * 注意：这里没有给key加前后缀，所以可能导致key删除失败
     *
     * @param script
     * @return
     */
    @Override
    @Deprecated
    public Object evalsha(String script) {
        Jedis jedis = null;
        Object ret = null;
        // script 没有key,script内容作为value
        try {
            jedis = pool.getResource();
            ret = jedis.evalsha(script);
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


    /**
     * 注意：这里没有给key加前后缀，所以可能导致key删除失败
     *
     * @param script
     * @return
     */
    @Override
    @Deprecated
    public Object evalsha(byte[] script) {
        Jedis jedis = null;
        Object ret = null;
        // script 没有key,script内容作为value
        try {
            jedis = pool.getResource();
            ret = jedis.evalsha(script);
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
    public Object evalsha(String sha1, List<String> keys, List<String> args) {
        Jedis jedis = null;
        Object ret = null;
        try {
            jedis = pool.getResource();

            List<String> newKeys = new ArrayList<>(keys.size());
            for (String key : keys) {
                newKeys.add(WRedisUtil.concatKey(clientConfig, key));
            }

            ret = jedis.evalsha(sha1, newKeys, args);
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
    public Object evalsha(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
        Jedis jedis = null;
        Object ret = null;
        try {
            jedis = pool.getResource();

            List<byte[]> newKeys = new ArrayList<byte[]>(keys.size());
            for (byte[] key : keys) {
                newKeys.add(WRedisUtil.concatKey(clientConfig, key));
            }

            ret = jedis.evalsha(sha1, newKeys, args);
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


    /**
     * 注意：这里没有给key加前后缀，所以可能导致key删除失败
     *
     * @param
     * @return
     */
    public Object evalsha(String sha1, int keyCount, String... params) {
        Jedis jedis = null;
        Object ret = null;
        try {
            jedis = pool.getResource();
            ret = jedis.evalsha(sha1, keyCount, params);
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


    /**
     * 注意：这里没有给key加前后缀，所以可能导致key删除失败
     *
     * @param
     * @return
     */
    @Override
    public Object evalsha(byte[] sha1, int keyCount, byte[]... params) {
        Jedis jedis = null;
        Object ret = null;
        try {
            jedis = pool.getResource();
            ret = jedis.evalsha(sha1, keyCount, params);
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
    public Long bitcount(final String key) {
        return bitcount(SafeEncoder.encode(key));
    }

    @Override
    public Long bitcount(final byte[] key) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.bitcount(finallyKey);
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
    public Long bitcount(final String key, long start, long end) {
        return bitcount(SafeEncoder.encode(key), start, end);
    }

    @Override
    public Long bitcount(final byte[] key, long start, long end) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.bitcount(finallyKey, start, end);
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
    public String restore(final String key, final int ttl,
                          final byte[] serializedValue) {
        return restore(SafeEncoder.encode(key), ttl, serializedValue);
    }

    @Override
    public String restore(final byte[] key, final int ttl,
                          final byte[] serializedValue) {
        Jedis jedis = null;
        String ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.restore(finallyKey, ttl, serializedValue);
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
    public Long pexpire(final String key, final long milliseconds) {
        return pexpire(SafeEncoder.encode(key), milliseconds);
    }

    @Override
    public Long pexpire(final byte[] key, final long milliseconds) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.pexpire(finallyKey, milliseconds);
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
    public Long pexpireAt(final String key, final long millisecondsTimestamp) {
        return pexpireAt(SafeEncoder.encode(key), millisecondsTimestamp);
    }

    @Override
    public Long pexpireAt(final byte[] key, final long millisecondsTimestamp) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis
                    .pexpireAt(finallyKey, millisecondsTimestamp);
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
    public Long pttl(final String key) {
        return pttl(SafeEncoder.encode(key));
    }

    @Override
    public Long pttl(final byte[] key) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.pttl(finallyKey);
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
    public String psetex(final String key, final int milliseconds,
                         final String value) {
        return psetex(SafeEncoder.encode(key), milliseconds,
                SafeEncoder.encode(value));
    }

    @Override
    public String psetex(final byte[] key, final int milliseconds,
                         final byte[] value) {
        Jedis jedis = null;
        String ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.psetex(finallyKey, milliseconds,
                    value);
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

    public String set(final String key, final String value, final String nxxx) {
        return set(SafeEncoder.encode(key), SafeEncoder.encode(value),
                SafeEncoder.encode(nxxx));
    }

    public String set(final byte[] key, final byte[] value, final byte[] nxxx) {
        Jedis jedis = null;
        String ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.set(finallyKey, value,
                    nxxx);
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

    public String set(final String key, final String value, final String nxxx,
                      final String expx, final int time) {
        return set(SafeEncoder.encode(key), SafeEncoder.encode(value),
                SafeEncoder.encode(nxxx), SafeEncoder.encode(expx), time);
    }

    public String set(final byte[] key, final byte[] value, final byte[] nxxx,
                      final byte[] expx, final int time) {
        Jedis jedis = null;
        String ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.set(finallyKey, value,
                    nxxx, expx, time);
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
    public byte[] dump(final String key) {
        return dump(SafeEncoder.encode(key));
    }

    @Override
    public byte[] dump(final byte[] key) {
        Jedis jedis = null;
        byte[] ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.dump(finallyKey);
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
    public String mset(String... keysvalues) {
        return mset(SafeEncoder.encodeMany(keysvalues));
    }

    @Override
    public String mset(byte[]... keysvalues) {
        Jedis jedis = null;
        String ret = null;
        if (keysvalues.length % 2 != 0) {
            throw new JedisException(
                    "MSET arguments count should be even number!");
        }
        try {
            jedis = pool.getResource();
            ret = jedis.mset(keysvalues);
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
    public Long msetnx(String... keysvalues) {
        return msetnx(SafeEncoder.encodeMany(keysvalues));
    }

    @Override
    public Long msetnx(byte[]... keysvalues) {
        Jedis jedis = null;
        Long ret = null;
        try {
            jedis = pool.getResource();
            ret = jedis.msetnx(keysvalues);
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
    public List<byte[]> lrange(byte[] key, int start, int end) {
        return lrange(key, (long) start, (long) end);
    }

    @Override
    public String ltrim(String key, int start, int end) {
        return ltrim(key, (long) start, (long) end);
    }

    @Override
    public String ltrim(byte[] key, int start, int end) {
        return ltrim(key, (long) start, (long) end);
    }

    @Override
    public byte[] lindex(byte[] key, int index) {
        return lindex(key, (long) index);
    }

    @Override
    public String lset(String key, int index, String value) {
        return lset(key, (long) index, value);
    }

    @Override
    public String lset(byte[] key, int index, byte[] value) {
        return lset(key, (long) index, value);
    }

    @Override
    public Long lrem(String key, int count, String value) {
        return lrem(key, (long) count, value);
    }

    @Override
    public Long lrem(byte[] key, int count, byte[] value) {
        return lrem(key, (long) count, value);
    }

    @Override
    public Set<byte[]> zrange(byte[] key, int start, int end) {
        return zrange(key, (long) start, (long) end);
    }

    @Override
    public Set<byte[]> zrevrange(byte[] key, int start, int end) {
        return zrevrange(key, (long) start, (long) end);
    }

    @Override
    public Set<Tuple> zrangeWithScores(byte[] key, int start, int end) {
        return zrangeWithScores(key, (long) start, (long) end);
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(String key, int start, int end) {
        return zrevrangeWithScores(key, (long) start, (long) end);
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(byte[] key, int start, int end) {
        return zrevrangeWithScores(key, (long) start, (long) end);
    }

    @Override
    public List<String> sort(String key) {
        return SafeEncoder.encodeList(sort(SafeEncoder.encode(key)));
    }

    @Override
    public List<String> sort(String key, SortingParams sortingParameters) {
        return SafeEncoder.encodeList(sort(SafeEncoder.encode(key),
                sortingParameters));
    }

    @Override
    public Long sort(String key, SortingParams sortingParameters, String dstkey) {
        return this.sort(SafeEncoder.encode(key), sortingParameters,
                SafeEncoder.encode(dstkey));
    }

    @Override
    public Long sort(String key, String dstkey) {
        return this.sort(SafeEncoder.encode(key), SafeEncoder.encode(dstkey));
    }

    @Override
    public List<String> blpop(int timeout, String... keys) {
        return SafeEncoder.encodeList(blpop(timeout,
                SafeEncoder.encodeMany(keys)));
    }

    @Override
    public List<byte[]> blpop(int timeout, byte[]... keys) {
        Jedis jedis = null;
        List<byte[]> ret = null;
        byte[][] finallyKeys = WRedisUtil.concatKeys(clientConfig, keys);
        try {
            jedis = pool.getResource();
            ret = jedis.blpop(timeout, finallyKeys);
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
    public List<String> brpop(int timeout, String... keys) {
        return SafeEncoder.encodeList(brpop(timeout,
                SafeEncoder.encodeMany(keys)));
    }

    @Override
    public List<byte[]> brpop(int timeout, byte[]... keys) {
        Jedis jedis = null;
        List<byte[]> ret = null;
        try {
            jedis = pool.getResource();
            ret = jedis.brpop(timeout, keys);
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
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max,
                                                 String min) {
        Jedis jedis = null;
        Set<Tuple> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, SafeEncoder.encode(key));
        try {
            jedis = pool.getResource();
            ret = jedis.zrevrangeByScoreWithScores(new String(finallyKey), max, min);
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
    public Long zremrangeByRank(String key, int start, int end) {
        return this.zremrangeByRank(key, (long) start, (long) end);
    }

    @Override
    public Long zremrangeByRank(byte[] key, int start, int end) {
        return this.zremrangeByRank(key, (long) start, (long) end);
    }

    @Override
    public Long rpushx(String key, String string) {
        return this.rpushx(SafeEncoder.encode(key), SafeEncoder.encode(string));
    }

    @Override
    public Long rpushx(byte[] key, byte[] string) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.rpushx(finallyKey, string);
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
    public String brpoplpush(String srckey, String dstkey, int timeout) {
        return SafeEncoder.encode(brpoplpush(SafeEncoder.encode(srckey),
                SafeEncoder.encode(dstkey), timeout));
    }

    @Override
    public byte[] brpoplpush(byte[] srckey, byte[] dstkey, int timeout) {
        Jedis jedis = null;
        byte[] ret = null;
        try {
            jedis = pool.getResource();
            ret = jedis.brpoplpush(srckey, dstkey, timeout);
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
    public Boolean setbit(String key, int offset, boolean value) {
        return this.setbit(key, (long) offset, value);
    }

    @Override
    public String getrange(byte[] key, int startOffset, int endOffset) {
        return SafeEncoder.encode(getrange(key, (long) startOffset,
                (long) endOffset));
    }

    @Override
    public Long lpushx(String key, String string) {
        return lpushx(SafeEncoder.encode(key), SafeEncoder.encode(string));
    }

    @Override
    public Long lpushx(byte[] key, byte[] string) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.lpushx(finallyKey, string);
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
    public List<byte[]> sort(byte[] key) {
        Jedis jedis = null;
        List<byte[]> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.sort(finallyKey);
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
    public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
        Jedis jedis = null;
        List<byte[]> ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.sort(finallyKey, sortingParameters);
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
    public Long sort(byte[] key, SortingParams sortingParameters, byte[] dstkey) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.sort(finallyKey, sortingParameters, dstkey);
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
    public Long sort(byte[] key, byte[] dstkey) {
        Jedis jedis = null;
        Long ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, key);
        try {
            jedis = pool.getResource();
            ret = jedis.sort(finallyKey, dstkey);
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
    public List<String> blpop(int timeout, String key) {
        return this.blpop(timeout, new String[]{key});
    }

    @Override
    public List<byte[]> blpop(int timeout, byte[] key) {
        return this.blpop(timeout, new byte[][]{key});
    }

    @Override
    public List<String> brpop(int timeout, String key) {
        return this.brpop(timeout, new String[]{key});
    }

    /**
     *
     * 如果需要使用，可以参考com.meizu.redis.client.RedisClient
     * @param ip
     */
    @Override
    public void pipelineOp(IPiplineOperator ip) {

    }

    @Override
    public void pipelineSyncOp(IPiplineOperator ip) {

    }

    @Override
    public List<Object> pipelineSyncReturnOp(IPiplineOperator ip) {
        return null;
    }

    @Override
    public String setObject(String key, Object obj) {
        Jedis jedis = null;
        String ret = null;
        byte[] binaryValue = null;
        try {
            binaryValue = SerializationUtil.serialize(obj);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
//        String finallyKey = WRedisUtil.concatKey(clientConfig,key);
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, SafeEncoder.encode(key));
        ;
        try {
            jedis = pool.getResource();
            ret = jedis.set(finallyKey, binaryValue);
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
    public String setObject(String key, Object obj, int seconds) {
        Jedis jedis = null;
        String ret = null;
        byte[] binaryValue = null;
        try {
            binaryValue = SerializationUtil.serialize(obj);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, SafeEncoder.encode(key));
        ;
        try {
            jedis = pool.getResource();
            ret = jedis.setex(finallyKey, seconds, binaryValue);
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
    public Object getToObject(String key) {
        Jedis jedis = null;
        Object ret = null;
        byte[] finallyKey = WRedisUtil.concatKey(clientConfig, SafeEncoder.encode(key));
        try {
            jedis = pool.getResource();
            byte[] b = jedis.get(finallyKey);
            pool.returnResource(jedis);
            ret = b == null ? null : SerializationUtil.deserialize(b);
            return ret;
        } catch (JedisException je) {
            if (jedis != null) {
                if (je instanceof JedisConnectionException) {
                    pool.returnBrokenResource(jedis);
                } else {
                    pool.returnResource(jedis);
                }
            }
            LOG.error(je.getMessage(), je);
            throw je;
        } catch (Exception e) {

            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {

        }
    }

    @Override
    public byte[] getToByte(String key) {
        return get(SafeEncoder.encode(key));
    }

    @Override
    public String set(String key, String value, int seconds) {
        return setex(SafeEncoder.encode(key), seconds,
                SafeEncoder.encode(value));
    }

    @Override
    public String set(String key, byte[] by, int seconds) {
        return setex(SafeEncoder.encode(key), seconds, by);
    }

    @Override
    public String set(String key, byte[] by) {
        return set(SafeEncoder.encode(key), by);
    }

}
