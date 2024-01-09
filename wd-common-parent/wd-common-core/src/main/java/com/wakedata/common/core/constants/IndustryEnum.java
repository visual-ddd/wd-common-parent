package com.wakedata.common.core.constants;

import java.util.ArrayList;
import java.util.List;

/**
 * 行业枚举
 *
 * @author hhf
 * @date 2022/6/15
 */
public enum IndustryEnum {

    /**
     * 行业枚举
     */
    DEFAULT("all", "默认"),
    BEAUTIFUL("beautiful", "美业"),
    RETAIL("retail", "商超零售"),
    TOURISM("tourism", "旅游"),
    UMP("ump", "集团"),
    COMMERCE("commerce", "商圈"),
    ESTATE_BUSINESS("estate_business", "地产置业"),
    BUSINESS_CENTER("business_center", "商业中心"),
    OFFICE_BUILDING("office_building", "写字楼"),
    SHOPPING_CENTER("shopping_center", "购物中心"),
    HOTEL_TRAVEL("hotel_travel", "酒店文旅"),
    HOME_BUILD_MATERIAL("home_build_material", "家居建材"),
    SCRM("scrm", "企业服务"),
    KANG_YANG("kangyang", "康养业务"),
    SALES_SCRM("sales_scrm", "数字化销售");

    private String industryKey;

    private String name;

    IndustryEnum(String industryKey, String name) {
        this.industryKey = industryKey;
        this.name = name;
    }


    public String getIndustryKey() {
        return industryKey;
    }


    public void setIndustryKey(String industryKey) {
        this.industryKey = industryKey;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public static List<IndustryEnum> getEnum(List<String> identityList) {
        List<IndustryEnum> roleEnumList = new ArrayList<>();
        for (IndustryEnum roleEnum : IndustryEnum.values()) {
            for (String identity : identityList) {
                if (roleEnum.getIndustryKey().equals(identity)) {
                    roleEnumList.add(roleEnum);
                }
            }
        }
        return roleEnumList;
    }

}
