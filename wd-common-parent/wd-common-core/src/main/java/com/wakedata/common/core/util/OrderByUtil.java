package com.wakedata.common.core.util;

import com.wakedata.common.core.constants.SortTypeEnum;
import com.wakedata.common.core.dto.PageQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

/**
 * @author: hhf
 * @date: 2021/2/1
 **/
public class OrderByUtil {

    private static final String COMMA = " ";
    private static final String DELIMITER = ",";

    public static String buildOderBySql(PageQuery pageQuery){
        if (CollectionUtils.isEmpty(pageQuery.getSortingFields())) {
            return StringUtils.EMPTY;
        }
        return pageQuery.getSortingFields().stream()
                .filter(x-> !StringUtils.isEmpty(x.getColumn()))
                .map(x-> x.getColumn().concat(COMMA).concat(x.isAsc()? SortTypeEnum.ASC.getDesc():SortTypeEnum.DESC.getDesc()))
                .collect(Collectors.joining(DELIMITER));
    }
}
