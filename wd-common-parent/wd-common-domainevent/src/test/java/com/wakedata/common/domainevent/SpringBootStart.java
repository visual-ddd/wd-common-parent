package com.wakedata.common.domainevent;

import com.wakedata.common.mq.annotation.EnableMessageCenter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 开启MQ
@EnableMessageCenter
@SpringBootApplication(scanBasePackages = {"com.wakedata.common.domainevent"})
public class SpringBootStart {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootStart.class);
    }
}
