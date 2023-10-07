package com.wakedata.common.core.util;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DateUtil
 * @Description TODO
 * @Date 2022/3/16 11:26
 * @Author zhizhongan
 */
public class DateUtil {

    /**
     * 获取两个日期中的所有日期,并转换为表后缀
     *
     * @param begin 格式:yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
     * @param end   格式:yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
     * @return 格式:MM_dd
     */
    public static Map<String, String> getDetHashList(String begin, String end) {
        Map<String, String> hashList = new HashMap<>(30);
        //yyyy-MM-dd
        String btime = begin.substring(0, 10);
        String etime = end.substring(0, 10);

        //yyyy-MM-dd
        Date bDate = cn.hutool.core.date.DateUtil.parse(btime, DatePattern.NORM_DATE_PATTERN);
        Date eDate = cn.hutool.core.date.DateUtil.parse(etime, DatePattern.NORM_DATE_PATTERN);
        //创建日期范围生成器
        List<DateTime> dateList = cn.hutool.core.date.DateUtil.rangeToList(bDate, eDate, DateField.DAY_OF_MONTH);
        String hash;
        for (DateTime dt : dateList) {
            hash = dt.toString().substring(0, 10);
            hashList.put(hash, hash);
        }
        return hashList;
    }

    /**
     * 获取两个日期中的所有日期,并转换为表后缀
     *
     * @param begin 格式:yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
     * @param end   格式:yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
     * @param format 格式，例如yyyy-MM-dd
     * @param index 切割日期的位置, 如：要切割为yyyy-MM-dd, 就传10, 切割为yyyy-MM, 就传7
     * @param unit unit – 步进单位(hutool的)
     * @return Map<String, String>
     */
    public static Map<String, String> getDetHashList(String begin, String end, String format, DateField unit, Integer index) {
        Map<String, String> hashList = new HashMap<>(50);
        //yyyy-MM-dd
        String btime = begin.substring(0, index);
        String etime = end.substring(0, index);

        //yyyy-MM-dd
        Date bDate = cn.hutool.core.date.DateUtil.parse(btime, format);
        Date eDate = cn.hutool.core.date.DateUtil.parse(etime, format);
        //创建日期范围生成器
        List<DateTime> dateList = cn.hutool.core.date.DateUtil.rangeToList(bDate, eDate, unit);
        String hash;
        for (DateTime dt : dateList) {
            hash = dt.toString().substring(0, index);
            hashList.put(hash, hash);
        }
        return hashList;
    }
}
