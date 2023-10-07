package com.wakedata.common.spring.i18n.timezone;

import cn.hutool.json.JSONUtil;
import com.wakedata.common.core.dto.ResultDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author luomeng
 * @Description 测试时区时间转换
 * @createTime 2022-04-18 19:34:00
 */
@RestController
@RequestMapping("/heartbeat")
public class TimeZoneDateTimeController {

    /**
     * 时间按时区及指定时间序列化
     * @param request
     * @param timeZoneDateTimeDTO
     * @return
     */
    @GetMapping("/get.time.i18n")
    public ResultDTO<TimeZoneDateTimeDTO> getTimeI18n(HttpServletRequest request,@RequestBody TimeZoneDateTimeDTO timeZoneDateTimeDTO){
        System.out.println("JSONUtil.toJsonPrettyStr(testTimeDTO) = " + JSONUtil.toJsonPrettyStr(timeZoneDateTimeDTO));

        TimeZoneDateTimeDTO timeDTO = new TimeZoneDateTimeDTO();
        Date date = new Date();
        System.out.println("date = " + date);
        timeDTO.setDate1(date);
        timeDTO.setDate2(date);
        timeDTO.setDate3Utc(date);

        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate localDate = localDateTime.toLocalDate();
        System.out.println("localDate = " + localDate);
        timeDTO.setLocalDate1(localDate);
        timeDTO.setLocalDate2(localDate);
        timeDTO.setLocalDate3Utc(localDate);

        System.out.println("localDateTime = " + localDateTime);
        timeDTO.setLocalDateTime1(localDateTime);
        timeDTO.setLocalDateTime2(localDateTime);
        timeDTO.setLocalDateTime3Utc(localDateTime);

        Locale locale = RequestContextUtils.getLocale(request);
        System.out.println("locale = " + locale);

        TimeZone timeZone = RequestContextUtils.getTimeZone(request);
        System.out.println("timeZone = " + timeZone);
        System.out.println("timeDTO = " + timeDTO);
        timeDTO.setTimeZoneId(timeZone.getID());
        return ResultDTO.success(timeDTO);

    }






}
