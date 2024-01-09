package com.wakedata.common.core.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * @author luomeng
 * @date 2022/2/12 15:41
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class EmployeeInfoWrapper extends BaseUserInfo implements Serializable {

    private static final long serialVersionUID = -6148046460545232674L;

    /*** 租户员工id*/
    private Long employeeId;

    /*** 租户员工名称*/
    private String employeeName;

    /**
     * 角色ID
     */
    private List<String> roleIdList;
    /**
     * 角色身份标识
     */
    private List<String> roleIdentifierList;

    /**
     * 用户微信openId
     */
    private String openId;

    /**
     * 用户微信unionId
     */
    private String unionId;
    /**
     * 用户头像
     */
    private String avatarImgUrl;

    /**
     * 门店权限id
     */
    private List<Long> permissionStoreBuIds;


}
