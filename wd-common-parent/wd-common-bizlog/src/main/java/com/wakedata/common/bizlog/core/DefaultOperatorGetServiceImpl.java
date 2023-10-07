package com.wakedata.common.bizlog.core;

import com.mzt.logapi.beans.Operator;
import com.mzt.logapi.service.IOperatorGetService;
import org.springframework.stereotype.Service;

/**
 * DefaultOperatorGetServiceImpl
 *
 * @author focus
 * @date 2022/10/10
 **/
@Service
public class DefaultOperatorGetServiceImpl implements IOperatorGetService {
    @Override
    public Operator getUser() {
        Operator operator = new Operator();
        operator.setOperatorId("SYSTEM");
        return operator;
    }
}
