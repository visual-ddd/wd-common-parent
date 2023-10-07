package com.wakedata.common.mq.model.producer;

import com.wakedata.common.mq.model.BaseMqConfigParam;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

/**
 * 生产者发送消息 - 包装类
 *
 * @author chenshaopeng
 * @date 2021/11/10
 */
@SuperBuilder
@Getter
@ToString
public class MakeMessageBody extends BaseMqConfigParam {

    /**
     * 默认生产者队列
     */
    private static final String DEFAULT_PRODUCT_QUEUE = "defaultProductQueue";

    /**
     * 消息体
     */
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }


    public void checkAndSetQueue(){
        if(StringUtils.isBlank(super.getQueueId())){
            super.setQueueId(DEFAULT_PRODUCT_QUEUE);
        }
    }
}
