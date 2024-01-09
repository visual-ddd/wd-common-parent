package com.wakedata.common.core.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pengxu on 2018/5/18.
 */
public enum RoleEnum {

    /**
     * 角色枚举
     */
    KEY_ADMIN("common_role_admin","Key管理员"),
    EP_ADMIN("common_role_enterprise","企业管理员"),
    STORE_KEEPER("common_role_storeKeeper","店长"),
    STORE_EMPLOYEE("common_role_clerk","店员"),
    STORE_CASHIER("common_role_cashier","收银员"),
    STORE_SALES("common_role_sales","导购员"),
    STORE_VERIFICATION("common_role_verifications","核销员"),
    STORE_TECHNICIAN("common_role_technician","技师"),
    WAREHOUSE_KEEPER("common_role_warehouseKeeper","仓库管理员"),
    OPERATOR("common_role_operator","运营"),
    FINANCE("common_role_finance","财务"),
    GROUP_FINANCE("common_role_group_finance","集团财务"),
    AREA_MANAGER("common_role_area_manager","区域管理员"),
    DEALER_MANAGER("common_role_dealer_manager","经销商管理员"),
    INTEGRAL_AUDIT("common_role_integral_audit","积分审核人员"),
    PROVIDER_MANAGE("common_role_provider","供应商管理员"),
    POINT_SITE_MANAGE("common_role_point_site","分站点管理员"),
    PLATFORM_MANAGER("common_role_platform_manage","平台管理员"),
    SELF_STORE_MANAGER("common_role_self_store_manager","BBC自营店铺管理员"),
    PRODUCT_OPERATOR("common_role_product_operator","商品运营"),
    CONTENT_OPERATE("common_role_content_operator","内容运营"),
    DESIGNER("common_role_designer","设计师"),
    STORE_DESIGNER("common_role_store_designer","门店设计师");

    private String identity;

    private String name;

    RoleEnum(String identity, String name) {
        this.identity = identity;
        this.name = name;
    }


    public String getIdentity() {
        return identity;
    }


    public void setIdentity(String identity) {
        this.identity = identity;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public static List<RoleEnum> getEnum (List<String> identityList){
        List<RoleEnum> roleEnumList = new ArrayList<>();
        for (RoleEnum roleEnum: RoleEnum.values()) {
            for (String identity:identityList) {
                if (roleEnum.getIdentity().equals(identity)){
                    roleEnumList.add(roleEnum);
                }
            }
        }
        return roleEnumList;
    }

    /**
     * 拥有所有权限的角色标识集合
     * @return
     */
    public static List<String> adminIdentifier(){
        return Arrays.asList(RoleEnum.EP_ADMIN.getIdentity(), RoleEnum.KEY_ADMIN.getIdentity());
    }
}
