package com.wakedata.common.redis.jedis.redis;

import redis.clients.jedis.Transaction;

/**
 * Created by wujiang on 2020/11/6.
 */
@Deprecated
public interface ITransactionOperator {
    void op(Transaction tx);
}
