package com.wakedata.common.chatgpt.config;

import com.wakedata.common.chatgpt.ChatGptClient;
import com.wakedata.common.chatgpt.ChatGptStreamClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * BizLogConfigtion
 *
 * @author focus
 **/
@Configuration
@EnableConfigurationProperties({ChatGptProperties.class})
public class ChatGptConfigtion {


    public ChatGptStreamClient chatGptStreamClient(ChatGptProperties properties){
        return new ChatGptStreamClient(properties);
    }

    public ChatGptClient chatGptClient(ChatGptProperties properties){
        return new ChatGptClient(properties);
    }


}
