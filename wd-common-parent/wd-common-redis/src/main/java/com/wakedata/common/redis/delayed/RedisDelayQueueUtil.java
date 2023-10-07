package com.wakedata.common.redis.delayed;

import com.wakedata.common.core.GlobalApplicationContext;
import com.wakedata.common.core.exception.SysException;
import com.wakedata.common.redis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 延迟信息队列
 *
 * @author hhf
 * @date 2022-04-13
 */
@Slf4j
public class RedisDelayQueueUtil {

    private static RedisDelayQueueUtil instance;

    private RedisDelayQueueUtil() {

    }

    public static RedisDelayQueueUtil getInstance() {
        if (instance == null) {
            instance = GlobalApplicationContext.getBean(RedisDelayQueueUtil.class);
            if (instance == null) {
                throw new SysException("RedisDelayQueueUtil未实例化");
            }
        }
        return instance;
    }

    @Resource
    RedissonClient redissonClient;

    /**
     * 添加队列
     *
     * @param t        DTO传输类
     * @param delay    时间数量
     * @param timeUnit 时间单位
     * @param <T>      泛型
     */
    public <T> void fireDelay(T t, long delay, TimeUnit timeUnit) {
        log.info("add to delay queue, queue name:{},time:{},timeUnit:{},content:{}", getQueueName(t), delay, timeUnit, t);
        RBlockingQueue<T> blockingFairQueue = redissonClient.getBlockingQueue(getQueueName(t));
        RDelayedQueue<T> delayedQueue = redissonClient.getDelayedQueue(blockingFairQueue);
        delayedQueue.offer(t, delay, timeUnit);
    }

    /**
     * 添加队列-秒
     *
     * @param t     DTO传输类
     * @param delay 时间数量
     * @param <T>   泛型
     */
    public <T> void fireDelaySeconds(T t, long delay) {
        fireDelay(t, delay, TimeUnit.SECONDS);
    }

    /**
     * 添加队列-分
     *
     * @param t     DTO传输类
     * @param delay 时间数量
     * @param <T>   泛型
     */
    public <T> void fireDelayMinute(T t, long delay) {
        fireDelay(t, delay, TimeUnit.MINUTES);
    }

    /**
     * 添加队列-时
     *
     * @param t     DTO传输类
     * @param delay 时间数量
     * @param <T>   泛型
     */
    public <T> void fireDelayHour(T t, long delay) {
        fireDelay(t, delay, TimeUnit.HOURS);
    }

    /**
     * 添加队列-天
     *
     * @param t     DTO传输类
     * @param delay 时间数量
     * @param <T>   泛型
     */
    public <T> void fireDelayDay(T t, long delay) {
        fireDelay(t, delay, TimeUnit.DAYS);
    }

    /**
     * 添加队列-LocalDateTime
     *
     * @param t        DTO传输类
     * @param fireTime 时间
     * @param <T>      泛型
     */
    public <T> void fireAt(T t, LocalDateTime fireTime) {
        long duration = Duration.between(LocalDateTime.now(), fireTime).getSeconds();
        if (duration<=0) {
            throw new SysException("fireTime must be a future time");
        }
        fireDelay(t, duration, TimeUnit.SECONDS);
    }

    private <T> String getQueueName(T t) {
        return t.getClass().getName();
    }
}