package com.wakedata.common.spring.hashids;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


/**
 * @author luomeng
 * @Description id转hash
 * @createTime 2022-04-19 10:16:00
 */
@ContextConfiguration(classes = SpringTestApplication.class)
@RunWith(SpringRunner.class)
@WebMvcTest(HashidsController.class)
public class HashidsControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testHashids() throws Exception {
        String param = "{\"id\":\"KEZA1pNvwePqD\",\"idLong\":\"p8qJ6y7VRBKwX45Wmzg9\",\"idInt\":\"Q8VXmovr\",\"idInteger\":\"mRDKaz9jJ\",\"idStr\":\"100\",\"idNoConvert\":100}";
        String timezone = "GMT+09:00";
        mockMvc.perform(MockMvcRequestBuilders.get("/heartbeat/convert.ids").with(request->{
            request.addHeader("Accept-Language","zh_CN");
            request.addHeader("Accept-Timezone",timezone);
            return request;
        }).contentType(MediaType.APPLICATION_JSON).content(param)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Matchers.is(true)))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void testHashidsParam() throws Exception {
        String id = "KEZA1pNvwePqD";
        mockMvc.perform(MockMvcRequestBuilders.get("/heartbeat/convert.test.param").with(request->{
                    return request;
                }).param("id",id).param("hashid","p8qJ6y7VRBKwX45Wmzg9").contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success", Matchers.is(true)))
                .andReturn().getResponse().getContentAsString();
    }

}
