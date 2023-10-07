package com.wakedata.common.bizlog.diff.sample.bo;

import com.mzt.logapi.starter.annotation.DiffLogField;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Order
 *
 * @author focus
 * @date 2022/10/11
 **/
@Data
public class Order {

    @DiffLogField(name = "订单编号")
    private String orderNo;
    @DiffLogField(name = "商品列表")
    private List<Item> items = new ArrayList<>();
    @DiffLogField(name = "订单价格")
    private Long price = 3L;

    public Order addItem(String itemNo,Long price){
        Item item = new Item();
        item.setPrice(price);
        item.setItemNo(itemNo);
        items.add(item);
        return this;
    }

}
