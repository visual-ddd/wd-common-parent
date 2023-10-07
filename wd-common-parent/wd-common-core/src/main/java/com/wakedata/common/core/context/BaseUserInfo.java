package com.wakedata.common.core.context;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 用户信息基类
 *
 * @date 2022/2/9
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class BaseUserInfo implements Serializable {

    /**
     * 租户ID(小程序所属企业ID)
     */
    protected Long tenantId;
    /**
     * 租户名称
     */
    protected String tenantName;

    /**
     * 应用对应的业务单元id(小程序所属店铺ID)
     */
    protected Long appBuId;

    /*** 应用名称*/
    protected String appBuName;
    /**
     * 应用下所属的业务单元id 对应门店那一个层级的业务单元id
     */
    protected Long buId;

    /**
     * 应用下所属的业务单元名称
     *
     */
    protected String buName;

    /**
     * userId 就是string接受，有些项目是string 登录账号ID
     */
    protected String userId;

    /**
     * pc端为用户名称（c端用户微信昵称）
     */
    protected String nickName;

    /**
     * 手机号码
     */
    protected String phone;


    /**
     * other 其他非公共信息 ,可以由外部控制 {@link OtherUserProperties}
     */
    protected Map<String, Object> other;

    /**
     * 添加信息到other map
     *
     * @param key   键
     * @param value 值
     */
    public BaseUserInfo putOther(String key, Object value) {
        other = ObjectUtils.defaultIfNull(other, Maps.newHashMap());
        other.put(key, value);
        return this;
    }

    /**
     * 从other map获取属性值
     *
     * @param key    键
     * @param tClass 返回对象类型
     */
    public <T> T get(String key, Class<T> tClass) {
        return MapUtil.get(other, key, tClass);
    }

    /**
     * 从other map获取属性值
     *
     * @param key    键
     * @param tClass 返回对象类型
     */
    public <T> List<T> getList(String key, Class<T> tClass) {
        if (CollUtil.isEmpty(other)) {
            return Collections.emptyList();
        }
        if (other instanceof JSONObject) {
            return Optional.ofNullable(((JSONObject) other).getJSONArray(key))
                    .map(item -> item.toJavaList(tClass)).orElse(Collections.emptyList());
        } else if (other instanceof HashMap) {
            List<?> list = (List<?>) other.getOrDefault(key, Collections.emptyList());
            return list.stream().map(item -> Convert.convert(tClass, item)).collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("other param type error");
        }
    }

    /**
     * 从other map获取String属性值
     *
     * @param key 键
     */
    public String getString(String key) {
        return MapUtil.getStr(other, key);
    }

    /**
     * 从other map获取Integer属性值
     *
     * @param key 键
     */
    public Integer getInteger(String key) {
        return MapUtil.getInt(other, key);
    }

    /**
     * 从other map获取Long属性值
     *
     * @param key 键
     */
    public Long getLong(String key) {
        return MapUtil.getLong(other, key);
    }
}
