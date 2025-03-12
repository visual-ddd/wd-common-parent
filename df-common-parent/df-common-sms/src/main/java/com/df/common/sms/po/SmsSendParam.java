package com.df.common.sms.po;

import com.df.common.sms.enums.NationCodeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luomeng
 * @Description 短信发送参数
 * @createTime 2021-12-11 10:24:00
 */
@Data
@Accessors(chain = true)
public class SmsSendParam<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 短信签名
     */
    private String sign;

    /**
     * 短信模板ID
     */
    private String templateId;

    /**
     * 签名通道号
     */
    private String Sender;

    /**
     * 国际区号
     * {@link NationCodeEnum}
     */
    private String nationCode;

    /**
     * 手机号
     */
    private T phone;

    /**
     * 发送内容
     */
    private List<SmsSendContent> content = new ArrayList<>();

    public void addContent(String key, String value) {
        content.add(new SmsSendContent().setKey(key).setValue(value));
    }
}
