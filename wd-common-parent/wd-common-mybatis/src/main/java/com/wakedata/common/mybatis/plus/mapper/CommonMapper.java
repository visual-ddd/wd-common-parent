package com.wakedata.common.mybatis.plus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * Mapper 层基类
 *
 * @author neexz
 */
public interface CommonMapper<T> extends BaseMapper<T> {

    /**
     * 批量插入
     *
     * @param collection 批量插入数据
     * @return ignore
     */
    int insertByBatch(@Param("collection") Collection<T> collection);

    /**
     * 全字段更新，不会忽略null值
     *
     * @param entity
     * @return
     */
    int fullUpdateById(@Param("et") T entity);
}
