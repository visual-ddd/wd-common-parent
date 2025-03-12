package com.df.common.sms;

import cn.hutool.json.JSONUtil;
import com.df.common.sms.enums.NationCodeEnum;
import com.df.common.sms.po.SmsSendContent;
import com.df.common.sms.po.SmsSendParam;
import com.df.common.sms.util.SendSmsUtil;
import com.df.common.sms.util.SmsCheckCodeUtil;
import com.df.common.core.dto.ResultDTO;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {SpringBootStart.class})
@RunWith(SpringRunner.class)
public class HuaweiTest {

    @Resource
    private SendSmsUtil sendSmsUtil;

//        //必填,请参考"开发准备"获取如下数据,替换为实际值
//        String url = "https://smsapi.cn-north-4.myhuaweicloud.com:443/sms/batchSendSms/v1"; //APP接入地址(在控制台"应用管理"页面获取)+接口访问URI
//        String appKey = "c8RWg3ggEcyd4D3p94bf3Y7x1Ile"; //APP_Key
//        String appSecret = "q4Ii87BhST9vcs8wvrzN80SfD7Al"; //APP_Secret
//
//        String sender = "csms12345678"; //国内短信签名通道号或国际/港澳台短信通道号
//        String templateId = "8ff55eac1d0b478ab3c06c3c6a492300"; //模板ID
//        //条件必填,国内短信关注,当templateId指定的模板类型为通用模板时生效且必填,必须是已审核通过的,与模板类型一致的签名名称
//        //国际/港澳台短信不用关注该参数
//        String signature = "华为云短信测试"; //签名名称
//        //必填,全局号码格式(包含国家码),示例:+8615123456789,多个号码之间用英文逗号分隔
//        String receiver = "+86151****6789,+86152****7890"; //短信接收人号码
//        //选填,短信状态报告接收地址,推荐使用域名,为空或者不填表示不接收状态报告
//        String statusCallBack = "";
//        String templateParas = "[\"369751\"]"; //模板变量，此处以单变量验证码短信为例，请客户自行生成6位验证码，并定义为字符串类型，以杜绝首位0丢失的问题（例如：002569变成了2569）。

    @Test
    public void testSingleSend() {
        SmsSendParam<String> param = new SmsSendParam<>();
        param.setTemplateId("013c9c4f96cc41d188709e07776f5a98");
        param.setSender("1069368924410004192");
        param.setPhone("13422821882");
        param.setSign("华为云短信测试");
        param.setNationCode(NationCodeEnum.CHINA.getCode());
        List<SmsSendContent> contents = new ArrayList<>();
        contents.add(new SmsSendContent().setKey("${NUM_8}").setValue(SmsCheckCodeUtil.generateCheckCode()));
        param.setContent(contents);
        ResultDTO<Boolean> booleanResultDTO = sendSmsUtil.sendSms(param);
        System.out.println("JSONUtil.toJsonStr(booleanResultDTO) = " + JSONUtil.toJsonStr(booleanResultDTO));
    }

    @Test
    public void testMultipleSend() {
        SmsSendParam<List<String>> param = new SmsSendParam<>();
        param.setTemplateId("013c9c4f96cc41d188709e07776f5a98");
        param.setSender("1069368924410004192");
        List<String> phones = new ArrayList<>();
        phones.add("18673128153");
        phones.add("13422821882");
        param.setPhone(phones);
        param.setSign("华为云短信测试");
        param.setNationCode(NationCodeEnum.CHINA.getCode());
        List<SmsSendContent> contents = new ArrayList<>();
        contents.add(new SmsSendContent().setKey("${NUM_8}").setValue(SmsCheckCodeUtil.generateCheckCode()));
        param.setContent(contents);
        ResultDTO<Boolean> booleanResultDTO = sendSmsUtil.sendMultiSms(param);
        System.out.println("JSONUtil.toJsonStr(booleanResultDTO) = " + JSONUtil.toJsonStr(booleanResultDTO));
    }


}