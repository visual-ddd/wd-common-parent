package com.wakedata.common.spring.i18n.timezone;

import com.wakedata.common.spring.i18n.timezone.annotation.TimeZoneDateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author luomeng
 * @Description 时区时间测试DTO
 * @createTime 2022-04-18 19:40:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeZoneDateTimeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date date1;
    @TimeZoneDateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date2;
    @TimeZoneDateTimeFormat(utcTime = true)
    private Date date3Utc;

    private LocalDate localDate1;
    @TimeZoneDateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate localDate2;
    @TimeZoneDateTimeFormat(utcTime = true)
    private LocalDate localDate3Utc;

    private LocalDateTime localDateTime1;
    @TimeZoneDateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime localDateTime2;
    @TimeZoneDateTimeFormat(utcTime = true)
    private LocalDateTime localDateTime3Utc;

    private String timeZoneId;


}
