package com.wakedata.common.redis.idgen;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.wakedata.common.redis.RedisTestApplication;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author: hhf
 * @date: 2021/12/22
 **/
@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisTestApplication.class)
public class RedisIdGeneratorTest {

    @Test
    public void testGetSet() throws Exception {
        Set set = new ConcurrentHashSet();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        IntStream.range(0, 100).forEach(i -> executorService.submit(() -> {
            Long id = RedisIdGenerator.nextId("test");
            System.out.println(Thread.currentThread().getName()+":"+id);
            assert !set.contains(id);
            set.add(id);
        }));
        executorService.awaitTermination(3, TimeUnit.SECONDS);
    }
}
