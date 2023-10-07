package com.wakedata.common.domainevent.retry.dao;

import com.alibaba.fastjson.JSON;
import com.wakedata.common.domainevent.retry.module.RetryEventDO;
import com.wakedata.common.domainevent.retry.module.TargetInfo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * 领域事件重试DAO
 *
 * @author: hhf
 * @date: 2022/6/13
 **/
public class RetryEventDAO {

    @Resource
    JdbcTemplate jdbcTemplate;

    public void save(RetryEventDO failedEvent) {
        Integer count = jdbcTemplate.queryForObject("select count(*) from domain_event_redo_log where idem=?", new Object[]{failedEvent.getIdem()}, Integer.class);
        if (count == 0) {
            jdbcTemplate.update("insert into domain_event_redo_log (event_info, target_info, idem, retry_type, retry_times, last_retry_time, create_time, update_time) values (?,?,?,?,0, now(), now(),now())",
                    failedEvent.getEventInfo(), JSON.toJSONString(failedEvent.getTargetInfo()), failedEvent.getIdem(), failedEvent.getRetryType());
        }
    }

    public void delete(Long id) {
        jdbcTemplate.update("delete from domain_event_redo_log where id = ?", id);
    }

    public void updateRetry(Long id) {
        jdbcTemplate.update("update domain_event_redo_log " +
                "set retry_times = retry_times + 1, " +
                "last_retry_time = now(), " +
                "update_time = now(), " +
                "retry_type = (case when retry_times > 2 then 1 else 0 end) " +
                "where id = ?", id);
    }

    public List<RetryEventDO> selectEventList(int pageNo, int pageSize, Integer retryType) {
        int offset = (pageNo - 1) * pageSize;
        return jdbcTemplate.query("select * from domain_event_redo_log " +
                        "where retry_type = ? " +
                        "order by last_retry_time asc " +
                        "limit ?, ?",new Object[]{retryType, offset, pageSize},
                new FailedEventRowMapper());
    }

    static class FailedEventRowMapper implements RowMapper<RetryEventDO> {

        @Override
        public RetryEventDO mapRow(ResultSet rs, int num) throws SQLException {
            long id = rs.getLong("id");
            String eventInfo = rs.getString("event_info");
            String targetInfo = rs.getString("target_info");
            int retryTimes = rs.getInt("retry_times");
            Date lastRetryTime = rs.getDate("last_retry_time");
            Date createTime = rs.getDate("create_time");
            Date updateTime = rs.getDate("update_time");

            RetryEventDO eventDO = new RetryEventDO();
            eventDO.setId(id);
            eventDO.setEventInfo(eventInfo);
            eventDO.setTargetInfo(JSON.parseObject(targetInfo, TargetInfo.class));
            eventDO.setRetryTimes(retryTimes);
            eventDO.setLastRetryTime(lastRetryTime);
            eventDO.setCreateTime(createTime);
            eventDO.setUpdateTime(updateTime);
            return eventDO;
        }

    }

}
