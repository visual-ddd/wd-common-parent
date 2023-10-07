package com.wakedata.common.core.dto;

import com.wakedata.common.core.resultcode.CommonResultCode;
import com.wakedata.common.core.resultcode.ResultCode;

import java.io.Serializable;


public class PageResultDTO<T> extends ResultDTO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer pageSize;

    private Integer pageNo;

    private Long totalCount = 0L;


    public PageResultDTO() {
        setCode(CommonResultCode.SUCCESS.getCode());
    }

    public PageResultDTO(PageQuery pageQuery){
        if (pageQuery != null){
            this.pageNo = pageQuery.getPageNo();
            this.pageSize = pageQuery.getPageSize();
        }
        setCode(CommonResultCode.SUCCESS.getCode());
    }

    public PageResultDTO(PageResultDTO pageResultDTO){
        if (pageResultDTO != null){
            this.pageNo = pageResultDTO.getPageNo();
            this.pageSize = pageResultDTO.getPageSize();
            this.totalCount = pageResultDTO.getTotalCount();
        }
        setCode(CommonResultCode.SUCCESS.getCode());
    }

    public static <T> PageResultDTO<T> success(T value) {
        PageResultDTO<T> r = new PageResultDTO<T>();
        r.setData(value);
        return r;
    }

    /**
     * 分页游标
     */
    private Long cursor;

    public static <T> PageResultDTO<T> fail(ResultCode resultCode) {
        PageResultDTO<T> r = new PageResultDTO<T>();
        r.setCode(resultCode.getCode());
        r.setMsg(resultCode.getMsg());
        return r;
    }

    public static <T> PageResultDTO<T> fail() {
        PageResultDTO<T> r = new PageResultDTO<T>();
        r.setCode(CommonResultCode.SYSTEM_ERROR.getCode());
        r.setMsg(CommonResultCode.SYSTEM_ERROR.getMsg());
        return r;
    }

    public static <T> PageResultDTO<T> fail(String msg) {
        PageResultDTO<T> r = new PageResultDTO<T>();
        r.setCode(CommonResultCode.SYSTEM_ERROR.getCode());
        r.setMsg(msg);
        return r;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }


    public Integer getPageNo() {
        return pageNo;
    }


    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Long getCursor() {
        return cursor;
    }

    public void setCursor(Long cursor) {
        this.cursor = cursor;
    }

    @Override
    public String toString() {
        return "PageResultDTO{" + "pageSize=" + pageSize + ", pageNo=" + pageNo + ", totalCount="
                + totalCount + '}';
    }
}
