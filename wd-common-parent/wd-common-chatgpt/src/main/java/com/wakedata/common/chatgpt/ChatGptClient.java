package com.wakedata.common.chatgpt;

import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import com.unfbx.chatgpt.interceptor.OpenAiResponseInterceptor;
import com.wakedata.common.chatgpt.config.ChatGptProperties;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
/**
 * @Author focus
 * @Date 2023/3/30
 */
public class ChatGptClient {

    private static volatile OpenAiClient instance;
    private static ChatGptProperties properties;

    public ChatGptClient(ChatGptProperties properties) {
        this.properties = properties;
    }

    public static OpenAiClient getInstance() {
        if (instance == null) {
            synchronized (ChatGptClient.class) {
                if (instance == null) {
                    instance = buildInstance();
                }
            }
        }
        return instance;
    }

    public static OpenAiClient buildInstance() {
        OkHttpClient okHttpClient = buildOkHttpClient(properties);

        return OpenAiClient.builder()
                .apiKey(Arrays.asList(properties.getApiKey()))
                .okHttpClient(okHttpClient)
                .apiHost(properties.getProxyApiHost())
                .build();
    }

    private static OkHttpClient buildOkHttpClient(ChatGptProperties properties) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new OpenAiResponseInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }
}
