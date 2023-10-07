package com.wakedata.common.spring.i18n;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.context.i18n.TimeZoneAwareLocaleContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author: hhf
 * @date: 2021/12/7
 **/
public class Test {

    public static void main(String[] args) {

        Date date = new Date();
        LocalDateTime localDateTime = LocalDateTime.now();

        System.out.println("date = " + date);
        System.out.println("localDateTime = " + localDateTime);

        String[] availableIDs = TimeZone.getAvailableIDs();
        System.out.println("JSONUtil.toJsonPrettyStr(availableIDs) = " + JSONUtil.toJsonPrettyStr(availableIDs));

//        TimeZoneAwareLocaleContext

        TimeZone aDefault = TimeZone.getDefault();
        System.out.println("aDefault = " + aDefault);
        System.out.println("aDefault.getRawOffset(); = " + aDefault.getRawOffset());
        LocalDateTime localDateTime1 = LocalDateTimeUtil.ofUTC(DateUtil.toInstant(localDateTime));
        System.out.println("localDateTime1 = " + localDateTime1);
        System.out.println("LocalDateTimeUtil.toEpochMilli(localDateTime) = " + LocalDateTimeUtil.toEpochMilli(localDateTime));
        System.out.println("LocalDateTimeUtil.toEpochMilli(localDateTime1) = " + LocalDateTimeUtil.toEpochMilli(localDateTime1));
        ZoneId defaultZoneId = ZoneId.of("Asia/Shanghai");
        ZoneId zoneId = ZoneId.of("Asia/Tokyo");
        LocalDateTime localDateTime2 = localDateTime.atZone(defaultZoneId).withZoneSameInstant(zoneId).toLocalDateTime();
        System.out.println("localDateTime2 = " + localDateTime2);
        ZoneId zoneId1 = ZoneId.of("UTC");
        LocalDateTime localDateTime3 = localDateTime.atZone(defaultZoneId).withZoneSameInstant(zoneId1).toLocalDateTime();
        System.out.println("localDateTime3 = " + localDateTime3);

        LocalDateTime localDateTime4 = localDateTime2.atZone(zoneId).withZoneSameInstant(defaultZoneId).toLocalDateTime();
        System.out.println("localDateTime4 = " + localDateTime4);
        LocalDateTime localDateTime5 = localDateTime3.atZone(zoneId1).withZoneSameInstant(defaultZoneId).toLocalDateTime();
        System.out.println("localDateTime5 = " + localDateTime5);

        ZoneId zoneId2 = aDefault.toZoneId();
        System.out.println("zoneId2 = " + zoneId2);
    }

}
