package com.wakedata.common.bizlog.diff.sample.func;

import com.mzt.logapi.service.IParseFunction;
import org.springframework.stereotype.Service;

/**
 * OoderFunction
 *
 * @author focus
 * @date 2022/10/9
 **/
@Service
public class OrderFunction implements IParseFunction {
    @Override
    public String functionName() {
        return "Order_No";
    }

    @Override
    public String apply(Object value) {
        return "【中文名字=" + value+"】";
    }
}
