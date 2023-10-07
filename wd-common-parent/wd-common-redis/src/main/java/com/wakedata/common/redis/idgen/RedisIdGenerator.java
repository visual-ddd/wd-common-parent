package com.wakedata.common.redis.idgen;


import com.wakedata.common.redis.util.RedisUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * redis唯一Id生成器
 * @author zhangtielong
 * @date 2020/12/22
 */
public class RedisIdGenerator {

    private static final String REDIS_ID_GENERATOR_PREFIX = "redis_id_generator_%s";

    /**
     * 生成刨除bizCode外千万级别容量的唯一Id，基于redis incr命令实现
     * 原子自增，适合生成带增长规律自增ID，如会员ID等场景
     * 使用时注意redis如果持久化采用RDB方式宕机造成的重复可能性
     * @param bizCode 业务区分code
     * @param type id生成业务
     * @return id
     */
    public static Long nextId(Long bizCode, RedisIdGeneratorType type) {
        String keySuffix = type.getKey() + "_" + bizCode;
        String bizKey = String.format(REDIS_ID_GENERATOR_PREFIX, keySuffix);
        if (!RedisUtil.getInstance().hasKey(bizKey)) {
            String initNum = bizCode + String.format("%08d", 1);
            // 这里不需要考虑并发问题，如果setnx成功了直接返回，否则则证明有其他线程已经对该值进行了初始化，直接递归调用返回即可
            if (RedisUtil.getInstance().setIfAbsent(bizKey, initNum)) {
                return Long.parseLong(initNum);
            } else {
                return nextId(bizCode, type);
            }
        }
        return RedisUtil.getInstance().incr(bizKey, 1);
    }

    public static Long nextId(String prefix) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String bizKey = String.format("redis_id_generator_".concat(prefix).concat("_%s"), today);
        if (!RedisUtil.getInstance().hasKey(bizKey)) {
            String initNum = today + String.format("%08d", 1);
            if (RedisUtil.getInstance().setIfAbsentWithBuildKey(bizKey, initNum)) {
                RedisUtil.getInstance().expire(bizKey, 86400L);
                return Long.parseLong(initNum);
            } else {
                return RedisUtil.getInstance().incr(bizKey, 1L);
            }
        } else {
            return RedisUtil.getInstance().incr(bizKey, 1L);
        }
    }
}
