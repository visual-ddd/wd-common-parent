package com.wakedata.common.core.jwt;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 保存在jwt payload中的信息
 * @author luomeng
 * @date 2022/6/22 20:27
 */
@Data
@NoArgsConstructor
public class WdPayload {
    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 账号id
     */
    private Long userId;
    /**
     * 员工id
     */
    private Long employeeId;
    /**
     * 不传默认使用uuid生成
     */
    private String jti;

    public WdPayload(Long userId) {
        this.userId = userId;
    }

    public WdPayload(Long tenantId, Long userId, Long employeeId) {
        this.tenantId = tenantId;
        this.userId = userId;
        this.employeeId = employeeId;
    }

    /**
     * 是否登录成功
     * @return
     */
    public Boolean isLoginSuccess() {
        return this.tenantId != null && this.tenantId > 0;
    }

}
