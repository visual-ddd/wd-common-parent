package com.wakedata.common.mybatis.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.wakedata.common.core.dto.PageResultDTO;

import java.util.List;


/**
 * @author: hhf
 * @date: 2021/12/21
 **/
public class PageHelperUtil {

    public static <T> PageResultDTO<List<T>> convertPage(PageInfo<T> pageInfo) {
        PageResultDTO<List<T>> pageResultDTO = new PageResultDTO<>();
        pageResultDTO.setData(pageInfo.getList());
        pageResultDTO.setTotalCount(pageInfo.getTotal());
        pageResultDTO.setPageNo(pageInfo.getPageNum());
        pageResultDTO.setPageSize(pageInfo.getPageSize());
        return pageResultDTO;
    }

    /**
     * @Description: 将IPage转换成PageResultDTO
     * @Author: DemonsPan
     * @Date: 2022/4/7
     * @Time: 12:08 下午
     */
    public static PageResultDTO wrapPageResultDTO(IPage<?> page) {
        PageResultDTO pageResult = new PageResultDTO();
        pageResult.setPageNo(Math.toIntExact(page.getCurrent()));
        pageResult.setPageSize(Math.toIntExact(page.getSize()));
        pageResult.setCursor(page.getCurrent());
        pageResult.setTotalCount(page.getTotal());
        pageResult.setData(page.getRecords());
        return pageResult;
    }
}
