package com.wakedata.common.core.constants;

import com.wakedata.common.core.base.BaseEnum;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

/**
 * 性别枚举
 * @author dengqingtao
 * @version 1.0.0
 * @date 2022/1/11 19:51
 */
public enum GenderTypeEnum  implements BaseEnum {
    /**
     * 未知
     */
    UNKNOWN(0, "未知"),
    /**
     * 男
     */
    MALE(1, "男"),
    /**
     * 女
     */
    FEMALE(2, "女"),;

    private Integer value;
    private String desc;

    GenderTypeEnum(Integer code, String desc) {
        this.value = code;
        this.desc = desc;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public static GenderTypeEnum parseDesc(String desc) {
        if (StringUtils.isBlank(desc)){
            return null;
        }
        for (GenderTypeEnum typeEnum : values()) {
            if (typeEnum.desc.equals(desc)){
                return typeEnum;
            }
        }
        return null;
    }


    public static GenderTypeEnum parseValue(Integer value){
        if (value == null){
            return null;
        }
        for(GenderTypeEnum typeEnum : values()){
            if(typeEnum.value.equals(value)){
                return typeEnum;
            }
        }
        return null;
    }

}
