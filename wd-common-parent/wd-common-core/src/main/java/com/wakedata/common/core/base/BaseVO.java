package com.wakedata.common.core.base;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @date 2022/1/12 21:08 基础vo
 */
@Getter
@Setter
@ToString
public class BaseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    /**
     * 业务单元名称
     */
    @ApiModelProperty("业务单元名称")
    private String tenantName;


    @ApiModelProperty(value = "业务id")
    private Long appBuId;

    /**
     * 业务单元名称
     */
    @ApiModelProperty("业务单元名称")
    private String appBuName;

    @ApiModelProperty(value = "业务id")
    private Long buId;

    /**
     * 业务单元名称
     */
    @ApiModelProperty("业务单元名称")
    private String buName;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建人昵称")
    private String createNickName;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改人昵称")
    private String updateNickName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern= DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern= DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime updateTime;
}
