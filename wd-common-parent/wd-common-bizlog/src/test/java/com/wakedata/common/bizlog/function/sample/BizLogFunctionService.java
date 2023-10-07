package com.wakedata.common.bizlog.function.sample;

import com.mzt.logapi.starter.annotation.LogRecord;
import com.wakedata.common.bizlog.basic.sample.BizOrder;
import org.springframework.stereotype.Component;

/**
 * BizLogFunctionService
 *
 * @author focus
 * @date 2022/10/10
 **/
@Component
public class BizLogFunctionService {

    @LogRecord(success = "没有自定义函数：更新了订单{{#order.orderNo}},更新内容为...",
            type = "ORDER", bizNo = "{{#order.orderNo}}",
            extra = "{{#order.toString()}}")
    public void updateWithOutFunction(BizOrder order){

    }


    @LogRecord(success = "添加了定义函数：更新了订单{Order_Name{#order.orderNo}},更新内容为...",
            type = "ORDER", bizNo = "{{#order.orderNo}}",
            extra = "{{#order.toString()}}")
    public void updateWithFunction(BizOrder order) {

    }
}
