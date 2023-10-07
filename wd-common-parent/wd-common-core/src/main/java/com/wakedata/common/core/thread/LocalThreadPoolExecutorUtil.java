package com.wakedata.common.core.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.wakedata.common.core.context.BaseUserInfo;
import com.wakedata.common.core.context.UserInfoContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.*;

/**
 * ContextThradPoolExecutor
 *  构建携带上下文的线程池
 * @author focus
 * @date 2022/3/5
 **/
@Slf4j
public class LocalThreadPoolExecutorUtil {

    private static ThreadPoolExecutor threadPool;
    private static int DEFAULT_CORE_POOL_SIZE = 10;
    private static int DEFAULT_MAX_POOL_SIZE = 30;
    private static long DEFAULT_MAX_ALIVE_TIME = 60L;
    private static int DEFAULT_BLOCK_QUEUE_SIZE = 100000;
    private static RejectedExecutionHandler DEFAULT_REJECTED_HANDLER = new ThreadPoolExecutor.AbortPolicy();


    public static ThreadPoolExecutor getThreadPool(String appName) {
        if (threadPool != null) {
            return threadPool;
        } else {
            if (StringUtils.isBlank(appName)) {
                appName = "default";
            }

            ThreadFactory threadFactory = (new ThreadFactoryBuilder()).setNameFormat(appName + "--customize--thread--pool--%d").build();
            Class var2 = ThreadPoolUtils.class;
            synchronized(ThreadPoolUtils.class) {
                if (threadPool == null) {
                    threadPool = new LocalThreadPoolExecutor(DEFAULT_CORE_POOL_SIZE, DEFAULT_MAX_POOL_SIZE, DEFAULT_MAX_ALIVE_TIME, TimeUnit.SECONDS, new ArrayBlockingQueue(DEFAULT_BLOCK_QUEUE_SIZE), threadFactory, DEFAULT_REJECTED_HANDLER);
                }

                return threadPool;
            }
        }
    }

    /**
     * 复写线程池，携带本地上下文的用户信息
     */
    static class LocalThreadPoolExecutor extends ThreadPoolExecutor{

        public LocalThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        public LocalThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        }

        public LocalThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        }

        public LocalThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }

        @Override
        public Future<?> submit(Runnable task) {
            BaseUserInfo baseUserInfo = UserInfoContext.getUser();
            return super.submit(()->{
                UserInfoContext.setUser(baseUserInfo);
                try{
                    task.run();
                }catch (Exception e){
                    log.error(" 线程执行失败。",e);
                }finally {
                    UserInfoContext.removeUserInfoContext();
                }
            });
        }

        @Override
        public <T> Future<T> submit(Callable<T> task) {
            BaseUserInfo baseUserInfo = UserInfoContext.getUser();
            return super.submit(new Callable<T>() {
                @Override
                public T call() throws Exception {
                    UserInfoContext.setUser(baseUserInfo);
                    try {
                        return task.call();
                    } catch (Exception e) {
                        log.error(" 线程执行失败。", e);
                    } finally {
                        UserInfoContext.removeUserInfoContext();
                    }

                    return null;
                }
            });
        }

        @Override
        public void execute(Runnable command) {

            BaseUserInfo baseUserInfo = UserInfoContext.getUser();

            super.execute(()->{
                UserInfoContext.setUser(baseUserInfo);
                try{
                    command.run();
                }catch (Exception e){
                    log.error(" 线程执行失败。",e);
                }finally {
                    UserInfoContext.removeUserInfoContext();
                }
            });
        }
    }
}
