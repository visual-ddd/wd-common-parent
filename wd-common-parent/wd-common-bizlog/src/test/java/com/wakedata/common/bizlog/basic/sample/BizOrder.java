package com.wakedata.common.bizlog.basic.sample;

import com.mzt.logapi.starter.annotation.DiffLogField;
import com.wakedata.common.bizlog.core.WKCompareID;
import lombok.Data;

/**
 * Order
 *
 * @author focus
 * @date 2022/10/9
 **/
@Data
public class BizOrder {

    @DiffLogField(name = "采购单名称")
    private String purchaseName;

    @DiffLogField(name = "订单ID", function = "Order_No")
    @WKCompareID
    private String orderNo;

    @DiffLogField(name = "生成地名称")
    private String productName;

    @Override
    public String toString(){
        return purchaseName;
    }
}
