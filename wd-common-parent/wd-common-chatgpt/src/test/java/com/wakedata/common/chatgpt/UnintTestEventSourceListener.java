package com.wakedata.common.chatgpt;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.util.Objects;

/**
 * 单元测试相关
 * @Author focus
 * @Date 2023/3/30
 */
@Slf4j
public class UnintTestEventSourceListener extends EventSourceListener {

    StringBuilder result = new StringBuilder();
    @Override
    public void onOpen(EventSource eventSource, Response response) {
        log.info("OpenAI建立sse连接...");
    }

    @Override
    public void onEvent(EventSource eventSource, String id, String type, String data) {
        if (data.equals("[DONE]")) {
            log.info(result.toString());
            log.info("OpenAI返回数据结束了");
            return;
        }

        JSONObject json = JSONUtil.parseObj(data);
        json.getJSONArray("choices").stream().forEach(e ->{
            result.append(JSONUtil.parseObj(e).get("text"));
            return;
        });
    }

    @Override
    public void onClosed(EventSource eventSource) {
        log.info("OpenAI关闭sse连接...");
    }

    @SneakyThrows
    @Override
    public void onFailure(EventSource eventSource, Throwable t, Response response) {
        if(Objects.isNull(response)){
            log.error("OpenAI  sse连接异常:{}", t);
            eventSource.cancel();
            return;
        }
        ResponseBody body = response.body();
        if (Objects.nonNull(body)) {
            log.error("OpenAI  sse连接异常data：{}，异常：{}", body.string(), t);
        } else {
            log.error("OpenAI  sse连接异常data：{}，异常：{}", response, t);
        }
        eventSource.cancel();
    }
}
