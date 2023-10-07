package com.wakedata.common.core.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 异步执行工具类
 *
 * @author chenshaopeng
 * @date 2021/8/19
 */
public class AsyncExecutorUtil {

    public static final ThreadFactory NAMED_THREAD_FACTORY = new ThreadFactoryBuilder()
            .setNameFormat("async-pool-%d").build();

    private static final ThreadPoolExecutor EXECUTOR;

    private static final int BLOCKING_QUEUE_CAPACITY = 1024;

    static {
        EXECUTOR = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() + 1
                , 200
                , 0L
                , TimeUnit.MILLISECONDS
                , new LinkedBlockingQueue<>(BLOCKING_QUEUE_CAPACITY)
                , NAMED_THREAD_FACTORY
                // 如果队列满了则使用调用者所在的线程来执行任务，拒绝丢弃
                , new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * 异步执行
     */
    public static void exec(Runnable command){
        EXECUTOR.execute(command);
    }

    /**
     * 线程池任务数，包含进行中的与排队中的
     */
    public static int taskCount(){
        return BLOCKING_QUEUE_CAPACITY - EXECUTOR.getQueue().remainingCapacity() + EXECUTOR.getActiveCount();
    }
}
