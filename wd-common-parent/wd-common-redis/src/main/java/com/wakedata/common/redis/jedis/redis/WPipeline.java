package com.wakedata.common.redis.jedis.redis;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * wakedata pipeline实现，主要是惟客keyPrefix 跟kyeSuffix
 *
 * @author pengxu
 * @Date 2019/9/27.
 */
@Deprecated
public class WPipeline extends Pipeline {

    private WRedisClientConfig clientConfig;

    public WPipeline(WRedisClientConfig clientConfig,Client client) {
        this.clientConfig = clientConfig;
        super.client = client;
    }

    @Override
    public Response<List<String>> brpop(String... keys) {
        byte[][] keysArray = WRedisUtil.concatKeys(clientConfig, SafeEncoder.encodeMany(keys));
        String[] finalKeys = SafeEncoder.encodeMany(keysArray);
        return super.brpop(finalKeys);
    }

    @Override
    public Response<List<String>> brpop(int timeout, String... keys) {
        byte[][] keysArray = WRedisUtil.concatKeys(clientConfig, SafeEncoder.encodeMany(keys));
        return super.brpop(timeout, keysArray);
    }

    @Override
    public Response<List<String>> blpop(String... args) {
        byte[][] keysArray = WRedisUtil.concatKeys(clientConfig, SafeEncoder.encodeMany(args));
        String[] finalKeys = SafeEncoder.encodeMany(keysArray);
        return super.blpop(finalKeys);
    }

    @Override
    public Response<List<String>> blpop(int timeout, String... keys) {
        byte[][] keysArray = WRedisUtil.concatKeys(clientConfig, SafeEncoder.encodeMany(keys));
        return super.blpop(timeout, keysArray);
    }

    @Override
    public Response<Map<String, String>> blpopMap(int timeout, String... keys) {
        byte[][] keysArray = WRedisUtil.concatKeys(clientConfig, SafeEncoder.encodeMany(keys));
        String[] finalKeys = SafeEncoder.encodeMany(keysArray);
        return super.blpopMap(timeout, finalKeys);
    }

    @Override
    public Response<List<byte[]>> brpop(byte[]... args) {
        byte[][] keysArray = WRedisUtil.concatKeys(clientConfig, args);
        return super.brpop(keysArray);
    }

    @Override
    public Response<List<String>> brpop(int timeout, byte[]... keys) {
        byte[][] keysArray = WRedisUtil.concatKeys(clientConfig, keys);
        return super.brpop(timeout, keysArray);
    }

    @Override
    public Response<Map<String, String>> brpopMap(int timeout, String... keys) {
        byte[][] keysArray = WRedisUtil.concatKeys(clientConfig, SafeEncoder.encodeMany(keys));
        String[] finalKeys = SafeEncoder.encodeMany(keysArray);
        return super.brpopMap(timeout, finalKeys);
    }

    @Override
    public Response<List<byte[]>> blpop(byte[]... args) {
        byte[][] keysArray = WRedisUtil.concatKeys(clientConfig, args);
        return super.blpop(keysArray);
    }

    @Override
    public Response<List<String>> blpop(int timeout, byte[]... keys) {
        byte[][] keysArray = WRedisUtil.concatKeys(clientConfig, keys);
        return super.blpop(timeout, keysArray);
    }

    @Override
    public Response<Long> del(String... keys) {
        byte[][] keysArray = WRedisUtil.concatKeys(clientConfig, SafeEncoder.encodeMany(keys));
        return super.del(keysArray);
    }

    @Override
    public Response<Long> del(byte[]... keys) {
        byte[][] keysArray = WRedisUtil.concatKeys(clientConfig, keys);
        return super.del(keysArray);
    }

    @Override
    public Response<List<String>> mget(String... keys) {
        byte[][] keysArray = WRedisUtil.concatKeys(clientConfig, SafeEncoder.encodeMany(keys));
        String[] finalKeys = SafeEncoder.encodeMany(keysArray);
        return super.mget(finalKeys);
    }

    @Override
    public Response<List<byte[]>> mget(byte[]... keys) {
        byte[][] keysArray = WRedisUtil.concatKeys(clientConfig, keys);
        return super.mget(keysArray);
    }

//    @Override
//    public Response<String> mset(String... keysvalues) {
//        List<String[]> kv = KeyValueUtil.splitKeyValue(keysvalues);
//        String[] fianlKeys = WRedisUtil.concatKeys(clientConfig, kv.get(0));
//        return super.mset(KeyValueUtil.concatKeyValue(fianlKeys, kv.get(1)));
//    }
//
//    @Override
//    public Response<String> mset(byte[]... keysvalues) {
//        List<byte[][]> kv = KeyValueUtil.splitKeyValue(keysvalues);
//        byte[][] fianlKeys = WRedisUtil.concatKeys(clientConfig, kv.get(0));
//        return super.mset(KeyValueUtil.concatKeyValue(fianlKeys, kv.get(1)));
//    }
//
//    @Override
//    public Response<Long> msetnx(String... keysvalues) {
//        List<String[]> kv = KeyValueUtil.splitKeyValue(keysvalues);
//        String[] fianlKeys = WRedisUtil.concatKeys(clientConfig, kv.get(0));
//        return super.msetnx(KeyValueUtil.concatKeyValue(fianlKeys, kv.get(1)));
//    }
//
//    @Override
//    public Response<Long> msetnx(byte[]... keysvalues) {
//        List<byte[][]> kv = KeyValueUtil.splitKeyValue(keysvalues);
//        byte[][] fianlKeys = WRedisUtil.concatKeys(clientConfig, kv.get(0));
//        return super.msetnx(KeyValueUtil.concatKeyValue(fianlKeys, kv.get(1)));
//    }

    @Override
    public Response<String> rename(String oldkey, String newkey) {
        return super.rename(WRedisUtil.concatKey(clientConfig, oldkey),
            WRedisUtil.concatKey(clientConfig, newkey));
    }

    @Override
    public Response<String> rename(byte[] oldkey, byte[] newkey) {
        return super.rename(WRedisUtil.concatKey(clientConfig, oldkey),
            WRedisUtil.concatKey(clientConfig, newkey));
    }

    @Override
    public Response<Long> renamenx(String oldkey, String newkey) {
        return super.renamenx(WRedisUtil.concatKey(clientConfig, oldkey),
            WRedisUtil.concatKey(clientConfig, newkey));
    }

    @Override
    public Response<Long> renamenx(byte[] oldkey, byte[] newkey) {
        return super.renamenx(WRedisUtil.concatKey(clientConfig, oldkey),
            WRedisUtil.concatKey(clientConfig, newkey));
    }

    @Override
    public Response<String> rpoplpush(String srckey, String dstkey) {
        return super.rpoplpush(WRedisUtil.concatKey(clientConfig, srckey),
            WRedisUtil.concatKey(clientConfig, dstkey));
    }

    @Override
    public Response<byte[]> rpoplpush(byte[] srckey, byte[] dstkey) {
        return super.rpoplpush(WRedisUtil.concatKey(clientConfig, srckey),
            WRedisUtil.concatKey(clientConfig, dstkey));
    }

    @Override
    public Response<Set<String>> sdiff(String... keys) {
        return super.sdiff(WRedisUtil.concatKeys(clientConfig, keys));
    }

    @Override
    public Response<Set<byte[]>> sdiff(byte[]... keys) {
        return super.sdiff(WRedisUtil.concatKeys(clientConfig, keys));
    }

    @Override
    public Response<Long> sdiffstore(String dstkey, String... keys) {
        return super.sdiffstore(WRedisUtil.concatKey(clientConfig, dstkey),
            WRedisUtil.concatKeys(clientConfig, keys));
    }

    @Override
    public Response<Long> sdiffstore(byte[] dstkey, byte[]... keys) {
        return super.sdiffstore(WRedisUtil.concatKey(clientConfig, dstkey),
            WRedisUtil.concatKeys(clientConfig, keys));
    }

    @Override
    public Response<Set<String>> sinter(String... keys) {
        return super.sinter(WRedisUtil.concatKeys(clientConfig, keys));
    }

    @Override
    public Response<Set<byte[]>> sinter(byte[]... keys) {
        return super.sinter(WRedisUtil.concatKeys(clientConfig, keys));
    }

    @Override
    public Response<Long> sinterstore(String dstkey, String... keys) {
        return super.sinterstore(WRedisUtil.concatKey(clientConfig, dstkey),
            WRedisUtil.concatKeys(clientConfig, keys));
    }

    @Override
    public Response<Long> sinterstore(byte[] dstkey, byte[]... keys) {
        return super.sinterstore(WRedisUtil.concatKey(clientConfig, dstkey),
            WRedisUtil.concatKeys(clientConfig, keys));
    }

    @Override
    public Response<Long> smove(String srckey, String dstkey, String member) {
        return super.smove(WRedisUtil.concatKey(clientConfig, srckey),
            WRedisUtil.concatKey(clientConfig, dstkey), member);
    }

    @Override
    public Response<Long> smove(byte[] srckey, byte[] dstkey, byte[] member) {
        return super.smove(WRedisUtil.concatKey(clientConfig, srckey),
            WRedisUtil.concatKey(clientConfig, dstkey), member);
    }

    @Override
    public Response<Long> sort(String key, SortingParams sortingParameters, String dstkey) {
        return super.sort(WRedisUtil.concatKey(clientConfig, key), sortingParameters,
            WRedisUtil.concatKey(clientConfig, dstkey));
    }

    @Override
    public Response<Long> sort(byte[] key, SortingParams sortingParameters, byte[] dstkey) {
        return super.sort(WRedisUtil.concatKey(clientConfig, key), sortingParameters,
            WRedisUtil.concatKey(clientConfig, dstkey));
    }

    @Override
    public Response<Long> sort(String key, String dstkey) {
        return super.sort(WRedisUtil.concatKey(clientConfig, key),
            WRedisUtil.concatKey(clientConfig, dstkey));
    }

    @Override
    public Response<Long> sort(byte[] key, byte[] dstkey) {
        return super.sort(WRedisUtil.concatKey(clientConfig, key),
            WRedisUtil.concatKey(clientConfig, dstkey));
    }

    @Override
    public Response<Set<String>> sunion(String... keys) {
        return super.sunion(WRedisUtil.concatKeys(clientConfig, keys));
    }

    @Override
    public Response<Set<byte[]>> sunion(byte[]... keys) {
        return super.sunion(WRedisUtil.concatKeys(clientConfig, keys));
    }

    @Override
    public Response<Long> sunionstore(String dstkey, String... keys) {
        return super.sunionstore(WRedisUtil.concatKey(clientConfig, dstkey),
            WRedisUtil.concatKeys(clientConfig, keys));
    }

    @Override
    public Response<Long> sunionstore(byte[] dstkey, byte[]... keys) {
        return super.sunionstore(WRedisUtil.concatKey(clientConfig, dstkey),
            WRedisUtil.concatKeys(clientConfig, keys));
    }

    @Override
    public Response<Long> zinterstore(String dstkey, String... sets) {
        return super.zinterstore(WRedisUtil.concatKey(clientConfig, dstkey),
            WRedisUtil.concatKeys(clientConfig, sets));
    }

    @Override
    public Response<Long> zinterstore(byte[] dstkey, byte[]... sets) {
        return super.zinterstore(WRedisUtil.concatKey(clientConfig, dstkey),
            WRedisUtil.concatKeys(clientConfig, sets));
    }

    @Override
    public Response<Long> zinterstore(String dstkey, ZParams params, String... sets) {
        return super.zinterstore(WRedisUtil.concatKey(clientConfig, dstkey), params,
            WRedisUtil.concatKeys(clientConfig, sets));
    }

    @Override
    public Response<Long> zinterstore(byte[] dstkey, ZParams params, byte[]... sets) {
        return super.zinterstore(WRedisUtil.concatKey(clientConfig, dstkey), params,
            WRedisUtil.concatKeys(clientConfig, sets));

    }

    @Override
    public Response<Long> zunionstore(String dstkey, String... sets) {
        return super.zunionstore(WRedisUtil.concatKey(clientConfig, dstkey),
            WRedisUtil.concatKeys(clientConfig, sets));
    }

    @Override
    public Response<Long> zunionstore(byte[] dstkey, byte[]... sets) {
        return super.zunionstore(WRedisUtil.concatKey(clientConfig, dstkey),
            WRedisUtil.concatKeys(clientConfig, sets));
    }

    @Override
    public Response<Long> zunionstore(String dstkey, ZParams params, String... sets) {
        return super.zunionstore(WRedisUtil.concatKey(clientConfig, dstkey), params,
            WRedisUtil.concatKeys(clientConfig, sets));
    }

    @Override
    public Response<Long> zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {
        return super.zunionstore(WRedisUtil.concatKey(clientConfig, dstkey), params,
            WRedisUtil.concatKeys(clientConfig, sets));
    }

    @Override
    public Response<String> brpoplpush(String source, String destination, int timeout) {
        return super.brpoplpush(WRedisUtil.concatKey(clientConfig, source),
            WRedisUtil.concatKey(clientConfig, destination), timeout);
    }

    @Override
    public Response<byte[]> brpoplpush(byte[] source, byte[] destination, int timeout) {
        return super.brpoplpush(WRedisUtil.concatKey(clientConfig, source),
            WRedisUtil.concatKey(clientConfig, destination), timeout);
    }

    @Override
    public Response<Long> bitop(BitOP op, byte[] destKey, byte[]... srcKeys) {
        return super.bitop(op, WRedisUtil.concatKey(clientConfig, destKey),WRedisUtil.concatKeys(clientConfig, srcKeys));
    }

    @Override
    public Response<Long> bitop(BitOP op, String destKey, String... srcKeys) {
        return super.bitop(op, WRedisUtil.concatKey(clientConfig, destKey),WRedisUtil.concatKeys(clientConfig, srcKeys));
    }

    @Override
    public Response<String> pfmerge(byte[] destkey, byte[]... sourcekeys) {
        return super.pfmerge(WRedisUtil.concatKey(clientConfig, destkey),WRedisUtil.concatKeys(clientConfig, sourcekeys));
    }

    @Override
    public Response<String> pfmerge(String destkey, String... sourcekeys) {
        return super.pfmerge(WRedisUtil.concatKey(clientConfig, destkey),WRedisUtil.concatKeys(clientConfig, sourcekeys));
    }

    @Override
    public Response<Long> pfcount(String... keys) {
        return super.pfcount(WRedisUtil.concatKeys(clientConfig, keys));
    }

    @Override
    public Response<Long> pfcount(byte[]... keys) {
        return super.pfcount(WRedisUtil.concatKeys(clientConfig, keys));
    }

    @Override
    public Response<Long> append(String key, String value) {
        return super.append(WRedisUtil.concatKey(clientConfig, key), value);
    }

    @Override
    public Response<Long> append(byte[] key, byte[] value) {
        return super.append(WRedisUtil.concatKey(clientConfig, key), value);
    }

    @Override
    public Response<List<String>> blpop(String key) {
        return super.blpop(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<List<String>> brpop(String key) {
        return super.brpop(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<List<byte[]>> blpop(byte[] key) {
        return super.blpop(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<List<byte[]>> brpop(byte[] key) {
        return super.brpop(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> decr(String key) {
        return super.decr(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> decr(byte[] key) {
        return super.decr(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> decrBy(String key, long integer) {
        return super.decrBy(WRedisUtil.concatKey(clientConfig, key), integer);
    }

    @Override
    public Response<Long> decrBy(byte[] key, long integer) {
        return super.decrBy(WRedisUtil.concatKey(clientConfig, key), integer);
    }

    @Override
    public Response<Long> del(String key) {
        return super.del(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> del(byte[] key) {
        return super.del(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Boolean> exists(String key) {
        return super.exists(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Boolean> exists(byte[] key) {
        return super.exists(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> expire(String key, int seconds) {
        return super.expire(WRedisUtil.concatKey(clientConfig, key), seconds);
    }

    @Override
    public Response<Long> expire(byte[] key, int seconds) {
        return super.expire(WRedisUtil.concatKey(clientConfig, key), seconds);
    }

    @Override
    public Response<Long> expireAt(String key, long unixTime) {
        return super.expireAt(WRedisUtil.concatKey(clientConfig, key), unixTime);
    }

    @Override
    public Response<Long> expireAt(byte[] key, long unixTime) {
        return super.expireAt(WRedisUtil.concatKey(clientConfig, key), unixTime);
    }

    @Override
    public Response<String> get(String key) {
        return super.get(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<byte[]> get(byte[] key) {
        return super.get(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Boolean> getbit(String key, long offset) {
        return super.getbit(WRedisUtil.concatKey(clientConfig, key), offset);
    }

    @Override
    public Response<Boolean> getbit(byte[] key, long offset) {
        return super.getbit(WRedisUtil.concatKey(clientConfig, key), offset);
    }

    @Override
    public Response<Long> bitpos(String key, boolean value) {
        return super.bitpos(WRedisUtil.concatKey(clientConfig, key), value);
    }

    @Override
    public Response<Long> bitpos(String key, boolean value, BitPosParams params) {
        return super.bitpos(WRedisUtil.concatKey(clientConfig, key), value, params);
    }

    @Override
    public Response<Long> bitpos(byte[] key, boolean value) {
        return super.bitpos(WRedisUtil.concatKey(clientConfig, key), value);
    }

    @Override
    public Response<Long> bitpos(byte[] key, boolean value, BitPosParams params) {
        return super.bitpos(WRedisUtil.concatKey(clientConfig, key), value, params);
    }

    @Override
    public Response<String> getrange(String key, long startOffset, long endOffset) {
        return super.getrange(WRedisUtil.concatKey(clientConfig, key), startOffset, endOffset);
    }

    @Override
    public Response<String> getSet(String key, String value) {
        return super.getSet(WRedisUtil.concatKey(clientConfig, key), value);
    }

    @Override
    public Response<byte[]> getSet(byte[] key, byte[] value) {
        return super.getSet(WRedisUtil.concatKey(clientConfig, key), value);
    }

    @Override
    public Response<Long> getrange(byte[] key, long startOffset, long endOffset) {
        return super.getrange(WRedisUtil.concatKey(clientConfig, key), startOffset, endOffset);
    }

    @Override
    public Response<Long> hdel(String key, String... field) {
        return super.hdel(WRedisUtil.concatKey(clientConfig, key), field);
    }

    @Override
    public Response<Long> hdel(byte[] key, byte[]... field) {
        return super.hdel(WRedisUtil.concatKey(clientConfig, key), field);
    }

    @Override
    public Response<Boolean> hexists(String key, String field) {
        return super.hexists(WRedisUtil.concatKey(clientConfig, key), field);
    }

    @Override
    public Response<Boolean> hexists(byte[] key, byte[] field) {
        return super.hexists(WRedisUtil.concatKey(clientConfig, key), field);
    }

    @Override
    public Response<String> hget(String key, String field) {
        return super.hget(WRedisUtil.concatKey(clientConfig, key), field);
    }

    @Override
    public Response<byte[]> hget(byte[] key, byte[] field) {
        return super.hget(WRedisUtil.concatKey(clientConfig, key), field);
    }

    @Override
    public Response<Map<String, String>> hgetAll(String key) {
        return super.hgetAll(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Map<byte[], byte[]>> hgetAll(byte[] key) {
        return super.hgetAll(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> hincrBy(String key, String field, long value) {
        return super.hincrBy(WRedisUtil.concatKey(clientConfig, key), field, value);
    }

    @Override
    public Response<Long> hincrBy(byte[] key, byte[] field, long value) {
        return super.hincrBy(WRedisUtil.concatKey(clientConfig, key), field, value);
    }

    @Override
    public Response<Set<String>> hkeys(String key) {
        return super.hkeys(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Set<byte[]>> hkeys(byte[] key) {
        return super.hkeys(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> hlen(String key) {
        return super.hlen(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> hlen(byte[] key) {
        return super.hlen(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<List<String>> hmget(String key, String... fields) {
        return super.hmget(WRedisUtil.concatKey(clientConfig, key), fields);
    }

    @Override
    public Response<List<byte[]>> hmget(byte[] key, byte[]... fields) {
        return super.hmget(WRedisUtil.concatKey(clientConfig, key), fields);
    }

//    @Override
//    public Response<String> hmset(String key, Map<String, String> hash) {
//        String[] values = KeyValueUtil.splitStringKeyAndValue(hash).get(1);
//        return super.hmset(WRedisUtil.concatKey(clientConfig, key), hash);
//    }
//
//    @Override
//    public Response<String> hmset(byte[] key, Map<byte[], byte[]> hash) {
//        byte[][] values = KeyValueUtil.splitByteKeyAndValue(hash).get(1);
//        return super.hmset(WRedisUtil.concatKey(clientConfig, key), hash);// 不改变value
//    }

    @Override
    public Response<Long> hset(String key, String field, String value) {
        return super.hset(WRedisUtil.concatKey(clientConfig, key), field, value);
    }

    @Override
    public Response<Long> hset(byte[] key, byte[] field, byte[] value) {
        return super.hset(WRedisUtil.concatKey(clientConfig, key), field, value);
    }

    @Override
    public Response<Long> hsetnx(String key, String field, String value) {
        return super.hsetnx(WRedisUtil.concatKey(clientConfig, key), field, value);
    }

    @Override
    public Response<Long> hsetnx(byte[] key, byte[] field, byte[] value) {
        return super.hsetnx(WRedisUtil.concatKey(clientConfig, key), field, value);
    }

    @Override
    public Response<List<String>> hvals(String key) {
        return super.hvals(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<List<byte[]>> hvals(byte[] key) {
        return super.hvals(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> incr(String key) {
        return super.incr(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> incr(byte[] key) {
        return super.incr(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> incrBy(String key, long integer) {
        return super.incrBy(WRedisUtil.concatKey(clientConfig, key), integer);
    }

    @Override
    public Response<Long> incrBy(byte[] key, long integer) {
        return super.incrBy(WRedisUtil.concatKey(clientConfig, key), integer);
    }

    @Override
    public Response<String> lindex(String key, long index) {
        return super.lindex(WRedisUtil.concatKey(clientConfig, key), index);
    }

    @Override
    public Response<byte[]> lindex(byte[] key, long index) {
        return super.lindex(WRedisUtil.concatKey(clientConfig, key), index);
    }

    @Override
    public Response<Long> linsert(String key, LIST_POSITION where, String pivot, String value) {
        return super.linsert(WRedisUtil.concatKey(clientConfig, key), where, pivot, value);
    }

    @Override
    public Response<Long> linsert(byte[] key, LIST_POSITION where, byte[] pivot, byte[] value) {
        return super.linsert(WRedisUtil.concatKey(clientConfig, key), where, pivot, value);
    }

    @Override
    public Response<Long> llen(String key) {
        return super.llen(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> llen(byte[] key) {
        return super.llen(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<String> lpop(String key) {
        return super.lpop(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<byte[]> lpop(byte[] key) {
        return super.lpop(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> lpush(String key, String... string) {
        return super.lpush(WRedisUtil.concatKey(clientConfig, key), string);
    }

    @Override
    public Response<Long> lpush(byte[] key, byte[]... string) {
        return super.lpush(WRedisUtil.concatKey(clientConfig, key), string);
    }

    @Override
    public Response<Long> lpushx(String key, String... string) {
        return super.lpushx(WRedisUtil.concatKey(clientConfig, key), string);
    }

    @Override
    public Response<Long> lpushx(byte[] key, byte[]... bytes) {
        return super.lpushx(WRedisUtil.concatKey(clientConfig, key), bytes);
    }

    @Override
    public Response<List<String>> lrange(String key, long start, long end) {
        return super.lrange(WRedisUtil.concatKey(clientConfig, key), start, end);
    }

    @Override
    public Response<List<byte[]>> lrange(byte[] key, long start, long end) {
        return super.lrange(WRedisUtil.concatKey(clientConfig, key), start, end);
    }

    @Override
    public Response<Long> lrem(String key, long count, String value) {
        return super.lrem(WRedisUtil.concatKey(clientConfig, key), count, value);
    }

    @Override
    public Response<Long> lrem(byte[] key, long count, byte[] value) {
        return super.lrem(WRedisUtil.concatKey(clientConfig, key), count, value);
    }

    @Override
    public Response<String> lset(String key, long index, String value) {
        return super.lset(WRedisUtil.concatKey(clientConfig, key), index, value);
    }

    @Override
    public Response<String> lset(byte[] key, long index, byte[] value) {
        return super.lset(WRedisUtil.concatKey(clientConfig, key), index, value);
    }

    @Override
    public Response<String> ltrim(String key, long start, long end) {
        return super.ltrim(WRedisUtil.concatKey(clientConfig, key), start, end);
    }

    @Override
    public Response<String> ltrim(byte[] key, long start, long end) {
        return super.ltrim(WRedisUtil.concatKey(clientConfig, key), start, end);
    }

    @Override
    public Response<Long> move(String key, int dbIndex) {
        return super.move(WRedisUtil.concatKey(clientConfig, key), dbIndex);
    }

    @Override
    public Response<Long> move(byte[] key, int dbIndex) {
        return super.move(WRedisUtil.concatKey(clientConfig, key), dbIndex);
    }

    @Override
    public Response<Long> persist(String key) {
        return super.persist(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> persist(byte[] key) {
        return super.persist(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<String> rpop(String key) {
        return super.rpop(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<byte[]> rpop(byte[] key) {
        return super.rpop(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> rpush(String key, String... string) {
        return super.rpush(WRedisUtil.concatKey(clientConfig, key), string);
    }

    @Override
    public Response<Long> rpush(byte[] key, byte[]... string) {
        return super.rpush(WRedisUtil.concatKey(clientConfig, key), string);
    }

    @Override
    public Response<Long> rpushx(String key, String... string) {
        return super.rpushx(WRedisUtil.concatKey(clientConfig, key), string);
    }

    @Override
    public Response<Long> rpushx(byte[] key, byte[]... string) {
        return super.rpushx(WRedisUtil.concatKey(clientConfig, key), string);
    }

    @Override
    public Response<Long> sadd(String key, String... member) {
        return super.sadd(WRedisUtil.concatKey(clientConfig, key), member);
    }

    @Override
    public Response<Long> sadd(byte[] key, byte[]... member) {
        return super.sadd(WRedisUtil.concatKey(clientConfig, key), member);
    }

    @Override
    public Response<Long> scard(String key) {
        return super.scard(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> scard(byte[] key) {
        return super.scard(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<String> set(String key, String value) {
        return super.set(WRedisUtil.concatKey(clientConfig, key), value);
    }

    @Override
    public Response<String> set(byte[] key, byte[] value) {
        return super.set(WRedisUtil.concatKey(clientConfig, key), value);
    }

    @Override
    public Response<Boolean> setbit(String key, long offset, boolean value) {
        return super.setbit(WRedisUtil.concatKey(clientConfig, key), offset, value);
    }

    @Override
    public Response<Boolean> setbit(byte[] key, long offset, byte[] value) {
        return super.setbit(WRedisUtil.concatKey(clientConfig, key), offset, value);
    }

    @Override
    public Response<String> setex(String key, int seconds, String value) {
        return super.setex(WRedisUtil.concatKey(clientConfig, key), seconds, value);
    }

    @Override
    public Response<String> setex(byte[] key, int seconds, byte[] value) {
        return super.setex(WRedisUtil.concatKey(clientConfig, key), seconds, value);
    }

    @Override
    public Response<Long> setnx(String key, String value) {
        return super.setnx(WRedisUtil.concatKey(clientConfig, key), value);
    }

    @Override
    public Response<Long> setnx(byte[] key, byte[] value) {
        return super.setnx(WRedisUtil.concatKey(clientConfig, key), value);
    }

    @Override
    public Response<Long> setrange(String key, long offset, String value) {
        return super.setrange(WRedisUtil.concatKey(clientConfig, key), offset, value);
    }

    @Override
    public Response<Long> setrange(byte[] key, long offset, byte[] value) {
        return super.setrange(WRedisUtil.concatKey(clientConfig, key), offset, value);
    }

    @Override
    public Response<Boolean> sismember(String key, String member) {
        return super.sismember(WRedisUtil.concatKey(clientConfig, key), member);
    }

    @Override
    public Response<Boolean> sismember(byte[] key, byte[] member) {
        return super.sismember(WRedisUtil.concatKey(clientConfig, key), member);
    }

    @Override
    public Response<Set<String>> smembers(String key) {
        return super.smembers(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Set<byte[]>> smembers(byte[] key) {
        return super.smembers(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<List<String>> sort(String key) {
        return super.sort(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<List<byte[]>> sort(byte[] key) {
        return super.sort(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<List<String>> sort(String key, SortingParams sortingParameters) {
        return super.sort(WRedisUtil.concatKey(clientConfig, key), sortingParameters);
    }

    @Override
    public Response<List<byte[]>> sort(byte[] key, SortingParams sortingParameters) {
        return super.sort(WRedisUtil.concatKey(clientConfig, key), sortingParameters);
    }

    @Override
    public Response<String> spop(String key) {
        return super.spop(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<byte[]> spop(byte[] key) {
        return super.spop(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<String> srandmember(String key) {
        return super.srandmember(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<List<String>> srandmember(String key, int count) {
        return super.srandmember(WRedisUtil.concatKey(clientConfig, key), count);
    }

    @Override
    public Response<byte[]> srandmember(byte[] key) {
        return super.srandmember(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<List<byte[]>> srandmember(byte[] key, int count) {
        return super.srandmember(WRedisUtil.concatKey(clientConfig, key), count);
    }

    @Override
    public Response<Long> srem(String key, String... member) {
        return super.srem(WRedisUtil.concatKey(clientConfig, key), member);
    }

    @Override
    public Response<Long> srem(byte[] key, byte[]... member) {
        return super.srem(WRedisUtil.concatKey(clientConfig, key), member);
    }

    @Override
    public Response<Long> strlen(String key) {
        return super.strlen(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> strlen(byte[] key) {
        return super.strlen(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<String> substr(String key, int start, int end) {
        return super.substr(WRedisUtil.concatKey(clientConfig, key), start, end);
    }

    @Override
    public Response<String> substr(byte[] key, int start, int end) {
        return super.substr(WRedisUtil.concatKey(clientConfig, key), start, end);
    }

    @Override
    public Response<Long> ttl(String key) {
        return super.ttl(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> ttl(byte[] key) {
        return super.ttl(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<String> type(String key) {
        return super.type(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<String> type(byte[] key) {
        return super.type(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> zadd(String key, double score, String member) {
        return super.zadd(WRedisUtil.concatKey(clientConfig, key), score, member);
    }

    @Override
    public Response<Long> zadd(String key, Map<String, Double> scoreMembers) {
        return super.zadd(WRedisUtil.concatKey(clientConfig, key), scoreMembers);
    }

    @Override
    public Response<Long> zadd(byte[] key, double score, byte[] member) {
        return super.zadd(WRedisUtil.concatKey(clientConfig, key), score, member);
    }

    @Override
    public Response<Long> zcard(String key) {
        return super.zcard(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> zcard(byte[] key) {
        return super.zcard(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> zcount(String key, double min, double max) {
        return super.zcount(WRedisUtil.concatKey(clientConfig, key), min, max);
    }

    @Override
    public Response<Long> zcount(String key, String min, String max) {
        return super.zcount(WRedisUtil.concatKey(clientConfig, key), min, max);
    }

    @Override
    public Response<Long> zcount(byte[] key, double min, double max) {
        return super.zcount(WRedisUtil.concatKey(clientConfig, key), min, max);
    }

    @Override
    public Response<Double> zincrby(String key, double score, String member) {
        return super.zincrby(WRedisUtil.concatKey(clientConfig, key), score, member);
    }

    @Override
    public Response<Double> zincrby(byte[] key, double score, byte[] member) {
        return super.zincrby(WRedisUtil.concatKey(clientConfig, key), score, member);
    }

    @Override
    public Response<Set<String>> zrange(String key, long start, long end) {
        return super.zrange(WRedisUtil.concatKey(clientConfig, key), start, end);
    }

    @Override
    public Response<Set<byte[]>> zrange(byte[] key, long start, long end) {
        return super.zrange(WRedisUtil.concatKey(clientConfig, key), start, end);
    }

    @Override
    public Response<Set<String>> zrangeByScore(String key, double min, double max) {
        return super.zrangeByScore(WRedisUtil.concatKey(clientConfig, key), min, max);
    }

    @Override
    public Response<Set<byte[]>> zrangeByScore(byte[] key, double min, double max) {
        return super.zrangeByScore(WRedisUtil.concatKey(clientConfig, key), min, max);
    }

    @Override
    public Response<Set<String>> zrangeByScore(String key, String min, String max) {
        return super.zrangeByScore(WRedisUtil.concatKey(clientConfig, key), min, max);
    }

    @Override
    public Response<Set<byte[]>> zrangeByScore(byte[] key, byte[] min, byte[] max) {
        return super.zrangeByScore(WRedisUtil.concatKey(clientConfig, key), min, max);
    }

    @Override
    public Response<Set<String>> zrangeByScore(String key, double min, double max, int offset,
        int count) {
        return super.zrangeByScore(WRedisUtil.concatKey(clientConfig, key), min, max, offset, count);
    }

    @Override
    public Response<Set<String>> zrangeByScore(String key, String min, String max, int offset,
        int count) {
        return super.zrangeByScore(WRedisUtil.concatKey(clientConfig, key), min, max, offset, count);
    }

    @Override
    public Response<Set<byte[]>> zrangeByScore(byte[] key, double min, double max, int offset,
        int count) {
        return super.zrangeByScore(WRedisUtil.concatKey(clientConfig, key), min, max, offset, count);
    }

    @Override
    public Response<Set<byte[]>> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset,
        int count) {
        return super.zrangeByScore(WRedisUtil.concatKey(clientConfig, key), min, max, offset, count);
    }

    @Override
    public Response<Set<Tuple>> zrangeByScoreWithScores(String key, double min, double max) {
        return super.zrangeByScoreWithScores(WRedisUtil.concatKey(clientConfig, key), min, max);
    }

    @Override
    public Response<Set<Tuple>> zrangeByScoreWithScores(String key, String min, String max) {
        return super.zrangeByScoreWithScores(WRedisUtil.concatKey(clientConfig, key), min, max);
    }

    @Override
    public Response<Set<Tuple>> zrangeByScoreWithScores(byte[] key, double min, double max) {
        return super.zrangeByScoreWithScores(WRedisUtil.concatKey(clientConfig, key), min, max);
    }

    @Override
    public Response<Set<Tuple>> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
        return super.zrangeByScoreWithScores(WRedisUtil.concatKey(clientConfig, key), min, max);
    }

    @Override
    public Response<Set<Tuple>> zrangeByScoreWithScores(String key, double min, double max,
        int offset, int count) {
        return super.zrangeByScoreWithScores(WRedisUtil.concatKey(clientConfig, key), min, max, offset, count);
    }

    @Override
    public Response<Set<Tuple>> zrangeByScoreWithScores(String key, String min, String max,
        int offset, int count) {
        return super.zrangeByScoreWithScores(WRedisUtil.concatKey(clientConfig, key), min, max, offset, count);
    }

    @Override
    public Response<Set<Tuple>> zrangeByScoreWithScores(byte[] key, double min, double max,
        int offset, int count) {
        return super.zrangeByScoreWithScores(WRedisUtil.concatKey(clientConfig, key), min, max, offset, count);
    }

    @Override
    public Response<Set<Tuple>> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max,
        int offset, int count) {
        return super.zrangeByScoreWithScores(WRedisUtil.concatKey(clientConfig, key), min, max, offset, count);
    }

    @Override
    public Response<Set<String>> zrevrangeByScore(String key, double max, double min) {
        return super.zrevrangeByScore(WRedisUtil.concatKey(clientConfig, key), max, min);
    }

    @Override
    public Response<Set<byte[]>> zrevrangeByScore(byte[] key, double max, double min) {
        return super.zrevrangeByScore(WRedisUtil.concatKey(clientConfig, key), max, min);
    }

    @Override
    public Response<Set<String>> zrevrangeByScore(String key, String max, String min) {
        return super.zrevrangeByScore(WRedisUtil.concatKey(clientConfig, key), max, min);
    }

    @Override
    public Response<Set<byte[]>> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
        return super.zrevrangeByScore(WRedisUtil.concatKey(clientConfig, key), max, min);
    }

    @Override
    public Response<Set<String>> zrevrangeByScore(String key, double max, double min, int offset,
        int count) {
        return super.zrevrangeByScore(WRedisUtil.concatKey(clientConfig, key), max, min, offset, count);
    }

    @Override
    public Response<Set<String>> zrevrangeByScore(String key, String max, String min, int offset,
        int count) {
        return super.zrevrangeByScore(WRedisUtil.concatKey(clientConfig, key), max, min, offset, count);
    }

    @Override
    public Response<Set<byte[]>> zrevrangeByScore(byte[] key, double max, double min, int offset,
        int count) {
        return super.zrevrangeByScore(WRedisUtil.concatKey(clientConfig, key), max, min, offset, count);
    }

    @Override
    public Response<Set<byte[]>> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset,
        int count) {
        return super.zrevrangeByScore(WRedisUtil.concatKey(clientConfig, key), max, min, offset, count);
    }

    @Override
    public Response<Set<Tuple>> zrevrangeByScoreWithScores(String key, double max, double min) {
        return super.zrevrangeByScoreWithScores(WRedisUtil.concatKey(clientConfig, key), max, min);
    }

    @Override
    public Response<Set<Tuple>> zrevrangeByScoreWithScores(String key, String max, String min) {
        return super.zrevrangeByScoreWithScores(WRedisUtil.concatKey(clientConfig, key), max, min);
    }

    @Override
    public Response<Set<Tuple>> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
        return super.zrevrangeByScoreWithScores(WRedisUtil.concatKey(clientConfig, key), max, min);
    }

    @Override
    public Response<Set<Tuple>> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min) {
        return super.zrevrangeByScoreWithScores(WRedisUtil.concatKey(clientConfig, key), max, min);
    }

    @Override
    public Response<Set<Tuple>> zrevrangeByScoreWithScores(String key, double max, double min,
        int offset, int count) {
        return super.zrevrangeByScoreWithScores(WRedisUtil.concatKey(clientConfig, key), max, min, offset, count);
    }

    @Override
    public Response<Set<Tuple>> zrevrangeByScoreWithScores(String key, String max, String min,
        int offset, int count) {
        return super.zrevrangeByScoreWithScores(WRedisUtil.concatKey(clientConfig, key), max, min, offset, count);
    }

    @Override
    public Response<Set<Tuple>> zrevrangeByScoreWithScores(byte[] key, double max, double min,
        int offset, int count) {
        return super.zrevrangeByScoreWithScores(WRedisUtil.concatKey(clientConfig, key), max, min, offset, count);
    }

    @Override
    public Response<Set<Tuple>> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min,
        int offset, int count) {
        return super.zrevrangeByScoreWithScores(WRedisUtil.concatKey(clientConfig, key), max, min, offset, count);
    }

    @Override
    public Response<Set<Tuple>> zrangeWithScores(String key, long start, long end) {
        return super.zrangeWithScores(WRedisUtil.concatKey(clientConfig, key), start, end);
    }

    @Override
    public Response<Set<Tuple>> zrangeWithScores(byte[] key, long start, long end) {
        return super.zrangeWithScores(WRedisUtil.concatKey(clientConfig, key), start, end);
    }

    @Override
    public Response<Long> zrank(String key, String member) {
        return super.zrank(WRedisUtil.concatKey(clientConfig, key), member);
    }

    @Override
    public Response<Long> zrank(byte[] key, byte[] member) {
        return super.zrank(WRedisUtil.concatKey(clientConfig, key), member);
    }

    @Override
    public Response<Long> zrem(String key, String... member) {
        return super.zrem(WRedisUtil.concatKey(clientConfig, key), member);
    }

    @Override
    public Response<Long> zrem(byte[] key, byte[]... member) {
        return super.zrem(WRedisUtil.concatKey(clientConfig, key), member);
    }

    @Override
    public Response<Long> zremrangeByRank(String key, long start, long end) {
        return super.zremrangeByRank(WRedisUtil.concatKey(clientConfig, key), start, end);
    }

    @Override
    public Response<Long> zremrangeByRank(byte[] key, long start, long end) {
        return super.zremrangeByRank(WRedisUtil.concatKey(clientConfig, key), start, end);
    }

    @Override
    public Response<Long> zremrangeByScore(String key, double start, double end) {
        return super.zremrangeByScore(WRedisUtil.concatKey(clientConfig, key), start, end);
    }

    @Override
    public Response<Long> zremrangeByScore(String key, String start, String end) {
        return super.zremrangeByScore(WRedisUtil.concatKey(clientConfig, key), start, end);
    }

    @Override
    public Response<Long> zremrangeByScore(byte[] key, double start, double end) {
        return super.zremrangeByScore(WRedisUtil.concatKey(clientConfig, key), start, end);
    }

    @Override
    public Response<Long> zremrangeByScore(byte[] key, byte[] start, byte[] end) {
        return super.zremrangeByScore(WRedisUtil.concatKey(clientConfig, key), start, end);
    }

    @Override
    public Response<Set<String>> zrevrange(String key, long start, long end) {
        return super.zrevrange(WRedisUtil.concatKey(clientConfig, key), start, end);
    }

    @Override
    public Response<Set<byte[]>> zrevrange(byte[] key, long start, long end) {
        return super.zrevrange(WRedisUtil.concatKey(clientConfig, key), start, end);
    }

    @Override
    public Response<Set<Tuple>> zrevrangeWithScores(String key, long start, long end) {
        return super.zrevrangeWithScores(WRedisUtil.concatKey(clientConfig, key), start, end);
    }

    @Override
    public Response<Set<Tuple>> zrevrangeWithScores(byte[] key, long start, long end) {
        return super.zrevrangeWithScores(WRedisUtil.concatKey(clientConfig, key), start, end);
    }

    @Override
    public Response<Long> zrevrank(String key, String member) {
        return super.zrevrank(WRedisUtil.concatKey(clientConfig, key), member);
    }

    @Override
    public Response<Long> zrevrank(byte[] key, byte[] member) {
        return super.zrevrank(WRedisUtil.concatKey(clientConfig, key), member);
    }

    @Override
    public Response<Double> zscore(String key, String member) {
        return super.zscore(WRedisUtil.concatKey(clientConfig, key), member);
    }

    @Override
    public Response<Double> zscore(byte[] key, byte[] member) {
        return super.zscore(WRedisUtil.concatKey(clientConfig, key), member);
    }

    @Override
    public Response<Long> zlexcount(byte[] key, byte[] min, byte[] max) {
        return super.zlexcount(WRedisUtil.concatKey(clientConfig, key), min, max);
    }

    @Override
    public Response<Long> zlexcount(String key, String min, String max) {
        return super.zlexcount(WRedisUtil.concatKey(clientConfig, key), min, max);
    }

    @Override
    public Response<Set<byte[]>> zrangeByLex(byte[] key, byte[] max, byte[] min) {
        return super.zrangeByLex(WRedisUtil.concatKey(clientConfig, key), max, min);
    }

    @Override
    public Response<Set<String>> zrangeByLex(String key, String max, String min) {
        return super.zrangeByLex(WRedisUtil.concatKey(clientConfig, key), max, min);
    }

    @Override
    public Response<Set<byte[]>> zrangeByLex(byte[] key, byte[] max, byte[] min, int offset,
        int count) {
        return super.zrangeByLex(WRedisUtil.concatKey(clientConfig, key), max, min, offset, count);
    }

    @Override
    public Response<Set<String>> zrangeByLex(String key, String max, String min, int offset,
        int count) {
        return super.zrangeByLex(WRedisUtil.concatKey(clientConfig, key), max, min, offset, count);
    }

    @Override
    public Response<Long> zremrangeByLex(byte[] key, byte[] min, byte[] max) {
        return super.zremrangeByLex(WRedisUtil.concatKey(clientConfig, key), min, max);
    }

    @Override
    public Response<Long> zremrangeByLex(String key, String min, String max) {
        return super.zremrangeByLex(WRedisUtil.concatKey(clientConfig, key), min, max);
    }

    @Override
    public Response<Long> bitcount(String key) {
        return super.bitcount(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> bitcount(String key, long start, long end) {
        return super.bitcount(WRedisUtil.concatKey(clientConfig, key), start, end);
    }

    @Override
    public Response<Long> bitcount(byte[] key) {
        return super.bitcount(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> bitcount(byte[] key, long start, long end) {
        return super.bitcount(WRedisUtil.concatKey(clientConfig, key), start, end);
    }

    @Override
    public Response<byte[]> dump(String key) {
        return super.dump(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<byte[]> dump(byte[] key) {
        return super.dump(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<String> migrate(String host, int port, String key, int destinationDb,
        int timeout) {
        return super.migrate(host, port, WRedisUtil.concatKey(clientConfig, key), destinationDb, timeout);
    }

    @Override
    public Response<String> migrate(byte[] host, int port, byte[] key, int destinationDb,
        int timeout) {
        return super.migrate(host, port, WRedisUtil.concatKey(clientConfig, key), destinationDb, timeout);
    }

    @Override
    public Response<Long> objectRefcount(String key) {
        return super.objectRefcount(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> objectRefcount(byte[] key) {
        return super.objectRefcount(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<String> objectEncoding(String key) {
        return super.objectEncoding(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<byte[]> objectEncoding(byte[] key) {
        return super.objectEncoding(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> objectIdletime(String key) {
        return super.objectIdletime(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> objectIdletime(byte[] key) {
        return super.objectIdletime(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> pexpire(String key, int milliseconds) {
        return super.pexpire(WRedisUtil.concatKey(clientConfig, key), milliseconds);
    }

    @Override
    public Response<Long> pexpire(byte[] key, int milliseconds) {
        return super.pexpire(WRedisUtil.concatKey(clientConfig, key), milliseconds);
    }

    @Override
    public Response<Long> pexpire(String key, long milliseconds) {
        return super.pexpire(WRedisUtil.concatKey(clientConfig, key), milliseconds);
    }

    @Override
    public Response<Long> pexpire(byte[] key, long milliseconds) {
        return super.pexpire(WRedisUtil.concatKey(clientConfig, key), milliseconds);
    }

    @Override
    public Response<Long> pexpireAt(String key, long millisecondsTimestamp) {
        return super.pexpireAt(WRedisUtil.concatKey(clientConfig, key), millisecondsTimestamp);
    }

    @Override
    public Response<Long> pexpireAt(byte[] key, long millisecondsTimestamp) {
        return super.pexpireAt(WRedisUtil.concatKey(clientConfig, key), millisecondsTimestamp);
    }

    @Override
    public Response<Long> pttl(String key) {
        return super.pttl(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> pttl(byte[] key) {
        return super.pttl(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<String> restore(String key, int ttl, byte[] serializedValue) {
        return super.restore(WRedisUtil.concatKey(clientConfig, key), ttl, serializedValue);
    }

    @Override
    public Response<String> restore(byte[] key, int ttl, byte[] serializedValue) {
        return super.restore(WRedisUtil.concatKey(clientConfig, key), ttl, serializedValue);
    }

    @Override
    public Response<Double> incrByFloat(String key, double increment) {
        return super.incrByFloat(WRedisUtil.concatKey(clientConfig, key), increment);
    }

    @Override
    public Response<Double> incrByFloat(byte[] key, double increment) {
        return super.incrByFloat(WRedisUtil.concatKey(clientConfig, key), increment);
    }

    @Override
    public Response<String> psetex(String key, int milliseconds, String value) {
        return super.psetex(WRedisUtil.concatKey(clientConfig, key), milliseconds, value);
    }

    @Override
    public Response<String> psetex(byte[] key, int milliseconds, byte[] value) {
        return super.psetex(WRedisUtil.concatKey(clientConfig, key), milliseconds, value);
    }

    @Override
    public Response<String> set(String key, String value, String nxxx) {
        return super.set(WRedisUtil.concatKey(clientConfig, key), value, nxxx);
    }

    @Override
    public Response<String> set(byte[] key, byte[] value, byte[] nxxx) {
        return super.set(WRedisUtil.concatKey(clientConfig, key), value, nxxx);
    }

    @Override
    public Response<String> set(String key, String value, String nxxx, String expx, int time) {
        return super.set(WRedisUtil.concatKey(clientConfig, key), value, nxxx, expx, time);
    }

    @Override
    public Response<String> set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, int time) {
        return super.set(WRedisUtil.concatKey(clientConfig, key), value, nxxx, expx, time);
    }

    @Override
    public Response<Double> hincrByFloat(String key, String field, double increment) {
        return super.hincrByFloat(WRedisUtil.concatKey(clientConfig, key), field, increment);
    }

    @Override
    public Response<Double> hincrByFloat(byte[] key, byte[] field, double increment) {
        return super.hincrByFloat(WRedisUtil.concatKey(clientConfig, key), field, increment);
    }

    @Override
    public Response<String> eval(String script) {
        return super.eval(script);
    }

    @Override
    public Response<String> eval(String script, List<String> keys, List<String> args) {
        String [] finalKeys = null;
        if(null!= keys && keys.size() > 0){
            finalKeys = new String[keys.size()];
            int i =0;
            for(String key: keys){
                finalKeys[i] = key;
                i++;
            }
        }
        return super.eval(script, Arrays.asList(finalKeys), args);
    }

    @Override
    public Response<String> eval(String script, int numKeys, String[] argv) {
        return super.eval(script, numKeys, argv);
    }

    @Override
    public Response<String> evalsha(String script) {
        return super.evalsha(script);
    }

    @Override
    public Response<String> evalsha(String sha1, List<String> keys, List<String> args) {
        String [] finalKeys = null;
        if(null!= keys && keys.size() > 0){
            finalKeys = new String[keys.size()];
            int i =0;
            for(String key: keys){
                finalKeys[i] = key;
                i++;
            }
        }
        return super.evalsha(sha1, Arrays.asList(finalKeys), args);
    }

    @Override
    public Response<String> evalsha(String sha1, int numKeys, String[] argv) {
        return super.evalsha(sha1, numKeys, argv);
    }

    @Override
    public Response<Long> pfadd(byte[] key, byte[]... elements) {
        return super.pfadd(WRedisUtil.concatKey(clientConfig, key), elements);
    }

    @Override
    public Response<Long> pfcount(byte[] key) {
        return super.pfcount(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public Response<Long> pfadd(String key, String... elements) {
        return super.pfadd(WRedisUtil.concatKey(clientConfig, key), elements);
    }

    @Override
    public Response<Long> pfcount(String key) {
        return super.pfcount(WRedisUtil.concatKey(clientConfig, key));
    }

    @Override
    public void sync() {
        super.sync();
    }

    @Override
    public List<Object> syncAndReturnAll() {
        List<Object> ret = super.syncAndReturnAll();
        return ret;

    }
}
