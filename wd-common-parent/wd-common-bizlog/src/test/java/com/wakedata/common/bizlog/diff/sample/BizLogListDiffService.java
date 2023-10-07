package com.wakedata.common.bizlog.diff.sample;

import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import com.sun.org.apache.xpath.internal.operations.Or;
import com.wakedata.common.bizlog.basic.sample.BizOrder;
import com.wakedata.common.bizlog.diff.sample.bo.BizSingle;
import com.wakedata.common.bizlog.diff.sample.bo.BizList;
import com.wakedata.common.bizlog.diff.sample.bo.Item;
import com.wakedata.common.bizlog.diff.sample.bo.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * BizLogDiffService
 *
 * @author focus
 * @date 2022/10/10
 **/
@Component
public class BizLogListDiffService {

    /**
     * 参数list
     * @param orders
     */
    @LogRecord(success = "更新了订单{_DIFF{#oldOrders, #orders}}",
            type = "ORDER", bizNo = "paramList",
            extra = "{{#orders.toString()}}")
    public void paramList(List<BizOrder> orders){
        List<BizOrder> oldOrders = new ArrayList<>();
        BizOrder order = new BizOrder();
        order.setPurchaseName("P0001");
        order.setProductName("X00001");
        order.setOrderNo("xx0001");
        oldOrders.add(order);

        LogRecordContext.putVariable("oldOrders",oldOrders);
    }

    @LogRecord(success = "更新了订单{_DIFF{#oldOrder, #order}}",
            type = "ORDER", bizNo = "{{#order.orderNo}}",
            extra = "{{#order.toString()}}")
    public void updateOrder(Order oldOrder,Order order){

    }

}
