package com.wakedata.common.redis.lock;

import com.wakedata.common.redis.lock.annotation.LockKey;
import com.wakedata.common.redis.lock.annotation.RedisLock;
import com.wakedata.common.redis.lock.core.LockTimeoutStrategy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author hhf
 * @date 2021/12/21
 */
@Service
public class TestService {

    @RedisLock(waitTime = 10,leaseTime = 60,keys = {"#param"},lockTimeoutStrategy = LockTimeoutStrategy.FAIL_FAST)
    public String getValue(String param) throws Exception {
        System.out.println("线程:[" + Thread.currentThread().getName() + "]进入方法=》" + LocalDateTime.now());
        //线程休眠或者断点阻塞，达到一直占用锁的测试效果
        if ("sleep".equals(param)) {
            Thread.sleep(1000*3);
        }
        return "success";
    }

}
