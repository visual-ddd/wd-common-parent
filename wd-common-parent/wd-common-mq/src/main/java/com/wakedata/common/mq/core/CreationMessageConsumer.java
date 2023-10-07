package com.wakedata.common.mq.core;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.wakedata.common.core.constants.IndustryEnum;
import com.wakedata.common.mq.annotation.MessageSubscribe;
import com.wakedata.common.mq.common.MqConfigParamConvert;
import com.wakedata.common.mq.exception.AlreadyExistConsumerException;
import com.wakedata.common.mq.exception.ConnectException;
import com.wakedata.common.mq.model.BusinessParamWrapper;
import com.wakedata.common.mq.model.MessageAnnotationApplyTarget;
import com.wakedata.common.mq.model.MqConfigParam;
import com.wakedata.common.mq.model.consumer.MakeSubscribeBody;
import com.wakedata.common.mq.model.consumer.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 创建消息消费者
 *
 * @author chenshaopeng
 * @date 2021/12/8
 */
@Slf4j
@Import(MqConfigurationSelector.class)
public class CreationMessageConsumer implements ApplicationRunner {

    private static final String INDUSTRY_KEY = "industryKey";

    @Resource
    private MqConfigurationSelector configurationSelector;

    /**
     * 初始化方法
     */
    @Override
    public void run(ApplicationArguments args) {
        annotationApplyTargetSubscribeProcess();
    }

    /**
     * 注解订阅处理
     */
    private void annotationApplyTargetSubscribeProcess() {
        for (MessageAnnotationApplyTarget target : MessageCenterAnnotationBeanPostProcessor
                .getAnnotationApplyTargetList()) {
            MakeSubscribeBody subscribeBody = buildSubscribeInfo(target.getAnnotation());
            if (Objects.isNull(subscribeBody)) {
                continue;
            }
            createSubscribe(subscribeBody, message -> createBusinessCmd(target.getBean()
                    , target.getMethod(), BusinessParamWrapper.builder().message(message).build())
            );
        }
    }

    /**
     * 构建订阅信息
     * 使用’配置选择器‘对多种配置优先级作处理
     */
    private MakeSubscribeBody buildSubscribeInfo(MessageSubscribe annotation) {
        try {
            MqConfigParam configParam = configurationSelector.getMqConfigParam(annotation);
            configParam.checkRequisiteParam();
            return MqConfigParamConvert.convertToMakeSubscribeBody(configParam);
        } catch (Exception e) {
            log.error("Failed to build MQ subscription information!", e);
            return null;
        }
    }

    /**
     * 创建消费者订阅
     */
    public void createSubscribe(@NotNull MakeSubscribeBody subscribeBody, Consumer<Message> msgConsumer) {
        String protocolName = subscribeBody.getProtocol().getName();
        try {
            subscribeBody.getProtocol().consumer().addSubscribe(subscribeBody, msgConsumer);

            log.info("Create a subscriber; protocol: {}, bootstrapServers: {}, topic: {}, tag: {}, queueId: {}"
                    , protocolName, subscribeBody.getBootstrapServers(), subscribeBody.getTopic()
                    , subscribeBody.getTag(), subscribeBody.getQueueId());
        } catch (AlreadyExistConsumerException e) {
            log.warn("Repeat create subscriber! protocol: {}, bootstrapServers: {}, topic: {}"
                    , protocolName, subscribeBody.getBootstrapServers(), subscribeBody.getTopic());
        } catch (ConnectException e) {
            log.error("Create subscriber failed! Please check your connection configuration. "
                            + "protocol: {}, bootstrapServers: {}, topic: {}, error: {}"
                    , protocolName, subscribeBody.getBootstrapServers(), subscribeBody.getTopic(), e.getMessage());
        } catch (Exception e) {
            log.error("Create subscriber failed! bootstrapServers: " + subscribeBody.getBootstrapServers(), e);
        }
    }

    /**
     * 创建业务指令
     * 执行消息回调方法，将message字符串转为回调方法入参的类型
     * <p>
     * 注意：回调方法只能存在一个入参参数，否则将出现异常
     */
    protected <W extends BusinessParamWrapper> void createBusinessCmd(Object bean, Method method, W wrapper) {
        log.debug("MQ new messages, message: {}, service: {}", wrapper.getMessage().toString(), method.getName());
        try {
            invokeBusinessMethod(bean, method, wrapper);
        } catch (Exception e) {
            log.error("MQ message consumption failed!, topic: " + wrapper.getMessage()
                    .getTopic() + ", method: " + bean.getClass().getName() + "." + method.getName(), e);
        }
    }

    /**
     * 创建业务指令
     * 执行消息回调方法，将message字符串转为回调方法入参的类型
     * <p>
     * 注意：回调方法只能存在一个入参参数，否则将出现异常
     */
    protected <W extends BusinessParamWrapper> void createBusinessCmd(Object bean, Method method, W wrapper, MessageEventListener listener, IndustryEnum industry) {
        log.debug("MQ new messages, message: {}, service: {}", wrapper.getMessage().toString(), method.getName());
        if (!IndustryEnum.DEFAULT.equals(industry)) {
            String msgIndustryKey = getMsgIndustryKey(wrapper.getMessage().getValue());
            if (StrUtil.isNotEmpty(msgIndustryKey) && !StrUtil.equals(industry.getIndustryKey(), msgIndustryKey)) {
                //消息中的industryKey和接收的industry不一致，不调用业务方法
                if (log.isDebugEnabled()) {
                    log.debug("filter msg, expect industry: {}, actual industry: {}", industry.getIndustryKey(), msgIndustryKey);
                }
                return;
            }
        }
        try {
            invokeBusinessMethod(bean, method, wrapper);
        } catch (Exception e) {
            log.error("MQ message consumption failed! topic: " + wrapper.getMessage()
                    .getTopic() + ", method: " + bean.getClass().getName() + "." + method.getName(), e);
            listener.onError(bean, method, wrapper);
        }
    }

    /**
     * 调用业务方法
     */
    protected <W extends BusinessParamWrapper> void invokeBusinessMethod(Object bean, Method method, W wrapper)
            throws Exception {
        method.setAccessible(true);
        method.invoke(bean, JSON.parseObject(wrapper.getMessage().getValue(), method.getParameterTypes()[0]));
    }

    private String getMsgIndustryKey(String msg) {
        return JSON.parseObject(msg).getString(INDUSTRY_KEY);
    }

}
