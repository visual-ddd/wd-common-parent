package com.wakedata.common.redis.delayed.core;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.wakedata.common.redis.delayed.model.SubscriberAnnotationApplyTarget;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonShutdownException;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 创建消息消费者
 *
 * @author hhf
 * @date 2022/04/12
 */
@Slf4j
public class DelayMessageConsumer implements ApplicationRunner, DisposableBean {

    private static final Map<String, List<SubscriberAnnotationApplyTarget>> CACHE = new HashMap<>();

    /**
     * 监听redis延时消息线程池
     */
    private ThreadPoolExecutor listenPool;
    /**
     * 反射执行监听方法线程池
     */
    private ThreadPoolExecutor executorThreadPool;

    @Resource
    RedissonClient redissonClient;

    private volatile boolean SHUTDOWN = false;

    @Override
    public void run(ApplicationArguments args) {
        annotationApplyTargetSubscribeProcess();
        if (CACHE.size() == 0) {
            return;
        }
        initThreadPool();
        startConsumeThread();
    }

    private void initThreadPool() {
        listenPool = new ThreadPoolExecutor(
                CACHE.size()
                , CACHE.size()
                , 0L
                , TimeUnit.MILLISECONDS
                , new LinkedBlockingQueue<>(128)
                , new ThreadFactoryBuilder().setNameFormat("delay-msg-listen-pool").build()
                , new ThreadPoolExecutor.CallerRunsPolicy());

        executorThreadPool = new ThreadPoolExecutor(5, 10, 60L,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(10),
                new ThreadFactoryBuilder().setNameFormat("delay-msg-executor-pool").build(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    private void startConsumeThread() {
        CACHE.keySet().forEach(this::startListenThread);
    }

    /**
     * 注解订阅处理
     */
    private void annotationApplyTargetSubscribeProcess() {
        for (SubscriberAnnotationApplyTarget target : DelayMsgSubscribeAnnotationBeanPostProcessor
                .getAnnotationApplyTargetList()) {
            Class<?>[] classes = target.getMethod().getParameterTypes();
            if (classes.length == 0) {
                continue;
            }
            String queueName = classes[0].getName();
            if (CACHE.containsKey(queueName)) {
                CACHE.get(queueName).add(target);
                continue;
            }
            List<SubscriberAnnotationApplyTarget> list = new ArrayList<>();
            list.add(target);
            CACHE.put(queueName, list);
        }
    }

    /**
     * 启动线程获取队列
     *
     * @param queueName queueName
     * @param <T>       泛型
     * @return
     */
    private <T> void startListenThread(String queueName) {
        RBlockingQueue<T> blockingFairQueue = redissonClient.getBlockingQueue(queueName);
        redissonClient.getDelayedQueue(blockingFairQueue);
        listenPool.submit(() -> {
            while (!SHUTDOWN) {
                try {
                    T t = blockingFairQueue.take();
                    log.info("delay msg listen thread, queue name：{}, content:{}", queueName, t);
                    List<SubscriberAnnotationApplyTarget> targets = CACHE.get(queueName);
                    targets.forEach(target -> executorThreadPool.submit(new InvokeRunner<>(t, target)));
                } catch (RedissonShutdownException ignored) {
                } catch (Exception e) {
                    log.error("delay msg listen thread err,", e);
                }
            }
        });
    }

    @Override
    public void destroy() {
        this.SHUTDOWN = true;
        listenPool.shutdown();
        executorThreadPool.shutdown();
    }
}
