package com.wakedata.common.feign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wakedata.common.core.context.BaseUserInfo;
import com.wakedata.common.core.context.UserInfoContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.Base64;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Feign拦截器
 *
 * @author wangcan
 * @date 2022/1/5 10:39
 */
@Slf4j
public class OpenFeignInterceptor implements RequestInterceptor, HandlerInterceptor{

    /**
     * 用户语言Key
     */
    private static final String LANGUAGE = "Accept-Language";

    /**
     * 上下文信息Key
     */
    public static final String USER_CONTEXT = "User-Context";

    /**
     * 追踪日志开关
     */
    public static final String TRACE_LOG_START_KEY = "trace_log_start_key";

    /**
     * 追踪日志mdc信息透传traceId
     */
    public static final String TRACE_LOG_MDC_ID = "trace_log_mdc_id";

    /**
     * 追踪日志mdc信息透传spanId
     */
    public static final String TRACE_LOG_MDC_SPAN_ID = "trace_log_mdc_span_id";

    /**
     * 追踪日志mdc用户信息
     */
    public static final String TRACE_LOG_MDC_FEIGN_COOKIE = "trace_log_mdc_feign_cookie";

    /**
     * 是否为feign请求
     */
    public static final String IS_FEIGN = "is_feign";


    /**
     * feign请求前置拦截，请求头注入用户上下文
     */
    @Override
    public void apply(RequestTemplate template) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            // 透传language信息
            String language = ((ServletRequestAttributes) requestAttributes).getRequest().getHeader(LANGUAGE);
            if (Objects.nonNull(language)) {
                template.header(LANGUAGE, language);
            }

            // 透传traceId和spanId
            dataSpread(template, (ServletRequestAttributes) requestAttributes);
        }

        // 透传UserInfo
        BaseUserInfo userInfoContext = UserInfoContext.getUser();
        if (Objects.isNull(userInfoContext)) {
            log.warn("feign get user context is null");
            return;
        }
        template.header(USER_CONTEXT, Base64.getEncoder()
                .encodeToString(JSON.toJSONString(userInfoContext, SerializerFeature.WriteClassName).getBytes()));
    }

    /**
     * feign接口访问前置拦截，恢复用户上下文
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response
            , @NonNull Object handler) {
        String sessionUser = request.getHeader(USER_CONTEXT);
        if (Objects.isNull(sessionUser)) {
            log.warn("rpc get session user is null, req uri={}", request.getRequestURI());
            return true;
        }
        UserInfoContext.setUser(JSON.parseObject(Base64.getDecoder().decode(sessionUser), BaseUserInfo.class));
        return Boolean.TRUE;
    }

    /**
     * feign接口访问后置拦截，销毁用户上下文
     */
    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response
            , @NonNull Object handler, Exception ex) {
        UserInfoContext.removeUserInfoContext();
    }


    /**
     * 全链路日志需要透传的traceId和spanId
     *
     * @param template template
     * @param requestAttributes requestAttributes
     */
    private void dataSpread(RequestTemplate template, ServletRequestAttributes requestAttributes) {

        if (Objects.equals(requestAttributes.getRequest().getAttribute(TRACE_LOG_START_KEY),Boolean.TRUE.toString())){
            // 透传feign标识,全链路traceId,spanId和cookie
            template.header(IS_FEIGN,Boolean.TRUE.toString());
            Object trace = requestAttributes.getRequest().getAttribute(TRACE_LOG_MDC_ID);
            if (Objects.nonNull(trace)){
                String tradeId = trace.toString();
                template.header(TRACE_LOG_MDC_ID,tradeId);
            }
            Object span = requestAttributes.getRequest().getAttribute(TRACE_LOG_MDC_SPAN_ID);
            if (Objects.nonNull(span)){
                String spanId = span.toString();
                template.header(TRACE_LOG_MDC_SPAN_ID,spanId);
            }
            String cookie = requestAttributes.getRequest().getHeader("Cookie");
            if (StringUtils.isNotEmpty(cookie)){
                template.header(TRACE_LOG_MDC_FEIGN_COOKIE,cookie);
            }
        }
    }

}
