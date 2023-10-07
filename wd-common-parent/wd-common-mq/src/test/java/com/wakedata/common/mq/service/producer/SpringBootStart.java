package com.wakedata.common.mq.service.producer;

import com.wakedata.common.mq.annotation.EnableMessageCenter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableMessageCenter
@SpringBootApplication
public class SpringBootStart {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootStart.class);
    }
}
