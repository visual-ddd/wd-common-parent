package com.wakedata.common.chatgpt;

import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import com.wakedata.common.chatgpt.config.ChatGptProperties;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @Author focus
 * @Date 2023/3/30
 */
public class ChatGptStreamClient  {

    private static volatile OpenAiStreamClient instance;
    private static ChatGptProperties properties = new ChatGptProperties();

    public ChatGptStreamClient(ChatGptProperties properties) {
        this.properties = properties;
    }

    public static OpenAiStreamClient getInstance() {
        if (instance == null) {
            synchronized (ChatGptStreamClient.class) {
                if (instance == null) {
                    instance = buildInstance();
                }
            }
        }
        return instance;
    }


    public static  OpenAiStreamClient buildInstance(){
        return new OpenAiStreamClient.Builder()
                .apiKey(Arrays.asList(properties.getApiKey()))
                .okHttpClient(buildOkHttpClient())
                .apiHost(properties.getProxyApiHost())
                .build();
    }

    private static OkHttpClient buildOkHttpClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }
}

