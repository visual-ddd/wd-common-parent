
package com.wakedata.common.mq.core;

import com.wakedata.common.mq.config.*;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;

/**
 * 消息中心自动装配
 *
 * @author chenshaopeng
 * @date 2021/12/8
 */
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Import({MessageCenterAnnotationBeanPostProcessor.class, CreationMessageConsumer.class})
public class MessageCenterAutoConfiguration {

	@Bean("commonConfig")
	public CommonConfig commonConfig() {
		return new CommonConfig();
	}

	/**
	 * 私有的自定义配置
	 * 可以指定自由连接信息和topic等
	 */
	@Bean("privateMqConfig")
	@ConfigurationProperties(prefix = PrivateMqConfig.PREFIX)
	public PrivateMqConfig privateMqConfig() {
		return new PrivateMqConfig();
	}

	/**
	 * 公共默认RocketMQ配置, 含连接地址
	 */
	@Bean("defaultRocketMqConfig")
	public DefaultRocketMqConfig defaultRocketMqConfig() {
		return new DefaultRocketMqConfig();
	}

	/**
	 * 公共默认CMQ配置, 含连接地址
	 */
	@Bean("defaultCmqConfig")
	public DefaultCmqConfig defaultCmqConfig() {
		return new DefaultCmqConfig();
	}

	/**
	 * 公共默认Kafka配置, 含连接地址
	 */
	@Bean("defaultKafkaConfig")
	public DefaultKafkaConfig defaultKafkaConfig() {
		return new DefaultKafkaConfig();
	}

	/**
	 * 公共默认RabbitMQ配置, 含连接地址
	 */
	@Bean("defaultRabbitMqConfig")
	public DefaultRabbitMqConfig defaultRabbitMqConfig() {
		return new DefaultRabbitMqConfig();
	}

	/**
	 * 默认配置适配器
	 */
	@Bean
	public DefaultConfigurationAdapter defaultConfigAdapter(){
		return new DefaultConfigurationAdapterImpl();
	}


}
