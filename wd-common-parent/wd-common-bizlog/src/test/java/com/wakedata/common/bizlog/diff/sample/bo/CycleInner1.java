package com.wakedata.common.bizlog.diff.sample.bo;

import com.mzt.logapi.starter.annotation.DiffLogField;
import lombok.Data;

/**
 * CycleInner
 *
 * @author focus
 * @date 2022/10/10
 **/
@Data
public class CycleInner1 {

    @DiffLogField(name = "名字")
   private String name;
    @DiffLogField(name = "内嵌对象2")
   private CycleInner2 cycleInner2;
}
