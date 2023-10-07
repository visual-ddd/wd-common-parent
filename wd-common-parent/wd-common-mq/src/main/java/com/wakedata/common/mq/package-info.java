/**
 * 基于MQ(rocketMQ|kafka|cmq)的消息队列封装
 * （提供多级配置选择器，优先级：公共配置 < 私有配置 < 自定义配置）
 */
package com.wakedata.common.mq;

//  使用方式:

/*============================================================================================
 1. 启用消息中心，在Application上增加类注解@EnableMessageCenter

    @SpringBootApplication(scanBasePackages = {"com.wakedata.test"})
    @EnableMessageCenter
    public class Application {

        public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }

    }

============================================================================================
  2. 发送消息
     String topic = "testTopic";

    // 根据默认配置和指定topic发送消息
    MqProducerHelper.publish("测试消息", topic);

    // 根据默认配置和指定协议+topic发送消息
    Map<String, Object> objects = new HashMap<>();
    objects.put("name", "csp");
    objects.put("phone", "18607058631");
    MqProducerHelper.publish(objects, RequestProtocolEnum.KAFKA, topic);

    // 根据自定义配置发送消息
    MqConfigParam customParams = new MqConfigParam();
    customParams.setBootstrapServers("test.kafka.server.url");
    customParams.setProtocol(RequestProtocolEnum.KAFKA);
    customParams.setTopic(topic);
    MqProducerHelper.publish(objects, customParams);

============================================================================================
  3. 订阅消息

    // 自定义topic订阅消息
    @MessageSubscribe(topic = "testTopic")
    public void receive1(String message){
        System.out.println("接收到的消息：" + message);
    }

    // 使用外部配置配置订阅消息
    @MessageSubscribe(topic = "${topic}", bootstrapServers = "#{commonMqConfig.getBootstrapServers()}")
    public void receive2(Map<String, Object> map){
        System.out.println("接收到的消息：" + map);
    }

    // 使用私有配置订阅消息
    @MessageSubscribe(config = "config1")
    public void receive3(JSONObject jsonObject){
        System.out.println("接收到的消息：" + jsonObject);
    }

    // 使用私有配置和自定义top订阅消息
    @MessageSubscribe(config = "config1", topic = "topic2")
    public void receive4(UserInfo uesUserInfo){
        System.out.println("接收到的消息：" + uesUserInfo);
    }
============================================================================================
*/