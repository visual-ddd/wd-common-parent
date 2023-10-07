package com.wakedata.common.core.dto;

import com.wakedata.common.core.base.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 租户基础DTO
 * @date 2021/12/24 10:21
 */
@Data
public class WdTenantDTO extends BaseDTO {

    /**
     * 租户ID
     */
    @ApiModelProperty("租户ID")
    protected Long tenantId;

}
