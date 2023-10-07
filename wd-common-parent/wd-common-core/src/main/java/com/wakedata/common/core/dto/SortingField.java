package com.wakedata.common.core.dto;

import com.wakedata.common.core.base.DTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类名加了 ing 的原因是，避免和 ES SortField 重名。
 *
 * @date 2021/1/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SortingField extends DTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("排序字段")
    private String column;

    @ApiModelProperty(value = "是否升序, 默认升序")
    private boolean asc = true;
}