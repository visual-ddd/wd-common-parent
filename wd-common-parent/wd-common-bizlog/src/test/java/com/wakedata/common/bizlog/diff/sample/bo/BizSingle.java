package com.wakedata.common.bizlog.diff.sample.bo;

import com.mzt.logapi.starter.annotation.DiffLogField;
import com.wakedata.common.bizlog.basic.sample.BizOrder;
import lombok.Data;

import java.util.Date;

/**
 * BizChannel
 *
 * @author focus
 * @date 2022/10/10
 **/
@Data
public class BizSingle {

    @DiffLogField(name = "名字")
    private String name;
    @DiffLogField(name = "订单对象")
    private BizOrder bizOrder;
    @DiffLogField(name = "不变的值")
    private String noChanage;
    @DiffLogField(name = "创建时间")
    private Date createDate;
}
