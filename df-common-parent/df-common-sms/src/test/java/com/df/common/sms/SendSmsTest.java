package com.df.common.sms;

import cn.hutool.json.JSONUtil;
import com.df.common.sms.enums.NationCodeEnum;
import com.df.common.sms.po.SmsSendContent;
import com.df.common.sms.po.SmsSendParam;
import com.df.common.sms.util.SendSmsUtil;
import com.df.common.sms.util.SmsCheckCodeUtil;
import com.df.common.core.dto.ResultDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication
public class SendSmsTest {

    @Resource
    private SendSmsUtil sendSmsUtil;

    @Test
    public void sendSms() {
        SmsSendParam<String> param = new SmsSendParam<>();
        param.setTemplateId("453392");
        param.setPhone("13075611505");
        param.setSign("wakedata");
        param.setNationCode(NationCodeEnum.CHINA.getCode());
        List<SmsSendContent> contents = new ArrayList<>();
        contents.add(new SmsSendContent().setKey("code").setValue(SmsCheckCodeUtil.generateCheckCode()));
        param.setContent(contents);
        ResultDTO<Boolean> booleanResultDTO = sendSmsUtil.sendSms(param);
        System.out.println("JSONUtil.toJsonStr(booleanResultDTO) = " + JSONUtil.toJsonStr(booleanResultDTO));
    }

}