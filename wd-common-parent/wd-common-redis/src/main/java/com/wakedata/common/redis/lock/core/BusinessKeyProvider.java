package com.wakedata.common.redis.lock.core;

import com.wakedata.common.redis.lock.annotation.LockKey;
import com.wakedata.common.redis.lock.annotation.RedisLock;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户定义业务key解析
 *
 * @author hhf
 * @date 2020/12/28
 */
public class BusinessKeyProvider {

    private ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    private ExpressionParser parser = new SpelExpressionParser();

    private static final String EXPRESSION_REGEX = "[a-zA-Z0-9_\\.\\*#\\{\\}]*";

    public String getKeyName(JoinPoint joinPoint, RedisLock redisLock) {
        List<String> keyList = new ArrayList<>();
        Method method = getMethod(joinPoint);
        List<String> definitionKeys = getSpelDefinitionKey(redisLock.keys(), method, joinPoint.getArgs());
        keyList.addAll(definitionKeys);
        List<String> parameterKeys = getParameterKey(method.getParameters(), joinPoint.getArgs());
        keyList.addAll(parameterKeys);
        return StringUtils.collectionToDelimitedString(keyList, "", "-", "");
    }

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.getDeclaringClass().isInterface()) {
            try {
                method = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(),
                        method.getParameterTypes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return method;
    }

    private List<String> getSpelDefinitionKey(String[] definitionKeys, Method method, Object[] parameterValues) {
        List<String> definitionKeyList = new ArrayList<>();
        for (String definitionKey : definitionKeys) {
            if (!ObjectUtils.isEmpty(definitionKey)) {
                if (validateExpression(definitionKey)) {
                    EvaluationContext context = new MethodBasedEvaluationContext(null, method, parameterValues, nameDiscoverer);
                    Object objKey = parser.parseExpression(definitionKey).getValue(context);
                    definitionKeyList.add(ObjectUtils.nullSafeToString(objKey));
                }
            }
        }
        return definitionKeyList;
    }

    private List<String> getParameterKey(Parameter[] parameters, Object[] parameterValues) {
        List<String> parameterKey = new ArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getAnnotation(LockKey.class) != null) {
                LockKey keyAnnotation = parameters[i].getAnnotation(LockKey.class);
                if (keyAnnotation.value().isEmpty()) {
                    Object parameterValue = parameterValues[i];
                    parameterKey.add(ObjectUtils.nullSafeToString(parameterValue));
                    continue;
                }
                if (validateExpression(keyAnnotation.value())) {
                    StandardEvaluationContext context = new StandardEvaluationContext(parameterValues[i]);
                    Object key = parser.parseExpression(keyAnnotation.value()).getValue(context);
                    parameterKey.add(ObjectUtils.nullSafeToString(key));
                }
            }
        }
        return parameterKey;
    }

    private boolean validateExpression(String expression) {
        Pattern pattern = Pattern.compile(EXPRESSION_REGEX);
        Matcher matcher = pattern.matcher(expression);
        return matcher.matches();
    }

//    public static void main(String[] args) {
//        System.out.println(validateExpression("asd123"));
//        System.out.println(validateExpression("#{user}"));
//        System.out.println(validateExpression("#{user.username}"));
//        System.out.println(validateExpression("#{user.username.name}"));
//        System.out.println(validateExpression("#{user.username}+asd"));
//    }
}
