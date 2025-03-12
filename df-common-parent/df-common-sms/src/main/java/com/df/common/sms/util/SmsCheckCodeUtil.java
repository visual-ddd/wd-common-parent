package com.df.common.sms.util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author focus
 */
public class SmsCheckCodeUtil {

    private static Random random = new Random();
    private static final Integer RETRY_COUNT = 5;
    private static final Integer CHECK_CODE_MAX_LENGTH = 7;

    /**
     * 生成6位数验证码
     * @return
     */
    public static String generateCheckCode(){
        String checkCode=String.valueOf(random.nextLong()&System.currentTimeMillis());
        int retryCount = 0;
        while (checkCode.length() < CHECK_CODE_MAX_LENGTH && retryCount < RETRY_COUNT) {
            checkCode = String.valueOf(random.nextLong() & System.currentTimeMillis());
            retryCount++;
        }
        return checkCode.substring(checkCode.length()-CHECK_CODE_MAX_LENGTH,checkCode.length()-1);
    }


    /**
     * 校验手机号
     * @param regex
     * @param phoneNumber
     * @return
     */
    public static boolean validatePhone(String regex, String phoneNumber) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }
}
