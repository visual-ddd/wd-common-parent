package com.wakedata.common.core.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * ThreadPoolUtil
 *
 * @author focus
 * @date 2022/3/5
 **/
@Slf4j
public class ParallelUtil {

    /**
     * 并发执行任务， 但需要等到最终结果才释放线程
     * @param suppliers
     * @param threadPoolName
     */
    public static void execute(List<Supplier> suppliers, String threadPoolName){

        List<CompletableFuture<Boolean>> taskList = new ArrayList<>();

        suppliers.forEach(supplier ->
                taskList.add(CompletableFuture.supplyAsync(supplier,
                        LocalThreadPoolExecutorUtil.getThreadPool(threadPoolName))));

        ThreadPoolUtils.getFutureListResult(taskList);
    }

    /**
     * 发执行任务， 但需要等到最终结果才释放线程
     * @param suppliers
     */
    public static void execute(List<Supplier> suppliers){
        execute(suppliers,null);
    }
}
