package com.wakedata.common.redis.jedis.redis;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wujiang on 2020/3/16.
 */
@Deprecated
public interface IRedisClient {
    String set(String key, String value);

    String set(byte[] key, byte[] value);

    String set(final String key, final String value, final String nxxx,
               final String expx, final int time);

    String get(String key);

    byte[] get(byte[] key);

    Boolean exists(String key);

    Boolean exists(byte[] key);

    Long del(String... keys);

    Long del(byte[]... keys);

    Long del(String key);

    Long del(byte[] key);

    String type(String key);

    String type(byte[] key);

    // Set<String> keys(String pattern);

    // Set<byte[]> keys(byte[] pattern);

    // String randomKey();

    // byte[] randomBinaryKey();

    // String rename(String oldkey, String newkey);

    // String rename(byte[] oldkey, byte[] newkey);

    // Long renamenx(String oldkey, String newkey);

    // Long renamenx(byte[] oldkey, byte[] newkey);

    Long expire(String key, int seconds);

    Long expire(byte[] key, int seconds);

    Long expireAt(String key, long unixTime);

    Long expireAt(byte[] key, long unixTime);

    Long ttl(String key);

    Long ttl(byte[] key);

    String getSet(String key, String value);

    byte[] getSet(byte[] key, byte[] value);

    List<String> mget(String... keys);

    List<byte[]> mget(byte[]... keys);

    Long setnx(String key, String value);

    Long setnx(byte[] key, byte[] value);

    String setex(String key, int seconds, String value);

    String setex(byte[] key, int seconds, byte[] value);

    String mset(String... keysvalues);

    String mset(byte[]... keysvalues);

    Long msetnx(String... keysvalues);

    Long msetnx(byte[]... keysvalues);

    Long decrBy(String key, long integer);

    Long decrBy(byte[] key, long integer);

    Long decr(String key);

    Long decr(byte[] key);

    Long incrBy(String key, long integer);

    Long incrBy(byte[] key, long integer);

    Long incr(String key);

    Long incr(byte[] key);

    Long append(String key, String value);

    Long append(byte[] key, byte[] value);

    String substr(String key, int start, int end);

    byte[] substr(byte[] key, int start, int end);

    Long hset(String key, String field, String value);

    Long hset(byte[] key, byte[] field, byte[] value);

    String hget(String key, String field);

    byte[] hget(byte[] key, byte[] field);

    Long hsetnx(String key, String field, String value);

    Long hsetnx(byte[] key, byte[] field, byte[] value);

    String hmset(String key, Map<String, String> hash);

    String hmset(byte[] key, Map<byte[], byte[]> hash);

    List<String> hmget(String key, String... fields);

    List<byte[]> hmget(byte[] key, byte[]... fields);

    Long hincrBy(byte[] key, byte[] field, long value);

    Long hincrBy(String key, String field, long value);

    Boolean hexists(String key, String field);

    Boolean hexists(byte[] key, byte[] field);

    Long hdel(String key, String... fields);

    Long hdel(byte[] key, byte[]... fields);

    Long hlen(String key);

    Long hlen(byte[] key);

    Set<String> hkeys(String key);

    Set<byte[]> hkeys(byte[] key);

    List<String> hvals(String key);

    List<byte[]> hvals(byte[] key);

    Map<String, String> hgetAll(String key);

    Map<byte[], byte[]> hgetAll(byte[] key);

    Long rpush(String key, String... strings);

    Long rpush(byte[] key, byte[]... strings);

    Long lpush(String key, String... strings);

    Long lpush(byte[] key, byte[]... strings);

    Long llen(String key);

    Long llen(byte[] key);

    List<String> lrange(String key, long start, long end);

    List<byte[]> lrange(byte[] key, int start, int end);

    String ltrim(String key, int start, int end);

    String ltrim(byte[] key, int start, int end);

    String lindex(String key, long index);

    byte[] lindex(byte[] key, int index);

    String lset(String key, int index, String value);

    String lset(byte[] key, int index, byte[] value);

    Long lrem(String key, int count, String value);

    Long lrem(byte[] key, int count, byte[] value);

    String lpop(String key);

    byte[] lpop(byte[] key);

    String rpop(String key);

    byte[] rpop(byte[] key);

    String rpoplpush(String srckey, String dstkey);

    byte[] rpoplpush(byte[] srckey, byte[] dstkey);

    Long sadd(String key, String... members);

    Long sadd(byte[] key, byte[]... members);

    Set<String> smembers(String key);

    Set<byte[]> smembers(byte[] key);

    Long srem(String key, String... members);

    Long srem(byte[] key, byte[]... members);

    String spop(String key);

    byte[] spop(byte[] key);

    Long smove(String srckey, String dstkey, String member);

    Long smove(byte[] srckey, byte[] dstkey, byte[] member);

    Long scard(String key);

    Long scard(byte[] key);

    Boolean sismember(String key, String member);

    Boolean sismember(byte[] key, byte[] member);

    Set<String> sinter(String... keys);

    Set<byte[]> sinter(byte[]... keys);

    Long sinterstore(String dstkey, String... keys);

    Long sinterstore(byte[] dstkey, byte[]... keys);

    Set<String> sunion(String... keys);

    Set<byte[]> sunion(byte[]... keys);

    Long sunionstore(String dstkey, String... keys);

    Long sunionstore(byte[] dstkey, byte[]... keys);

    Set<String> sdiff(String... keys);

    Set<byte[]> sdiff(byte[]... keys);

    Long sdiffstore(String dstkey, String... keys);

    Long sdiffstore(byte[] dstkey, byte[]... keys);

    String srandmember(String key);

    byte[] srandmember(byte[] key);

    Long zadd(String key, double score, String member);

    Long zadd(byte[] key, double score, byte[] member);

    // TODO check change
    Long zadd(String key, Map<String, Double> scoreMembers);

    // TODO check change
    Long zadd(byte[] key, Map<byte[], Double> scoreMembers);

    Set<String> zrange(String key, long start, long end);

    Set<byte[]> zrange(byte[] key, int start, int end);

    Long zrem(String key, String... members);

    Long zrem(byte[] key, byte[]... members);

    Double zincrby(String key, double score, String member);

    Double zincrby(byte[] key, double score, byte[] member);

    Long zrank(String key, String member);

    Long zrank(byte[] key, byte[] member);

    Long zrevrank(String key, String member);

    Long zrevrank(byte[] key, byte[] member);

    Set<String> zrevrange(String key, long start, long end);

    Set<byte[]> zrevrange(byte[] key, int start, int end);

    Set<Tuple> zrangeWithScores(String key, long start, long end);

    Set<Tuple> zrangeWithScores(byte[] key, int start, int end);

    Set<Tuple> zrevrangeWithScores(String key, int start, int end);

    Set<Tuple> zrevrangeWithScores(byte[] key, int start, int end);

    Long zcard(String key);

    Long zcard(byte[] key);

    Double zscore(String key, String member);

    Double zscore(byte[] key, byte[] member);

    List<String> sort(String key);

    List<String> sort(String key, SortingParams sortingParameters);

    Long sort(String key, SortingParams sortingParameters, String dstkey);

    Long sort(String key, String dstkey);

    List<String> blpop(int timeout, String... keys);

    List<byte[]> blpop(int timeout, byte[]... keys);

    List<String> brpop(int timeout, String... keys);

    List<byte[]> brpop(int timeout, byte[]... keys);

    Long zcount(String key, double min, double max);

    Long zcount(byte[] key, double min, double max);

    Long zcount(String key, String min, String max);

    Long zcount(byte[] key, byte[] min, byte[] max);

    Set<String> zrangeByScore(String key, double min, double max);

    Set<byte[]> zrangeByScore(byte[] key, double min, double max);

    Set<String> zrangeByScore(String key, String min, String max);

    Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max);

    Set<String> zrangeByScore(String key, double min, double max, int offset,
                              int count);

    Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset,
                              int count);

    Set<String> zrangeByScore(String key, String min, String max, int offset,
                              int count);

    Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset,
                              int count);

    Set<Tuple> zrangeByScoreWithScores(String key, String min, String max);

    Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max);

    Set<Tuple> zrangeByScoreWithScores(String key, double min, double max,
                                       int offset, int count);

    Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max,
                                       int offset, int count);

    Set<Tuple> zrangeByScoreWithScores(String key, String min, String max,
                                       int offset, int count);

    Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max,
                                       int offset, int count);

    Set<String> zrevrangeByScore(String key, double max, double min);

    Set<byte[]> zrevrangeByScore(byte[] key, double max, double min);

    Set<String> zrevrangeByScore(String key, String max, String min);

    Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min);

    Set<String> zrevrangeByScore(String key, double max, double min,
                                 int offset, int count);

    Set<byte[]> zrevrangeByScore(byte[] key, double max, double min,
                                 int offset, int count);

    Set<String> zrevrangeByScore(String key, String max, String min,
                                 int offset, int count);

    Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min,
                                 int offset, int count);

    Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min,
                                          int offset, int count);

    Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min,
                                          int offset, int count);

    Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min);

    Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min);

    Long zremrangeByRank(String key, int start, int end);

    Long zremrangeByRank(byte[] key, int start, int end);

    Long zremrangeByScore(String key, double start, double end);

    Long zremrangeByScore(byte[] key, double start, double end);

    Long zremrangeByScore(String key, String start, String end);

    Long zremrangeByScore(byte[] key, byte[] start, byte[] end);

    Long zunionstore(String dstkey, String... sets);

    Long zunionstore(byte[] dstkey, byte[]... sets);

    Long zunionstore(byte[] dstkey, ZParams params, byte[]... sets);

    Long zunionstore(String dstkey, ZParams params, String... sets);

    Long zinterstore(String dstkey, String... sets);

    Long zinterstore(byte[] dstkey, byte[]... sets);

    Long zinterstore(String dstkey, ZParams params, String... sets);

    Long zinterstore(byte[] dstkey, ZParams params, byte[]... sets);

    Long strlen(String key);

    Long strlen(byte[] key);

    Long persist(String key);

    Long persist(byte[] key);

    Long rpushx(String key, String string);

    Long rpushx(byte[] key, byte[] string);

    Long linsert(String key, LIST_POSITION where, String pivot, String value);

    Long linsert(byte[] key, LIST_POSITION where, byte[] pivot, byte[] value);

    String brpoplpush(String srckey, String dstkey, int timeout);

    byte[] brpoplpush(byte[] srckey, byte[] dstkey, int timeout);

    Boolean setbit(String key, int offset, boolean value);

    Boolean setbit(byte[] key, long offset, byte[] value);

    Boolean getbit(String key, long offset);

    Boolean getbit(byte[] key, long offset);

    Long setrange(String key, long offset, String value);

    Long setrange(byte[] key, long offset, byte[] value);

    String getrange(String key, long startOffset, long endOffset);

    String getrange(byte[] key, int startOffset, int endOffset);

    // Boolean subscribe(JedisPubSub jedisPubSub, String... channels);

    // Long publish(String channel, String message);

    // Boolean psubscribe(JedisPubSub jedisPubSub, String... patterns);

    Set<Tuple> zrangeByScoreWithScores(String key, double min, double max);

    Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max);

    Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min);

    Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min);

    Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min,
                                          int offset, int count);

    Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min,
                                          int offset, int count);

    Long lpushx(final String key, final String string);

    Long lpushx(final byte[] key, final byte[] string);

    // Long publish(byte[] channel, byte[] message);

    List<byte[]> sort(byte[] key);

    List<byte[]> sort(byte[] key, SortingParams sortingParameters);

    Long sort(byte[] key, SortingParams sortingParameters, byte[] dstkey);

    Long sort(byte[] key, byte[] dstkey);

    List<String> blpop(int timeout, String key);

    List<byte[]> blpop(int timeout, byte[] key);

    List<String> brpop(int timeout, String key);

    // String flushDB();

    //
    // Pipeline getPipeline();

    // String ping();

    void pipelineOp(IPiplineOperator ip);

    void pipelineSyncOp(IPiplineOperator ip);

    List<Object> pipelineSyncReturnOp(IPiplineOperator ip);

    // void startTransactionOP(ITransactionOperator it);

    // =============张伦添加========================
    /**
     * 添加序列化对象到redis 要求高性能不建议用
     *
     * @param key
     * @param obj
     * @return
     */
    String setObject(String key, Object obj);

    /**
     * 添加序列化对象到redis 要求高性能不建议用
     *
     * @param key
     * @param obj
     * @param seconds
     * @return
     */
    String setObject(String key, Object obj, int seconds);

    /**
     * 更加KEY获取redis序列化对象
     *
     * @param key
     * @return
     */
    Object getToObject(String key);

    byte[] getToByte(String key);

    String set(String key, String value, int seconds);

    String set(String key, byte[] by, int seconds);

    String set(String key, byte[] by);


    //belows added from 2.0.1

    Double incrByFloat(final byte[] key, final double value);

    Double incrByFloat(final String key, final double value);

    Double hincrByFloat(final String key, final String field, final double value);

    Double hincrByFloat(final byte[] key, final byte[] field, final double value);

    List<byte[]> lrange(final byte[] key, final long start, final long end);

    String ltrim(final String key, final long start, final long end);

    String ltrim(final byte[] key, final long start, final long end);

    byte[] lindex(final byte[] key, final long index);

    String lset(final String key, final long index, final String value);

    String lset(final byte[] key, final long index, final byte[] value);

    Long lrem(final String key, final long count, final String value);

    Long lrem(final byte[] key, final long count, final byte[] value);

    List<String> srandmember(final String key, final int count);

    List<byte[]> srandmember(final byte[] key, final int count);

    Set<byte[]> zrange(final byte[] key, final long start, final long end);

    Set<byte[]> zrevrange(final byte[] key, final long start, final long end);

    Set<Tuple> zrangeWithScores(final byte[] key, final long start,
                                final long end);

    Set<Tuple> zrevrangeWithScores(final String key, final long start,
                                   final long end);

    Set<Tuple> zrevrangeWithScores(final byte[] key, final long start,
                                   final long end);

    Long zremrangeByRank(final String key, final long start,
                         final long end);

    Long zremrangeByRank(final byte[] key, final long start,
                         final long end);

    Long lpushx(final String key, final String... string);

    Long lpushx(final byte[] key, final byte[]... string);

    Long rpushx(final String key, final String... string);

    Long rpushx(final byte[] key, final byte[]... string) ;

    Boolean setbit(String key, long offset, boolean value) ;

    Boolean setbit(byte[] key, long offset, boolean value);
    Boolean setbit(String key, long offset, String value);

    byte[] getrange(byte[] key, long startOffset, long endOffset);

    Object eval(String script, int keyCount, String... params);

    Object eval(byte[] script, int keyCount, byte[]... params);

    Object eval(String script, List<String> keys, List<String> args);

    Object eval(byte[] script, List<byte[]> keys, List<byte[]> args);

    Object eval(String script);

    Object eval(byte[] script);

    Object evalsha(String script);

    Object evalsha(byte[] script);

    Object evalsha(String sha1, List<String> keys, List<String> args) ;

    Object evalsha(byte[] sha1, List<byte[]> keys, List<byte[]> args);

    Object evalsha(String sha1, int keyCount, String... params) ;

    Object evalsha(byte[] sha1, int keyCount, byte[]... params) ;

    Long bitcount(final String key);

    Long bitcount(final byte[] key);

    Long bitcount(final String key, long start, long end);

    Long bitcount(final byte[] key, long start, long end);

    String restore(final String key, final int ttl,
                   final byte[] serializedValue);

    String restore(final byte[] key, final int ttl,
                   final byte[] serializedValue);

    Long pexpire(final String key, final long milliseconds);

    Long pexpire(final byte[] key, final long milliseconds);

    Long pexpireAt(final String key, final long millisecondsTimestamp);

    Long pexpireAt(final byte[] key, final long millisecondsTimestamp);

    Long pttl(final String key);

    Long pttl(final byte[] key);

    String psetex(final String key, final int milliseconds,
                  final String value);

    String psetex(final byte[] key, final int milliseconds,
                  final byte[] value);

    byte[] dump(final String key);

    byte[] dump(final byte[] key);

}
