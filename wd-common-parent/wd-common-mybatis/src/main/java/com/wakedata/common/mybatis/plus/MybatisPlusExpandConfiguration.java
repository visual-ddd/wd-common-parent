package com.wakedata.common.mybatis.plus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.wakedata.common.mybatis.plus.handler.DefaultDbFieldHandler;
import com.wakedata.common.mybatis.plus.injector.CustomSqlInject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author luomeng
 * @Description mybatisplus 拓展实现，注入自定义的方法
 * @createTime 2021-12-09 14:43:00
 */
@Configuration
public class MybatisPlusExpandConfiguration {

    /**
     * 自定义方法扩展注入器
     * @return ISqlInjector CustomSqlInject
     */
    @Bean
    @ConditionalOnMissingBean(ISqlInjector.class)
    public ISqlInjector sqlInjector(){
        return new CustomSqlInject();
    }

    /**
     * 自动填充参数类
     */
    @Bean
    public MetaObjectHandler defaultMetaObjectHandler(){
        return new DefaultDbFieldHandler();
    }

    /**
     * 阻止恶意的全表更新删除
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }
}
