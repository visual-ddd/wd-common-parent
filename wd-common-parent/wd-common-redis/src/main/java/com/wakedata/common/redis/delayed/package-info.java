/**
 * 基于redis的注解式分布式锁的实现
 *
 * @author hhf
 * @date 2021/1/7
 */
package com.wakedata.common.redis.delayed;

//  基于redisson延时队列使用方式:
//    1.发送消息
//    //在某个时间接消息
//    RedisDelayQueueUtil.getInstance().fireAt(what, LocalDateTime.now().plusSeconds(-5));
//    //延时1秒接收到消息
//    RedisDelayQueueUtil.getInstance().fireDelaySeconds(what, 1);
//    //延时1分钟接收到消息
//    RedisDelayQueueUtil.getInstance().fireDelayMinute(what, 1);
//    //延时1小时接收到消息
//    RedisDelayQueueUtil.getInstance().fireDelayHour(what, 1);
//    //延时1天接收到消息
//    RedisDelayQueueUtil.getInstance().fireDelayDay(what, 1);
//    2.接收消息
//    //spring托管的类方法上添加注解 @DelayMsgSubscribe
//    示例
//    @Component
//    public class TaskListener {

//    @DelayMsgSubscribe
//    public void onMessage(ActivityStartCmd activityStartCmd) {
//    }
//}


