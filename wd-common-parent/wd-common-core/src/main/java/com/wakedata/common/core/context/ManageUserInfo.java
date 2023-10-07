package com.wakedata.common.core.context;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 权限后台用户信息
 * @date 2022/11/11 11:14
 */
@Data
@NoArgsConstructor
public class ManageUserInfo implements Serializable {

    /**
     * 自增id
     */
    private Long id;

    /**
     * 账号
     */
    private String account;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 业务域id
     */
    private List<Long> scopeIdList;

    /**
     * 平台id
     */
    private Long platformId;

}
