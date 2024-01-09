package com.wakedata.common.redis.delayed;

import com.wakedata.common.redis.RedisTestApplication;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author hhf
 * @date 2021/12/21
 */
@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisTestApplication.class)
public class RedisDelayedQueueTest {

    /**
     * 获取延时消息
     *
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        TaskBody taskBodyDTO = new TaskBody();
        taskBodyDTO.setName("name");
        taskBodyDTO.setBody("delay:" + 5);
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(() -> {
            RedisDelayQueueUtil.getInstance().fireAt(taskBodyDTO, LocalDateTime.now().plusSeconds(-5));
            taskBodyDTO.setBody("delay:" + 10);
            RedisDelayQueueUtil.getInstance().fireDelaySeconds(taskBodyDTO, 10);
        });
        executorService.awaitTermination(15, TimeUnit.SECONDS);
    }
}
