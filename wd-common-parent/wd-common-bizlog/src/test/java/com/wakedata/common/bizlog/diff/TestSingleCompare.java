package com.wakedata.common.bizlog.diff;

import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.EnableLogRecord;
import com.mzt.logapi.starter.annotation.LogRecord;
import com.wakedata.common.bizlog.BizLogTestApplication;
import com.wakedata.common.bizlog.basic.sample.BizOrder;
import com.wakedata.common.bizlog.diff.sample.BizLogSingleDiffService;
import com.wakedata.common.bizlog.diff.sample.bo.BizInnerSingle;
import com.wakedata.common.bizlog.diff.sample.bo.BizSingle;
import com.wakedata.common.bizlog.diff.sample.bo.CycleInner1;
import com.wakedata.common.bizlog.diff.sample.bo.CycleInner2;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

/**
 * TestListCompare
 *
 * @author focus
 * @date 2022/10/10
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BizLogTestApplication.class})
@EnableLogRecord(tenant = "wakedata.com")
@Slf4j
public class TestSingleCompare {

    @Resource
    BizLogSingleDiffService bizLogSingleDiffService;
    @Test
    public void testSingleUpdate(){

        BizOrder order = new BizOrder();
        order.setOrderNo("NO0001");
        order.setProductName("新的生成单");
        order.setPurchaseName("新的采购单");
        bizLogSingleDiffService.singleUpdate(order);
    }

    @Test
    public void singleInnerObject(){

        BizOrder newOrder = new BizOrder();
        newOrder.setOrderNo("no00001");
        newOrder.setPurchaseName("001采购单");
        newOrder.setProductName("001生产单");

        BizSingle newObject = new BizSingle();
        newObject.setBizOrder(newOrder);
        newObject.setName("新名称");
        newObject.setNoChanage("不变");
        newObject.setCreateDate(new Date());

        LogRecordContext.putVariable("oldObjectValue",newObject);
        bizLogSingleDiffService.singleInObject(newObject);
    }


    @Test
    public void TestBizInnerSingle(){

        BizOrder newOrder = new BizOrder();
        newOrder.setOrderNo("no00001");
        newOrder.setPurchaseName("001采购单");
        newOrder.setProductName("001生产单");


        BizSingle newBiz = new BizSingle();
        newBiz.setName("新的名字");
        newBiz.setBizOrder(newOrder);

        BizInnerSingle newInnerSigle = new BizInnerSingle();
        newInnerSigle.setInnerName("新的内嵌对象的名字");
        newInnerSigle.setBizSingle(newBiz);

        bizLogSingleDiffService.bizInnerSingle(newInnerSigle);
    }


   @Test
    public void cycleInner(){
        CycleInner1  cycleInner= new CycleInner1();
        cycleInner.setName("CycleInner1的名字");

        CycleInner2 cycleInner2 = new CycleInner2();
        cycleInner2.setName("cycleInner2的名字");


       cycleInner.setCycleInner2(cycleInner2);

       CycleInner1 cycleInner1= new CycleInner1();
       cycleInner1.setName("CycleInner1新的名字");
       cycleInner2.setCycleInner1(cycleInner1);


       bizLogSingleDiffService.cycleInner(cycleInner);
    }
}
