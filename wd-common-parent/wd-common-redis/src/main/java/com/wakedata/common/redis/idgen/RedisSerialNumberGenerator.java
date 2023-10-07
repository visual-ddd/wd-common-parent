package com.wakedata.common.redis.idgen;


import com.wakedata.common.core.constants.CommonConstant;
import com.wakedata.common.redis.util.RedisUtil;

/**
 *
 * 序列号生成器
 * @author pengxu
 * @date 2018/8/22
 */
public class RedisSerialNumberGenerator {

    private static final int CUSTOM_NUMBER_LENGTH = 3;

    private static final String REDIS_SERIAL_GENERATOR_KEY = "redis_serial_number_generator";

    /**
     * 随机序列
     * 当前时间戳后9位+4位随机数+自定义分隔数
     * @param customNumber 自定义数 最大支持3位
     * @return
     */
    public static Long generateTypeNum(Integer customNumber) {
        // 去掉首位，缩短使用时间，但是降低位数
        String timeS = String.valueOf(System.currentTimeMillis()).substring(1);
        String customNumberString = String.valueOf(customNumber);
        if(customNumberString.length() > CUSTOM_NUMBER_LENGTH){
            customNumberString = customNumberString.substring(0, CUSTOM_NUMBER_LENGTH);
        }
        String formatType =String.format("%0" + CUSTOM_NUMBER_LENGTH + "d", Long.parseLong(customNumberString));
        String orderNoS = timeS + autoIncrNum(customNumber, 4) + formatType;
        return Long.parseLong(orderNoS);
    }

    private static Long autoIncrNum(Integer customNumber, int length) {
        String key = REDIS_SERIAL_GENERATOR_KEY + CommonConstant.COLON + customNumber;
        return RedisUtil.getInstance().incr(key, 1) % (long) (Math.pow(10, length));
    }
}
