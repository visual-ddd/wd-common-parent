package com.wakedata.common.bizlog.diff;

import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.EnableLogRecord;
import com.mzt.logapi.starter.annotation.LogRecord;
import com.wakedata.common.bizlog.BizLogTestApplication;
import com.wakedata.common.bizlog.basic.sample.BizOrder;
import com.wakedata.common.bizlog.diff.sample.BizLogListDiffService;
import com.wakedata.common.bizlog.diff.sample.bo.BizList;
import com.wakedata.common.bizlog.diff.sample.bo.Item;
import com.wakedata.common.bizlog.diff.sample.bo.Order;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
public class TestListCompare {

    @Resource
    BizLogListDiffService bizLogDiffService;

    @Test
    public void paramList(){

        List<BizOrder> orders = new ArrayList<>();
        BizOrder order = new BizOrder();
        order.setPurchaseName("P0002");
        order.setProductName("X00002");
        order.setOrderNo("xx0001");
        orders.add(order);

        bizLogDiffService.paramList(orders);
    }

    @Test
    public void test_对象数组新增(){
        Order newOrder = new Order();
        newOrder.setOrderNo("xxx001");
        newOrder.setPrice(5L);
        newOrder.addItem("0001",5L);

        Order oldOrder = new Order();
        oldOrder.setOrderNo("xxx002");
        oldOrder.setPrice(6L);
        bizLogDiffService.updateOrder(oldOrder,newOrder);
    }

    @Test
    public void test_对象数组修改(){
        Order newOrder = new Order();
        newOrder.setOrderNo("xxx001");
        newOrder.setPrice(5L);
        newOrder.addItem("0001",5L);

        Order oldOrder = new Order();
        oldOrder.setOrderNo("xxx002");
        oldOrder.setPrice(6L);
        oldOrder.addItem("0001",6L);
        bizLogDiffService.updateOrder(oldOrder,newOrder);
    }

    @Test
    public void test_对象数组删除(){
        Order newOrder = new Order();
        newOrder.setOrderNo("xxx001");
        newOrder.setPrice(5L);

        Order oldOrder = new Order();
        oldOrder.setOrderNo("xxx002");
        oldOrder.setPrice(6L);
        oldOrder.addItem("0001",6L);
        bizLogDiffService.updateOrder(oldOrder,newOrder);
    }

}
