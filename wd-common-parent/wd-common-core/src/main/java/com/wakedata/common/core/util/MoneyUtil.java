package com.wakedata.common.core.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @ClassName RefundGrowthValueSubListener
 * @Description 金钱工具类
 * @Date 2022/2/24 15:23
 * @Author zhizhongan
 */
public class MoneyUtil {

    /**
     * 将分单位转换为元
     * @param cent 分
     * @return 元
     */
    public static double parseToYuan(Long cent) {
        if (cent != null && cent != 0) {
            return new BigDecimal(cent).divide(new BigDecimal(100), 2, RoundingMode.HALF_EVEN).doubleValue();
        }
        return 0D;
    }
}
