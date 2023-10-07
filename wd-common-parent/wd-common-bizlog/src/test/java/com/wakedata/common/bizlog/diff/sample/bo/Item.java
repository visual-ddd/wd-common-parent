package com.wakedata.common.bizlog.diff.sample.bo;

import com.mzt.logapi.starter.annotation.DiffLogField;
import com.wakedata.common.bizlog.core.WKCompareID;
import lombok.Data;

/**
 * Item
 *
 * @author focus
 * @date 2022/10/11
 **/
@Data
public class Item {
    @DiffLogField(name = "商品编码")
    @WKCompareID
    private String itemNo;
    @DiffLogField(name = "商品价格")
    private Long price;


}
