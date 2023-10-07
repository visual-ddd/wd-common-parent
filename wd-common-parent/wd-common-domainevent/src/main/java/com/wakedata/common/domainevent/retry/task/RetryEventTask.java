package com.wakedata.common.domainevent.retry.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.wakedata.common.core.GlobalApplicationContext;
import com.wakedata.common.core.context.BaseUserInfo;
import com.wakedata.common.core.context.UserInfoContext;
import com.wakedata.common.domainevent.model.BaseDomainEvent;
import com.wakedata.common.domainevent.retry.dao.RetryEventDAO;
import com.wakedata.common.domainevent.retry.module.RetryEventDO;
import com.wakedata.common.domainevent.retry.module.RetryTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * 消费失败事件重试任务
 *
 * @author hhf
 * @date 2022/6/13
 **/
@Slf4j
@Configuration
@EnableScheduling
public class RetryEventTask {
    @Resource
    DataSource dataSource;

    @Resource
    private RetryEventDAO retryEventDAO;
    private static final int PAGE_SIZE = 10;

    /**
     * 系统初始化时, 重试所有重试类型为重启时重试的事件
     */
    @Async("domainEventTaskExecutor")
    @PostConstruct
    public void init() {
        doScanAndSchedule(RetryTypeEnum.JVM_RESTART);
    }

    /**
     * 每30秒扫描一次需要重试的事件
     */
    @Scheduled(fixedRate = 30000)
    public void scheduledScan() {
        doScanAndSchedule(RetryTypeEnum.SECHEDULE);
    }

    /**
     * 扫描并重试失败的事件
     *
     * @param retryTypeEnum
     */
    public void doScanAndSchedule(RetryTypeEnum retryTypeEnum) {
        Connection conn = null;
        Boolean connAutoCommit = null;
        PreparedStatement preparedStatement = null;

        try {
            //1.获取数据库锁
            conn = dataSource.getConnection();
            connAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            preparedStatement = conn.prepareStatement("select * from domain_event_lock where lock_name = 'schedule_lock' for update");
            preparedStatement.execute();

            //2.scan event
            int pageNo = 1;
            List<RetryEventDO> list;
            do {
                list = retryEventDAO.selectEventList(pageNo, PAGE_SIZE, retryTypeEnum.getValue());
                //3. 反射调用
                list.forEach(this::invokeBusinessMethod);
                pageNo++;
            } while (CollUtil.isNotEmpty(list));

        } catch (Exception e) {
            log.error("retry task occurs error:", e);
        } finally {
            if (conn != null) {
                try {
                    conn.commit();
                } catch (SQLException e) {
                    log.error("err,", e);
                }
                try {
                    conn.setAutoCommit(connAutoCommit);
                } catch (SQLException e) {
                    log.error("err,", e);
                }
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error("err,", e);
                }
            }
            if (null != preparedStatement) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    log.error("err,", e);
                }
            }
        }
    }

    private void invokeBusinessMethod(RetryEventDO retryEvent) {
        BaseUserInfo userInfo = null;
        try {
            Class targetClazz = Class.forName(retryEvent.getTargetInfo().getClassName());
            Object targetBean = GlobalApplicationContext.getBean(targetClazz);
            Class paramClazz = Class.forName(retryEvent.getTargetInfo().getParamClassName());
            BaseDomainEvent param = (BaseDomainEvent) JSON.parseObject(retryEvent.getEventInfo(), paramClazz);
            //使用alibaba JSON.parseObject存在问题: 序列化后中的JSONString中@type前有属性值，反序列化后，@type前的属性值无法序列化成功
            fixUserInfo(param, retryEvent.getEventInfo());
            userInfo = param.getUserInfoContext();
            UserInfoContext.setUser(userInfo);
            ReflectUtil.invoke(targetBean, retryEvent.getTargetInfo().getMethodName(), param);
            retryEventDAO.delete(retryEvent.getId());
        } catch (Exception e) {
            log.error("retry consume event occurs error:", e);
            retryEventDAO.updateRetry(retryEvent.getId());
        } finally {
            if (Objects.nonNull(userInfo)) {
                UserInfoContext.removeUserInfoContext();
            }
        }
    }

    private void fixUserInfo(BaseDomainEvent param, String eventInfo) throws ClassNotFoundException {
        JSONObject eventObj = JSONUtil.parseObj(eventInfo);
        if (!eventObj.containsKey("userInfoContext")) {
            return;
        }
        JSONObject userObj = eventObj.getJSONObject("userInfoContext");
        if (!userObj.containsKey("@type")) {
            return;
        }
        String userClazzName = userObj.getStr("@type");
        Class<?> userClazz = Class.forName(userClazzName);
        Object baseUserInfo = JSON.parseObject(userObj.getStr("userInfoContext"), userClazz);
        param.setUserInfoContext((BaseUserInfo) baseUserInfo);
    }
}
