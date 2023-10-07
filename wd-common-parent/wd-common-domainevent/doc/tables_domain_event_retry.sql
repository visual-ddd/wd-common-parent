--
-- Create table `domain_event_redo_log`
--
CREATE TABLE domain_event_redo_log (
   id int NOT NULL AUTO_INCREMENT COMMENT '主键',
   event_info json DEFAULT NULL COMMENT '事件内容',
   target_info json DEFAULT NULL COMMENT '重试目标信息',
   retry_times int DEFAULT 0 COMMENT '重试次数',
   last_retry_time datetime DEFAULT NULL COMMENT '最后重试时间',
   retry_type int DEFAULT NULL COMMENT '重试类型 0:定时调度  1:重启时重试 ',
   idem varchar(64) DEFAULT NULL COMMENT '幂等值',
   create_time datetime DEFAULT NULL COMMENT '创建时间',
   update_time datetime DEFAULT NULL COMMENT '更新时间',
   PRIMARY KEY (id)
)
ENGINE = INNODB,
COMMENT = '领域事件重试表';

--
-- Create table `domain_event_lock`
--
CREATE TABLE domain_event_lock (
    lock_name varchar(50) DEFAULT NULL COMMENT '锁名称'
)
ENGINE = INNODB,
COMMENT = '领域事件锁';

--
-- Create index `UK_domain_event_redo_log_idem` on table `domain_event_redo_log`
--
ALTER TABLE domain_event_redo_log
    ADD UNIQUE INDEX UK_domain_event_redo_log_idem (idem);

--
-- init data
--
INSERT INTO domain_event_lock (lock_name) VALUES ('schedule_lock');
