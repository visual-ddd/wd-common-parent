package com.wakedata.common.bizlog.diff.sample;

import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import com.wakedata.common.bizlog.basic.sample.BizOrder;
import com.wakedata.common.bizlog.diff.sample.bo.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * BizLogDiffService
 *
 * @author focus
 * @date 2022/10/10
 **/
@Component
public class BizLogSingleDiffService {

    /**
     * 当个对象的更新
     */
    @LogRecord(success = "更新了订单{_DIFF{#oldOrder, #order}}",
            type = "ORDER", bizNo = "{{#order.orderNo}}",
            extra = "{{#order.toString()}}")
    public void singleUpdate(BizOrder order){
        BizOrder old = new BizOrder();
        old.setOrderNo("no00000");
        old.setPurchaseName("000采购单");
        old.setProductName("000生产单");
        LogRecordContext.putVariable("oldOrder",old);

    }

    /**
     * 存在值对象
     */
    @LogRecord(success = "更新了订单{_DIFF{#oldObjectValue, #objectValue}}",
            type = "ORDER", bizNo = "{{#oldObjectValue.bizOrder.orderNo}}",
            extra = "{{#oldObjectValue.toString()}}")
    public void singleInObject(BizSingle objectValue){

        BizSingle oldObjectValue = new BizSingle();
        BizOrder old = new BizOrder();
        old.setOrderNo("no00000");
        old.setPurchaseName("000采购单");
        old.setProductName("000生产单");
        oldObjectValue.setBizOrder(old);
        oldObjectValue.setName("旧名称");
        oldObjectValue.setNoChanage("不变");

        LogRecordContext.putVariable("oldObjectValue",oldObjectValue);
    }

    /**
     * 多重内嵌
     */
    @LogRecord(success = "更新了列表对象{_DIFF{#oldBiz, #biz}}",
            type = "ORDER", bizNo = "{{#biz.innerName}}",
            extra = "{{#biz.toString()}}")
    public void bizInnerSingle(BizInnerSingle biz){

        BizOrder oldOrder = new BizOrder();
        oldOrder.setOrderNo("no00000");
        oldOrder.setPurchaseName("000采购单");
        oldOrder.setProductName("000生产单");


        BizSingle oldBiz = new BizSingle();
        oldBiz.setName("旧的名字");
        oldBiz.setBizOrder(oldOrder);

        BizInnerSingle oldInnerSigle = new BizInnerSingle();
        oldInnerSigle.setInnerName("旧的内嵌对象的名字");
        oldInnerSigle.setBizSingle(oldBiz);

        LogRecordContext.putVariable("oldBiz",oldInnerSigle);
    }


    /**
     * 循环内嵌
     */
    @LogRecord(success = "更新了对象{_DIFF{#oldycleInner, #cycleInner}}",
            type = "ORDER", bizNo = "0001")
    public void cycleInner(CycleInner1 cycleInner){
        CycleInner1  oldCyclInner= new CycleInner1();
        oldCyclInner.setName("CycleInner1旧的名字");

        CycleInner2 cycleInner2 = new CycleInner2();
        cycleInner2.setName("cycleInner2旧的名字");

        cycleInner2.setCycleInner1(oldCyclInner);
        oldCyclInner.setCycleInner2(cycleInner2);

        LogRecordContext.putVariable("oldycleInner",oldCyclInner);
    }

}
