package com.wakedata.common.bizlog.basic.sample;

import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * BasicService
 *
 * @author focus
 * @date 2022/10/10
 **/
@Component
@Slf4j
public class BasicService {
    @LogRecord(
            success = "{{#order.purchaseName}}下了一个订单,购买商品「{{#order.productName}}」,测试变量「{{#innerOrder.productName}}」,下单结果:{{#_ret}}",
            type = "ORDER", bizNo = "{{#order.orderNo}}")
    public boolean createOrder(BizOrder order){

        log.info("【创建订单】orderNo={}", order.getOrderNo());
        // db insert order
        BizOrder order1 = new BizOrder();
        order1.setProductName("内部变量测试");
        LogRecordContext.putVariable("innerOrder", order1);

        return true;
    }
}
