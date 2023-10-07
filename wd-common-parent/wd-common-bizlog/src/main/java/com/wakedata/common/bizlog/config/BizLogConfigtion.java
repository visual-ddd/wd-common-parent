package com.wakedata.common.bizlog.config;

import com.mzt.logapi.service.ILogRecordService;
import com.mzt.logapi.starter.configuration.LogRecordProperties;
import com.mzt.logapi.starter.diff.IDiffItemsToLogContentService;
import com.wakedata.common.bizlog.core.BizDiffItemsToLogContentService;
import com.wakedata.common.bizlog.core.BizLogRecordService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;

/**
 * BizLogConfigtion
 *
 * @author focus
 * @date 2022/10/9
 **/
@Configuration
@EnableConfigurationProperties({LogRecordProperties.class,BizLogProperties.class})
public class BizLogConfigtion {

    @Bean
    @Primary
    public IDiffItemsToLogContentService
                bizDiffItemsToLogContentService(LogRecordProperties logRecordProperties) {
        return new BizDiffItemsToLogContentService(logRecordProperties);
    }

    @Bean
    @Primary
    public ILogRecordService bizLogRecordService(){
        return new BizLogRecordService();
    }

    @Bean
    public BizLogConfig bizLogConfig(BizLogProperties auditLogProperties) {
        return new BizLogConfig(auditLogProperties);
    }
}
