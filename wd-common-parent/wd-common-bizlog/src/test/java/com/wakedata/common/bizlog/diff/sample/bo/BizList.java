package com.wakedata.common.bizlog.diff.sample.bo;

import com.mzt.logapi.starter.annotation.DiffLogField;
import com.wakedata.common.bizlog.basic.sample.BizOrder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * BizList
 *
 * @author focus
 * @date 2022/10/10
 **/
@Data
public class BizList {

    @DiffLogField(name = "名字")
    private String name;
    @DiffLogField(name = "订单列表")
    private List<BizOrder> orders = new ArrayList<>();

    public BizList add(String orderNo,String purchaseName,String productName){

        BizOrder order = new BizOrder();
        order.setOrderNo(orderNo);
        order.setProductName(productName);
        order.setPurchaseName(purchaseName);
        orders.add(order);

        return this;
    }
}
