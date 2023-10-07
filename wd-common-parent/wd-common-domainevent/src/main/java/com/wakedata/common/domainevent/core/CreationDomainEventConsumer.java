package com.wakedata.common.domainevent.core;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wakedata.common.core.GlobalApplicationContext;
import com.wakedata.common.core.context.UserInfoContext;
import com.wakedata.common.domainevent.config.DomainEventConfig;
import com.wakedata.common.domainevent.model.BaseDomainEvent;
import com.wakedata.common.domainevent.model.DomainAnnotationApplyTarget;
import com.wakedata.common.domainevent.model.DomainBusinessParamWrapper;
import com.wakedata.common.domainevent.model.MessageBodyWrapper;
import com.wakedata.common.domainevent.retry.event.RetryEventPublisher;
import com.wakedata.common.domainevent.retry.util.AopTargetUtils;
import com.wakedata.common.mq.common.MqConfigParamConvert;
import com.wakedata.common.mq.common.MqConfigurationBuilder;
import com.wakedata.common.mq.core.CreationMessageConsumer;
import com.wakedata.common.mq.core.SpringElExpressionResolve;
import com.wakedata.common.mq.exception.MessageErrorException;
import com.wakedata.common.mq.model.BusinessParamWrapper;
import com.wakedata.common.mq.model.MqConfigParam;
import com.wakedata.common.mq.model.consumer.MakeSubscribeBody;
import com.wakedata.common.mq.model.consumer.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 创建领域事件消费者
 *
 * @author chenshaopeng
 * @date 2021/12/16
 */
@Slf4j
@Import({DomainEventLocalConsumeSupport.class})
public class CreationDomainEventConsumer extends CreationMessageConsumer implements ApplicationRunner {

    private static final String GENERATE_QUEUE_TEMPLATE = "QUEUE_%s_%s_%s_%s";

    @Resource
    private DomainEventLocalConsumeSupport domainEventLocalConsumeSupport;

    @Resource
    private DomainEventConfig configuration;

    @Resource
    private SpringElExpressionResolve expressionResolve;

    @Value("${env}")
    private String env;

    /**
     * 初始方法，启动后调用
     */
    @Override
    public void run(ApplicationArguments args) {
        annotationApplyTargetSubscribeProcess();
    }

    /**
     * 注解订阅处理
     */
    private void annotationApplyTargetSubscribeProcess() {
        log.info("开始订阅，订阅列表={}", JSONObject.toJSONString(DomainEventAnnotationBeanPostProcessor
                .getAnnotationApplyTargetList()));
        for (DomainAnnotationApplyTarget applyTarget : DomainEventAnnotationBeanPostProcessor
                .getAnnotationApplyTargetList()) {
            if (applyTarget.getAnnotation().localConsume()) {
                domainEventLocalConsumeSupport.createLocalSubscribe(applyTarget);
                continue;
            }
            Class<?> parameterClass = applyTarget.getMethod().getParameterTypes()[0];
            MakeSubscribeBody subscribeBody = buildSubscribeInfo(applyTarget, parameterClass);
            if (Objects.isNull(subscribeBody)) {
                continue;
            }
            createSubscribe(subscribeBody, createMsgConsumer(applyTarget, parameterClass));
        }
    }

    /**
     * 构建订阅信息
     * 使用‘多配置选择器’，未定义的配置则使用默认配置
     * topic优先级：yml定义 < 注解定义
     */
    private MakeSubscribeBody buildSubscribeInfo(DomainAnnotationApplyTarget target, Class<?> classes) {
        MqConfigParam configParam = MqConfigParam.builder().tag(getEventCodeOrError(classes))
                .topic(ObjectUtil.defaultIfBlank(expressionResolve.resolveString(target.getAnnotation().topic())
                        , configuration.getDomainEventTopic())
                ).queueId(generateQueueId(target.getBean().getClass(), target.getMethod())).build();

        configParam = MqConfigurationBuilder.build(configuration.getDomainEventProtocol(), configParam);
        try {
            configParam.checkRequisiteParam();
        } catch (Exception e) {
            log.error("Create domain event subscriber failed!", e);
            return null;
        }
        return MqConfigParamConvert.convertToMakeSubscribeBody(configParam);
    }

    /**
     * 创建消息消费者
     */
    private Consumer<Message> createMsgConsumer(DomainAnnotationApplyTarget target, Class<?> classes) {
        String eventCode = getEventCodeOrError(classes);
        return message -> super.createBusinessCmd(target.getBean(), target.getMethod()
                , DomainBusinessParamWrapper.builder().message(message).eventCode(eventCode).build(), (bean, method, wrapper) -> {
                    //消息消费失败后记录
                    String targetClass;
                    try {
                        targetClass = AopTargetUtils.getTarget(target.getBean()).getClass().getName();
                    } catch (Exception e) {
                        log.error("get class name occurs error:", e);
                        throw new RuntimeException(e.getMessage());
                    }
                    String targetMethod = target.getMethod().getName();
                    String targetMethodParamClass = target.getMethod().getParameterTypes()[0].getName();
                    RetryEventPublisher.send(targetClass, targetMethod, targetMethodParamClass, wrapper.getMessage().getValue());
                }, target.getAnnotation().industry());
    }

    /**
     * 生成queueId
     * <p>
     * 以 环境名+ 应用名称+类名+方法名生成的MD5来定义queueId
     */
    private String generateQueueId(Class<?> classes, Method method) {
        return (String.format(GENERATE_QUEUE_TEMPLATE, GlobalApplicationContext.getApplicationContext().getId(), env
                , StrUtil.subBefore(ClassUtil.getShortClassName(classes.getName()), "$", false)
                , method.getName())).replaceAll("\\.", "\\-");
    }

    /**
     * 获取eventCode
     */
    private String getEventCodeOrError(Class<?> eventClasses) {
        return ClassUtil.invoke(eventClasses.getName(), "getEventCode", new Object[]{});
    }

    /**
     * 恢复用户信息上下文
     */
    private void recoveryDomainEventUserInfoContext(Object domainEvent) {
        UserInfoContext.setUser(((BaseDomainEvent) domainEvent).getUserInfoContext());
    }

    /**
     * 销毁用户信息上下文
     */
    private void destroyDomainEventUserInfoContext() {
        UserInfoContext.removeUserInfoContext();
    }

    /**
     * 调用业务方法
     */
    @Override
    public void invokeBusinessMethod(Object bean, Method method, BusinessParamWrapper wrapper) throws Exception {
        log.debug("New MQ message was received, body: {}", wrapper.getMessage().toString());

        MessageBodyWrapper messageBody = JSON.parseObject(wrapper.getMessage().getValue(), MessageBodyWrapper.class);

        if (Objects.isNull(messageBody)) {
            throw new MessageErrorException("Domain event format error! message: " + wrapper.getMessage().toString());
        }
        // 多个领域事件同topic，按照eventCode进行消息过滤
        if (Objects.isNull(messageBody.getEventCode())
                || !messageBody.getEventCode().equals(((DomainBusinessParamWrapper) wrapper).getEventCode())) {
            return;
        }
        Object domainEvent = JSON.parseObject(messageBody.getInfoData(), method.getParameterTypes()[0]);
        try {
            this.recoveryDomainEventUserInfoContext(domainEvent);
            method.invoke(bean, domainEvent);
        } finally {
            this.destroyDomainEventUserInfoContext();
        }
    }

}
