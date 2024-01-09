package com.wakedata.common.redis.util;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.wakedata.common.redis.RedisTestApplication;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * @author hhf
 * @date 2021/12/21
 */
@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisTestApplication.class)
public class RedisUtilTest {

    @Test
    public void testGetSet() throws Exception {
        RedisUtil.getInstance().set("key1", "value1", 10);
        String getValue = RedisUtil.getInstance().get("key1");
        assert getValue.equals("value1");
    }

    @Test
    public void testZrange() throws Exception {

        IntStream.range(1, 10).forEach(x -> {
            Map<String, Double> scoreMap = new HashMap<>();
            scoreMap.put(String.valueOf(x), Double.parseDouble(RandomUtil.randomNumbers(4)) / 100);
            RedisUtil.getInstance().zadd("key2", scoreMap);
        });

        Set<ZSetOperations.TypedTuple<Object>> result = RedisUtil.getInstance().reverseRangeWithScores("key2", 0L, 2L);
        System.out.println(JSON.toJSONString(result, true));
        RedisUtil.getInstance().del("key2");
    }
}
