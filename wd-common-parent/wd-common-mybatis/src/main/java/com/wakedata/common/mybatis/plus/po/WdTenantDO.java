package com.wakedata.common.mybatis.plus.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 租户基础类
 * @author luomeng
 * @date 2021/12/23 16:06
 */
@Data
public class WdTenantDO extends BaseDO{

    /**
     * 租户ID
     */
    @TableField(value = "tenant_id", fill = FieldFill.INSERT)
    protected Long tenantId;


}
