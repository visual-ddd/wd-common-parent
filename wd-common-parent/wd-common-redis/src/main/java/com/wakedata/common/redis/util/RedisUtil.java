package com.wakedata.common.redis.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wakedata.common.core.GlobalApplicationContext;
import com.wakedata.common.core.constants.CommonConstant;
import com.wakedata.common.core.exception.SysException;

import com.wakedata.common.core.util.SafeEncoder;
import com.wakedata.common.core.util.SerializationUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * redis操作
 *
 * @author hhf
 * @date 2021/1/28
 */
@Slf4j
@Component
public class RedisUtil {

    private static RedisUtil instance;

    private RedisUtil() {

    }

    public static RedisUtil getInstance() {
        if (instance == null) {
            instance = GlobalApplicationContext.getBean(RedisUtil.class);
            if (instance == null) {
                throw new SysException("RedisUtil未实例化");
            }
        }
        return instance;
    }


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ValueOperations<String, String> redisValueOperations;

    @Autowired
    private HashOperations<String, String, Object> redisHashOperations;

    @Autowired
    private ListOperations<String, Object> redisListOperations;

    @Autowired
    private SetOperations<String, Object> redisSetOperations;

    @Autowired
    private ZSetOperations<String,Object> redisZSetOperations;

    @Value("${redis.prefix:dss}")
    private String redisPrefix;
    @Value("${redis.suffix:}")
    private String redisSuffix;

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        if (time <= 0) {
            return expire(key);
        }
        try {
            return redisTemplate.expire(buildKey(key), time, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }
    /**
     * 缓存立即失效
     *
     * @param key  键
     * @return
     */
    public boolean expire(String key) {
       return del(key);
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(buildKey(key), TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(buildKey(key));
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public boolean del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                return redisTemplate.delete(buildKey(key[0]));
            } else {
                return redisTemplate.delete(buildKey(CollectionUtils.arrayToList(key))) == key.length;
            }
        }
        return false;
    }

    //============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public String get(String key) {
        return key == null ? null : redisValueOperations.get(buildKey(key));
    }

    public <T> T getObject(String key, Class<T> clazz) {
        Object json = this.get(key);
        if (json == null) {
            return null;
        }
        T obj = JSON.parseObject(json.toString(), clazz);
        return obj;
    }

    public <T> List<T> getList(String key, Class<T> clz) {
        Object json = this.get(key);
        if (json == null) {
            return Lists.newArrayList();
        }
        List<T> list = JSONObject.parseArray(json.toString(), clz);
        return list;
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, String value) {
        try {
            redisValueOperations.set(buildKey(key), value);
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 普通缓存放入 "SET if Not eXists"
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean setIfAbsent(String key, String value) {
        try {
            return redisValueOperations.setIfAbsent(this.buildKey(key), value);
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 普通缓存放入 "SET if Not eXists"
     * @param key 键
     * @param value 值
     * @param timeout 超时时间
     * @return true成功 false失败
     */
    public boolean setIfAbsent(String key, String value, long timeout) {
        try {
            return redisValueOperations.setIfAbsent(this.buildKey(key), value, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 普通缓存放入 "SET if Not eXists"
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean setIfAbsentWithBuildKey(String key, String value) {
        try {
            return redisValueOperations.setIfAbsent(buildKey(key), value);
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }
    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, String value, long time) {
        try {
            if (time > 0) {
                redisValueOperations.set(buildKey(key), value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisValueOperations.increment(buildKey(key), delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisValueOperations.increment(buildKey(key), -delta);
    }

    //================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        return redisHashOperations.get(buildKey(key), item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<String, Object> hmget(String key) {
        return redisHashOperations.entries(buildKey(key));
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisHashOperations.putAll(buildKey(key), map);
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisHashOperations.putAll(buildKey(key), map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisHashOperations.put(buildKey(key), item, value);
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisHashOperations.put(buildKey(key), item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisHashOperations.delete(buildKey(key), item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisHashOperations.hasKey(buildKey(key), item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisHashOperations.increment(buildKey(key), item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisHashOperations.increment(buildKey(key), item, -by);
    }

    //============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            return redisSetOperations.members(buildKey(key));
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisSetOperations.isMember(buildKey(key), value);
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisSetOperations.add(buildKey(key), values);
        } catch (Exception e) {
            log.error("", e);
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisSetOperations.add(buildKey(key), values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            log.error("", e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return redisSetOperations.size(buildKey(key));
        } catch (Exception e) {
            log.error("", e);
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisSetOperations.remove(buildKey(key), values);
            return count;
        } catch (Exception e) {
            log.error("", e);
            return 0;
        }
    }
    //===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisListOperations.range(buildKey(key), start, end);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * 获取list缓存的所有内容
     *
     * @param key
     * @return
     */
    public List<Object> lGetAll(String key) {
        return lGet(key, 0, -1);
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisListOperations.size(buildKey(key));
        } catch (Exception e) {
            log.error("", e);
            return 0;
        }
    }


    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisListOperations.index(buildKey(key), index);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * 取出列表的第一个元素
     *
     * @param key
     * @return
     */
    public Object lGet(String key) {
        try {
            return redisListOperations.leftPop(buildKey(key));
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * 往列表尾部插入一个元素
     *
     * @param key
     * @param value
     * @return
     */
    public boolean rSet(String key, Object value) {
        try {
            redisListOperations.rightPush(buildKey(key), value);
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, Object value) {
        try {
            redisListOperations.rightPush(buildKey(key), value);
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisListOperations.rightPush(buildKey(key), value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisListOperations.rightPushAll(buildKey(key), value);
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisListOperations.rightPushAll(buildKey(key), value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisListOperations.set(buildKey(key), index, value);
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisListOperations.remove(buildKey(key), count, value);
            return remove;
        } catch (Exception e) {
            log.error("", e);
            return 0;
        }
    }


    /**
     * 返回有序集的大小
     * @param key
     * @return
     */
    public Long zcard(String key){
        try {
            return redisZSetOperations.zCard(buildKey(key));
        } catch (Exception e) {
            log.error("", e);
            return 0L;
        }
    }

    /**
     * 添加有序集
     * @param key
     * @param scoreMap
     * @return
     */
    public Long zadd(String key,Map<String,Double> scoreMap){
        try {
            if(scoreMap != null){
                Set<ZSetOperations.TypedTuple<Object>> valTypedTuples = new HashSet<>();
                for(Map.Entry<String,Double> entry : scoreMap.entrySet()){
                    valTypedTuples.add(new DefaultTypedTuple<>(entry.getKey(), entry.getValue()));
                }
                return redisZSetOperations.add(buildKey(key),valTypedTuples);
            }
            return 0L;
        } catch (Exception e) {
            log.error("", e);
            return 0L;
        }
    }

    /**
     * 移除指定区间的元素
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long zremrangeByRank(String key,Long start,Long end){
        try {
            return redisZSetOperations.removeRange(buildKey(key),start, end);
        } catch (Exception e) {
            log.error("", e);
            return 0L;
        }
    }

    /**
     * 批量移除元素
     * @param key
     * @param values
     * @return
     */
    public Long zrem(String key,Object...values){
        try {
            return redisZSetOperations.remove(buildKey(key),values);
        } catch (Exception e) {
            log.error("", e);
            return 0L;
        }
    }


    /**
     * 返回有序集中指定区间的成员
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<Object> zrevrange(String key, Long start, Long end){
        try {
            return redisZSetOperations.reverseRange(buildKey(key),start, end);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * 返回有序集中指定区间的成员,并返回得分
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> reverseRangeWithScores(String key, Long start, Long end){
        try {
            return redisZSetOperations.reverseRangeWithScores(buildKey(key),start, end);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    /**
     * 兼容旧的wkb  IRedisClient中的getToObject方法
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getToObject(String key, Class<T> clazz) {
        try {
            String buildKey = buildKey(key);
            byte[] finallyKey = SafeEncoder.encode(buildKey);
            byte[] bytes = redisTemplate.execute((RedisConnection redisConnection) -> redisConnection.get(finallyKey));
            if (bytes == null) {
                return null;
            }
            return (T) SerializationUtil.deserialize(bytes);
        } catch (IOException |ClassNotFoundException e) {
            log.error("getToObject err:", e);
        }
        return null;
    }

    /**
     * 兼容旧的wkb  IRedisClient中的setObject方法
     *
     * @param key     键
     * @param obj     对象
     * @param seconds 秒数
     * @return true/false
     */
    public boolean setObject(String key, Object obj, int seconds) {
        try {

            String buildKey = buildKey(key);
            byte[] finallyKey = SafeEncoder.encode(buildKey);
            byte[] binaryValue = SerializationUtil.serialize(obj);
            Boolean result = redisTemplate.execute((RedisConnection redisConnection) -> {
                    return redisConnection.setEx(finallyKey, seconds, binaryValue);
                }
            );

            return Objects.isNull(result) ? false : result;
        } catch (IOException ex) {
            log.error("setObject err:", ex);
        }

        return false;
    }

    public boolean setrange(String key, long offset, String value) {
        try {
            redisValueOperations.set(buildKey(key), value, offset);
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    private String buildKey(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new RuntimeException("redis key can not be empty");
        }
        if (StringUtils.isNotEmpty(redisPrefix)) {
            key = redisPrefix.concat(CommonConstant.POINT).concat(key);
        }
        if (StringUtils.isNotEmpty(redisSuffix)) {
            key = key.concat(CommonConstant.POINT).concat(redisSuffix);
        }
        return key;
    }

    private List<String> buildKey(List<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyList();
        }
        return keys.stream().map(this::buildKey).collect(Collectors.toList());
    }

    public Set<String> scan(String matchKey) {
        Set<String> keys = redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keysTmp = new HashSet<>();
            Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match("*" + matchKey + "*").count(1000).build());
            while (cursor.hasNext()) {
                keysTmp.add(new String(cursor.next()));
            }
            return keysTmp;
        });

        return keys;
    }

}