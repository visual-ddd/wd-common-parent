package com.wakedata.common.bizlog;

import com.mzt.logapi.starter.annotation.EnableLogRecord;
import com.wakedata.common.core.context.BaseUserInfo;
import com.wakedata.common.core.context.UserInfoContext;
import com.wakedata.common.mq.annotation.EnableMessageCenter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * TestApplication
 *
 * @author focus
 * @date 2022/10/10
 **/
@SpringBootApplication
@EnableLogRecord(tenant = "com.example.demo")
@EnableMessageCenter
public class BizLogTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(BizLogTestApplication.class, args);
    }

    @Bean
    public void userInfoContext() {
        BaseUserInfo userInfo = new BaseUserInfo();
        userInfo.setUserId("userID");
        userInfo.setAppBuId(1L);
        userInfo.setTenantId(2L);
        userInfo.setTenantName("tenantName");
        userInfo.setNickName("nickName");
        UserInfoContext.setUser(userInfo);
    }

}
