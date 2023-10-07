/**
 * 基于MQ(rocketMQ|kafka|cmq)的事件封装
 * （使用MQ默认连接配置）
 *
 * @author chenshaopeng
 * @date 2021/12/17
 */
package com.wakedata.common.domainevent;

//  使用方式:

/*============================================================================================
 1. 定义事件和属性(javabean)
    @Data
    public class MemberGrowthChangeEvent extends BaseDomainEvent{
        //企业Id
        private Long epId;

        //uniqueAccountId
        private String uniqueAccountId;

        //当前成长值
        private Integer growthValue;

        //Topic不能为空, 同项目内领域事件topic可以相同
        @Override
        public String topic() {
            return "#{commonMqTopic.storeTopic}";
        }

        //EventCode不能为空, 该code用于区分不同的事件
        @Override
        public String eventCode() {
            return "#{commonEventCode.storeAddEvent}";
        }
    }

============================================================================================
  2. 发送者
      业务代码调用DomainEventPublisher.post(), DomainEventPublisher.postAsync()进行同步或者异步 执行 接收该事件的方法

============================================================================================
  3. 接收者方法上添加@DomainEventSubscribe注解
    @DomainEventSubscribe
    @Transactional(rollbackFor = Exception.class)
    public void OnGrowthChangeListener(MemberGrowthChangeEvent event) {
        log.info("growth value change:{}" + event.toString());
    }
============================================================================================
*/