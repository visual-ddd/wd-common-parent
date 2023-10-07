package com.wakedata.common.redis.delayed;

import com.wakedata.common.core.exception.BizException;
import com.wakedata.common.redis.delayed.annotation.DelayMsgSubscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaskListener {

    @DelayMsgSubscribe
    public void onMessage1(TaskBody taskBodyDTO) {
        log.info("onMessage1...." + taskBodyDTO.getBody() + "===" + taskBodyDTO.getName());
        throw new BizException("onMessage throw exception");
    }

    @DelayMsgSubscribe
    public void onMessage2(TaskBody taskBodyDTO) {
        log.info("onMessage2...." + taskBodyDTO.getBody() + "===" + taskBodyDTO.getName());
    }
}