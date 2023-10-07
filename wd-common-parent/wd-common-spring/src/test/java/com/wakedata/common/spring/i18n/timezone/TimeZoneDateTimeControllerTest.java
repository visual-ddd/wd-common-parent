package com.wakedata.common.spring.i18n.timezone;

import com.wakedata.common.spring.SpringTestApplication;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author luomeng
 * @Description 时区时间转换单元测试
 * @createTime 2022-04-19 10:16:00
 */
@ContextConfiguration(classes = SpringTestApplication.class)
@RunWith(SpringRunner.class)
@WebMvcTest(TimeZoneDateTimeController.class)
public class TimeZoneDateTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testGetTimeI18n() throws Exception {
        String param = "{\n" +
                "    \"date2\": \"2022-04-18 01:21:19\",\n" +
                "    \"localDate2\": \"2022-04-17\",\n" +
                "    \"localDateTime2\": \"2022-04-18 01:21:19\"\n" +
                "}";
        String timezone = "GMT+09:00";
        mockMvc.perform(get("/heartbeat/get.time.i18n").with(request->{
            request.addHeader("Accept-Language","zh_CN");
            request.addHeader("Accept-Timezone",timezone);
            return request;
        }).contentType(MediaType.APPLICATION_JSON).content(param)).andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.timeZoneId",Matchers.is(timezone)))
                .andReturn().getResponse().getContentAsString();
    }
}
