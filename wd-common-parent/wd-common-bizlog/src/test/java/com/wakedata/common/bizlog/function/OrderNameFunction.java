package com.wakedata.common.bizlog.function;

import com.mzt.logapi.service.IParseFunction;
import org.springframework.stereotype.Service;

/**
 * OoderFunction
 *
 * @author focus
 * @date 2022/10/9
 **/
@Service
public class OrderNameFunction implements IParseFunction {
    @Override
    public String functionName() {
        return "Order_Name";
    }

    @Override
    public String apply(Object value) {
        return "【Order进行了转换名字：orderNo=" + value+"】";
    }
}
