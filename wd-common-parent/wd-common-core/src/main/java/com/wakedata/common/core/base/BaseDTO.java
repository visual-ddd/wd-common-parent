package com.wakedata.common.core.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wakedata.common.core.base.DTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author focus
 */
@Data
public class BaseDTO extends DTO {

    private static final long serialVersionUID = 1L;

    protected Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    protected LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    protected LocalDateTime updateTime;

    @ApiModelProperty("创建人")
    protected String createBy;

    @ApiModelProperty("更新人")
    protected String updateBy;

}
