package com.wakedata.common.core.util;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;

import java.util.Calendar;

/**
 *
 * 序列号生成器
 * @author pengxu
 * @date 2018/8/22
 */
public class SerialNumberGenerator {

    private static final int CUSTOM_NUMBER_LENGTH = 4;

    private static final int CUSTOM_NUMBER_LENGTH_2 = 2;

    private static final int CUSTOM_NUMBER_LENGTH_3 = 3;


    public static final String DEFAULT_TIME_FORMART = "yyMMddHHmmssSSS";

    /**
     * 随机序列
     * 当前时间戳后9位+自定义分隔数+3位随机数
     * @param customNumber 自定义数
     * @return
     */
    public static Long generateNum(Long customNumber) {
        long currentTime = System.currentTimeMillis();

        String timeS = String.valueOf(currentTime);
        timeS = timeS.substring(1, timeS.length());
        String customNumberString = String.valueOf(customNumber);
        if(customNumberString.length() > CUSTOM_NUMBER_LENGTH){
            customNumberString = customNumberString.substring(0, CUSTOM_NUMBER_LENGTH);
        }
        String formatUserId =String.format("%0" + CUSTOM_NUMBER_LENGTH + "d", Long.parseLong(customNumberString));
        String orderNoS = timeS+ formatUserId +randomNumber(3);
        return Long.parseLong(orderNoS);
    }

    /**
     * 随机序列，以时间戳后12位相减，主要为了防止优惠券因为券码正好落在支付宝区间
     * 当前时间戳后9位+自定义分隔数+3位随机数
     * @param customNumber 自定义数
     * @return
     */
    public static Long generateNumRevert(Long customNumber) {
        long currentTime = System.currentTimeMillis();
        long reverseTime = 10000000000000L - currentTime;

        String timeS = String.valueOf(reverseTime);
        timeS = timeS.substring(1, timeS.length());
        String customNumberString = String.valueOf(customNumber);
        if(customNumberString.length() > CUSTOM_NUMBER_LENGTH){
            customNumberString = customNumberString.substring(0, CUSTOM_NUMBER_LENGTH);
        }
        String formatUserId =String.format("%0" + CUSTOM_NUMBER_LENGTH + "d", Long.parseLong(customNumberString));
        String orderNoS = timeS+ formatUserId +randomNumber(3);
        return Long.parseLong(orderNoS);
    }

    /**
     * 随机序列
     * 当前时间戳后9位+4位随机数+自定义分隔数
     * @param customNumber 自定义数 最大支持3位
     * @return
     */
    public static Long generateTypeNum(Integer customNumber) {
        long currentTime = System.currentTimeMillis();

        String timeS = String.valueOf(currentTime);
        timeS = timeS.substring(1, timeS.length());
        String customNumberString = String.valueOf(customNumber);
        if(customNumberString.length() > CUSTOM_NUMBER_LENGTH_3){
            customNumberString = customNumberString.substring(0, CUSTOM_NUMBER_LENGTH_3);
        }
        String formatType =String.format("%0" + CUSTOM_NUMBER_LENGTH_3 + "d", Long.parseLong(customNumberString));
        String orderNoS = timeS +randomNumber(4) + formatType;
        return Long.parseLong(orderNoS);
    }

    /**
     * 使用雪花算法生成随机序列拼接业务标识
     * 需要注意的是，使用雪花算法整体id长度会是19+3(customerNumber)位共计21位，与以往业务19位不同，需要鉴别使用
     * @param customNumber 自定义数 最大支持3位
     * @return
     */
    public static String generateTypeNumUsingSnowFlake(Integer customNumber) {
        long id =  IdUtil.getSnowflake().nextId();
        String customNumberString = String.valueOf(customNumber);
        if(customNumberString.length() > CUSTOM_NUMBER_LENGTH_3){
            customNumberString = customNumberString.substring(0, CUSTOM_NUMBER_LENGTH_3);
        }
        String formatType =String.format("%0" + CUSTOM_NUMBER_LENGTH_3 + "d", Long.parseLong(customNumberString));
        return  id + formatType;
    }

    /**
     * 随机序列
     * WD + 自定义分隔数 + 当前秒级时间戳后12位 + 4位随机数
     * @param customNumber 自定义数
     * @return
     */
    public static String generateString(Integer customNumber) {
        long currentTime = System.currentTimeMillis();
        String timeS = String.valueOf(currentTime);
        timeS = timeS.substring(1, timeS.length());
        String customNumberString = String.valueOf(customNumber);
        if(customNumberString.length() > CUSTOM_NUMBER_LENGTH_2){
            customNumberString = customNumberString.substring(0, CUSTOM_NUMBER_LENGTH_2);
        }
        String formatUserId =String.format("%0" + CUSTOM_NUMBER_LENGTH_2 + "d", Long.parseLong(customNumberString));
        String orderNoS = "WD" + formatUserId + timeS  +randomNumber(4);
        return orderNoS;
    }

    private static String randomNumber(int length) {
        StringBuilder random = new StringBuilder();
        java.util.Random r = new java.util.Random();
        for (int i = 0; i < length; i++) {
            random.append(r.nextInt(10));
        }
        return random.toString();
    }

    /**
     * 随机序列
     * WD + 自定义分隔数 + 当前秒级时间戳后12位 + 4位随机数
     * @param customNumber 自定义数
     * @param str 序列号开头标识
     * @return
     */
    public static String generateString(Integer customNumber, String str) {
        long currentTime = System.currentTimeMillis();
        String timeS = String.valueOf(currentTime);
        timeS = timeS.substring(1, timeS.length());
        String customNumberString = String.valueOf(customNumber);
        if(customNumberString.length() > CUSTOM_NUMBER_LENGTH_2){
            customNumberString = customNumberString.substring(0, CUSTOM_NUMBER_LENGTH_2);
        }
        String formatUserId =String.format("%0" + CUSTOM_NUMBER_LENGTH_2 + "d", Long.parseLong(customNumberString));
        String orderNoS = str + formatUserId + timeS  +randomNumber(4);
        return orderNoS;
    }


    /**
     * yyyyMMddHHmmssSSS+4位随机数
     * @return
     */
    public static Long generateByTime(){
        String dateS = DateUtil.format(Calendar.getInstance().getTime(),DEFAULT_TIME_FORMART);
        dateS = dateS + randomNumber(4);
        return Long.valueOf(dateS);
    }


    public static void main(String [] args){
        System.out.println(generateString(1, "SP"));
        System.out.println(generateTypeNum(1));

        System.out.println(generateByTime());

    }

}
