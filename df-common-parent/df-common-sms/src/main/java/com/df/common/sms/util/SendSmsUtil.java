package com.df.common.sms.util;

import cn.hutool.core.util.StrUtil;
import com.df.common.core.dto.ResultDTO;
import com.df.common.sms.enums.NationCodeEnum;
import com.df.common.sms.po.SmsSendParam;

import java.util.List;

/**
 * @author focus
 */
public interface SendSmsUtil {

    /**
     * 获取国际区号如果没有则默认
     *
     * @param nationCode
     * @return
     */
    default String getNationCodeIfNullDefault(String nationCode) {
        if (StrUtil.isBlank(nationCode)) {
            return NationCodeEnum.CHINA.getCode();
        }
        return nationCode;
    }

    /**
     * 发送单个短信
     *
     * @param smsSendParam 短信参数
     * @return
     */
    ResultDTO<Boolean> sendSms(SmsSendParam<String> smsSendParam);

    /**
     * 群发短信
     *
     * @param smsSendParam 短信参数
     * @return
     */
    ResultDTO<Boolean> sendMultiSms(SmsSendParam<List<String>> smsSendParam);


}
