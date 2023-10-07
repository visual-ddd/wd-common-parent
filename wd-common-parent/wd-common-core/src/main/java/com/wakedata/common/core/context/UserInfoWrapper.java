package com.wakedata.common.core.context;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ObjectUtils;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class UserInfoWrapper extends BaseUserInfo implements Serializable {

    private static final long serialVersionUID = -6148046460545232674L;

    /**
     * 用户名称（账号）
     */
    private String userName;

    /*** 租户员工id*/
    private Long employeeId;

    /*** 租户员工名称*/
    private String employeeName;

    /**
     * 平台域ID
     */
    private Long domainId;

    /**
     * 角色ID
     */
    private List<String> roleIdList;
    /**
     * 角色身份标识
     */
    private List<String> roleIdentifierList;
    /**
     * 是否是租户管理员
     */
    private Boolean tenantAdmin;



    /**
     * 是否是集团应用
     *
     * @return 是返回true
     */
    public boolean isUmpApp() {
        return "ump".equalsIgnoreCase(getString(OtherUserProperties.APP_KEY_FLAG));
    }

    /**
     * 添加信息到other map
     *
     * @param key   键
     * @param value 值
     */
    public UserInfoWrapper put(String key, Object value) {
        this.other = ObjectUtils.defaultIfNull(other, Maps.newHashMap());
        this.other.put(key, value);
        return this;
    }

}
