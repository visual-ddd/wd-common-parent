package com.wakedata.common.redis.delayed.core;

import com.wakedata.common.redis.delayed.model.SubscriberAnnotationApplyTarget;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: hhf
 * @date: 2022/4/13
 **/
@Slf4j
public class InvokeRunner<T> implements Runnable {

    private static final int MAX_RETRY = 3;
    private T param;
    private SubscriberAnnotationApplyTarget target;

    public InvokeRunner(T param, SubscriberAnnotationApplyTarget target) {
        this.param = param;
        this.target = target;
    }

    @Override
    public void run() {
        int maxRetry = MAX_RETRY;
        do {
            try {
                target.getMethod().setAccessible(true);
                target.getMethod().invoke(target.getBean(), param);
                break;
            } catch (Exception e) {
                log.error("delay msg consumption failed", e);
                maxRetry--;
            }
        } while (maxRetry > 0);
    }
}
