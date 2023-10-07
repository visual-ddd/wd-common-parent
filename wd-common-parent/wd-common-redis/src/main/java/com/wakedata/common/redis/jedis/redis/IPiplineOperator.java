package com.wakedata.common.redis.jedis.redis;


import net.coobird.thumbnailator.filters.Pipeline;

/**
 * Created by wujiang on 2020/11/6.
 */
@Deprecated
public interface IPiplineOperator {
    void op(Pipeline pl);
}
