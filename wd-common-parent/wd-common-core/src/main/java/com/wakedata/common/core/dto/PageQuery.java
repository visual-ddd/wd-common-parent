package com.wakedata.common.core.dto;

import com.wakedata.common.core.base.BaseQuery;
import com.wakedata.common.core.constants.PageConstant;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.List;


public class PageQuery extends BaseQuery {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "每页数量不能为空")
    @ApiModelProperty(value = "每页数量(不能为空)", example = "10")
    @Max(value = 200, message = "每页最大为200")
    private Integer pageSize;

    @NotNull(message = "分页参数不能为空")
    @ApiModelProperty(value = "页码(不能为空)", example = "1")
    private Integer pageNo;

    @ApiModelProperty("是否查询总条数")
    private Boolean searchCount;

    private Integer offset;

    public Integer getPageSize() {
        if (pageSize == null || pageSize < 1) {
            return PageConstant.DEFAULT_PAGE_SIZE;
        }
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        if (null == pageNo || pageNo < 1) {
            return PageConstant.DEFAULT_PAGE_NO;
        }
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Boolean getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(Boolean searchCount) {
        this.searchCount = searchCount;
    }

    public Integer getOffset() {
        if(this.pageNo == null || this.pageSize == null){
            return 0;
        }
        return this.pageNo == 0 ? 0 : (this.pageNo - 1) * this.pageSize;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
