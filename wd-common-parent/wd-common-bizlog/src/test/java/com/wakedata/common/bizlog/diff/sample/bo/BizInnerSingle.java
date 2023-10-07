package com.wakedata.common.bizlog.diff.sample.bo;

import com.mzt.logapi.starter.annotation.DiffLogField;
import lombok.Data;

/**
 * BizInnerSingle
 *
 * @author focus
 * @date 2022/10/10
 **/
@Data
public class BizInnerSingle {
    @DiffLogField(name = "内嵌对象的名字")
    private String innerName;

    @DiffLogField(name = "单个对象")
    private BizSingle bizSingle;
}
