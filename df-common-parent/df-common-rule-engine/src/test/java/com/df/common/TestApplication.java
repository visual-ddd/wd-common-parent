package com.df.common;

import cn.hutool.http.HttpUtil;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Desc QueryWrapperX单元测试
 * @Author zkz
 * @Date 2022/1/17
 */
@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication
public class TestApplication {



    @Test
    public void test_request(){
        String body = "{}";
        HttpUtil.post("http://localhost:8099/t/req", body);
    }


}
