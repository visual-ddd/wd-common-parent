package com.wakedata.common.mybatis.plugin;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;

import java.util.Properties;

/**
 * mybaits sql 拦截
 *
 * @author hhf
 * @date 2021/1/29
 */
@Intercepts({@Signature(type = org.apache.ibatis.executor.Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
@Slf4j
public class MyBatisSqlUpdateSqlDebugPlugin implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        log.info(boundSql.getSql() + "\r\n" + boundSql.getParameterMappings().toString());
        Object obj = invocation.proceed();
        return obj;
    }

    @Override
    public Object plugin(Object arg0) {
        return Plugin.wrap(arg0, this);
    }

    @Override
    public void setProperties(Properties arg0) {

    }

}