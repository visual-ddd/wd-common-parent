package com.wakedata.common.core.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * @author dengqingtao
 * @date 2019/07/15
 */
@Slf4j
public class ThreadPoolUtils {


    public  static void execute(Runnable runnable){
        getThreadPool(null).execute(runnable);
    }


    public  static <T> Future<T> submit(Callable<T> callable){
        return   getThreadPool(null).submit(callable);
    }


    /**
     * 获取线程池
     * @param appName 应用名
     * @return 线程池对象
     */
    public static ThreadPoolExecutor getThreadPool(String appName) {
        return LocalThreadPoolExecutorUtil.getThreadPool(appName);

    }

    /**
     * 合并多线程结果
     * 将List<CompletableFuture<T>> 转化为CompletableFuture<List<T>>
     * @param futureList future并发线程列表
     * @param <T> 泛型类
     * @return CompletableFuture<List<T>>
     */
    public static <T> CompletableFuture<List<T>> sequenceFutureList(List<CompletableFuture<T>> futureList) {
        CompletableFuture<Void> allDoneFuture = CompletableFuture
            .allOf(futureList.toArray(new CompletableFuture[0]));
        return allDoneFuture.thenApply(v -> futureList.stream().map(CompletableFuture::join).collect(
            Collectors.toList()));
    }


    /**
     * 合并多线程，并直接得到多线程执行后的结果
     * 将List<CompletableFuture<T>> 转化为List<T>
     * @param futureList future并发线程列表
     * @param <T> 泛型类
     * @return List<T>
     */
    public static <T> List<T> getFutureListResult(List<CompletableFuture<T>> futureList) {

        CompletableFuture<List<T>> sequenceFuture = sequenceFutureList(futureList);
        List<T> resultList;
        try {
            resultList = sequenceFuture.get();
        } catch (InterruptedException ie) {
            log.error("getFutureListResult InterruptedException", ie);
            return null;
        } catch (ExecutionException e) {
            log.error("getFutureListResult ExecutionException", e);
            return null;
        }
        return resultList;
    }

    /**
     * 并行查询
     *
     * @param queryList 查询入参列表
     * @param function 方法 能将入参Q转化为T
     * @param <Q> 入参
     * @param <T> 出参
     * @return List<T>
     */
    public static <Q, T> List<T> getResultListByThreadPool(List<Q> queryList, Function<Q, T> function) {

        List<CompletableFuture<T>> queryFutureList = new ArrayList<>();
        for (Q query : queryList) {
            queryFutureList.add(CompletableFuture.supplyAsync(()-> function.apply(query)));
        }
        return getFutureListResult(queryFutureList);
    }




}
