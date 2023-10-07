package com.wakedata.common.bizlog.basic;


import com.mzt.logapi.starter.annotation.EnableLogRecord;
import com.wakedata.common.bizlog.BizLogTestApplication;
import com.wakedata.common.bizlog.basic.sample.BizOrder;
import com.wakedata.common.bizlog.basic.sample.BasicService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Desc
 * @Author zkz
 * @Date 2021/12/13
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BizLogTestApplication.class})
@EnableLogRecord(tenant = "wakedata.com")
@Slf4j
public class TestBasicObject {

    @Resource
    BasicService basicService;
    @Test
    public void createOrder() {
        BizOrder order = new BizOrder();
        order.setOrderNo("NO0001");
        order.setProductName("新的生成单");
        order.setPurchaseName("新的采购单");
        basicService.createOrder(order);
    }


}
