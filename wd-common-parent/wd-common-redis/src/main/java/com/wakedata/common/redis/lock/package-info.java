/**
 * 基于redis的注解式分布式锁的实现
 *
 * @author hhf
 * @date 2021/1/7
 */
package com.wakedata.common.redis.lock;

//  使用方式:
//  application.properties新增如下配置：
//      配置项                              默认值        是否必填       配置说明
//      common.redis-lock.enable=true       false           否
//      common.redis-lock.address=          null            否          redis地址
//      common.redis-lock.password=         null            否          redis密码
//      common.redis-lock.database=3        3               否          redis数据索引
//      common.redis-lock.waitTime          60              否          获取锁最长等待时间（默认：60，单位：秒）
//      common.redis-lock.leaseTime         60              否          已获取锁后自动释放时间（默认：60，单位：秒）
//
//      common.redis-lock.cluster-server.node-addresses : redis集群配置 如 127.0.0.1:6379,127.0.0.1:6378
//      common.redis-lock.cluster-server.node-addresses和common.redis-lock.address配置一项即可


//  @RedisLock注解参数说明
//  @RedisLock可以标注9个参数，作用分别如下
//  name：lock的name，对应redis的key值。默认为：类名+方法名
//  lockType：锁的类型，目前支持（可重入锁，公平锁，读写锁）。默认为：公平锁
//  keys : 业务key
//  waitTime：获取锁最长等待时间。默认为：60s。同时也可通过common.lock-redis.waitTime统一配置
//  leaseTime：获得锁后，自动释放锁的时间。默认为：60s。同时也可通过common.lock-redis.leaseTime统一配置
//  lockTimeoutStrategy: 加锁超时的处理策略，可配置为不做处理、快速失败、阻塞等待的处理策略，默认策略为不做处理
//  customLockTimeoutStrategy: 自定义加锁超时的处理策略，需指定自定义处理的方法的方法名，并保持入参一致。
//  releaseTimeoutStrategy: 释放锁时，持有的锁已超时的处理策略，可配置为不做处理、快速失败的处理策略，默认策略为不做处理
//  customReleaseTimeoutStrategy: 自定义释放锁时，需指定自定义处理的方法的方法名，并保持入参一致。


//  加锁超时处理策略(LockTimeoutStrategy)：
//  NO_OPERATION 不做处理，继续执行业务逻辑
//  FAIL_FAST 快速失败，会抛出RedisLockTimeoutException
//  KEEP_ACQUIRE 阻塞等待，一直阻塞，直到获得锁，但在太多的尝试后，会停止获取锁并报错，此时很有可能是发生了死锁。
//  自定义(customLockTimeoutStrategy) 需指定自定义处理的方法的方法名，并保持入参一致，指定自定义处理方法后，会覆盖上述三种策略，且会拦截业务逻辑的运行。


//  释放锁时超时处理策略(ReleaseTimeoutStrategy)：
//  NO_OPERATION 不做处理，继续执行业务逻辑
//  FAIL_FAST 快速失败，会抛出RedisLockTimeoutException
//  自定义(customReleaseTimeoutStrategy) 需指定自定义处理的方法的方法名，并保持入参一致，指定自定义处理方法后，会覆盖上述两种策略,
//  执行自定义处理方法时，业务逻辑已经执行完毕，会在方法返回前和throw异常前执行。

// redisKey示例:   lock.#{RedisLock.name}.#{lockKey}
// #{RedisLock.name}为空时，使用类名+方法名替换
// #{lockKey} 为RedisLock.keys + LockKey注解标注的变量字符串集合用 ‘-’分隔符分割后的字符串

//使用示例
//@RedisLock(keys = {"#user.id","#user.name"})
//private void test(@RedisKey Long appId, User user){
//
//}