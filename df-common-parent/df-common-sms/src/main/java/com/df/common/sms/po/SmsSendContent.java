package com.df.common.sms.po;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author luomeng
 * @Description 短信发送内容格式
 * @createTime 2021-12-11 10:35:00
 */
@Data
@Accessors(chain = true)
public class SmsSendContent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 参数key
     */
    private String key;

    /**
     * 具体发送内容
     */
    private String value;

}
