package com.wakedata.common.bizlog.function;

import com.mzt.logapi.starter.annotation.EnableLogRecord;
import com.wakedata.common.bizlog.BizLogTestApplication;
import com.wakedata.common.bizlog.basic.sample.BizOrder;
import com.wakedata.common.bizlog.function.sample.BizLogFunctionService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * TestFunction
 *
 * @author focus
 * @date 2022/10/10
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BizLogTestApplication.class})
@EnableLogRecord(tenant = "wakedata.com")
@Slf4j
public class TestFunction {

    @Resource
    BizLogFunctionService bizLogFunctionService;

    /**
     * 测试自定义函数
     */
    @Test
    public void testFunction(){
        BizOrder order = new BizOrder();
        order.setOrderNo("NO0001");
        order.setProductName("新的生成单");
        order.setPurchaseName("新的采购单");
        bizLogFunctionService.updateWithFunction(order);
        bizLogFunctionService.updateWithOutFunction(order);
    }
}
