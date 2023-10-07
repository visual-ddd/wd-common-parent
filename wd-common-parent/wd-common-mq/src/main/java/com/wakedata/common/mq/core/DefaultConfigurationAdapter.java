package com.wakedata.common.mq.core;

import com.wakedata.common.mq.model.MqConfigParam;
import org.springframework.core.Ordered;

/**
 * 公共配置/默认配置适配器接口
 *
 * @author chenshaopeng
 * @date 2021/12/13
 */
public interface DefaultConfigurationAdapter extends Ordered {

    /**
     * 设置默认公共配置
     *
     * @param target 来源MQ配置参数对象
     */
    void setDefaultCommonConfig(MqConfigParam target);

}
