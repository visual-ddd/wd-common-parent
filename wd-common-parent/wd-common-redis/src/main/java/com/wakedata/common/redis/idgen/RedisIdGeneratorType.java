package com.wakedata.common.redis.idgen;

/**
 * redis id 生成器业务类型枚举值
 *
 * @author zhangtielong
 * @date 2021/1/6
 */
public enum RedisIdGeneratorType {

    /**
     * 会员一账通Id
     */
    MEMBER_UNIQUE_ACCOUNT_ID("member_unique_account_id"),
    /**
     * 会员编号
     */
    MEMBER_USER_NUM("member_user_num"),

    /**
     * 康养核销码
     */
    KY_WRITE_OFF_CODE("kangyang_write_off_code"),

    KY_RANDOM_CREDENTIAL_NO("kangyang_random_credential_no"),

    /**
     * 商业中心项目编码
     */
    BUSINESS_MALL_PROJECT_CODE("business_mall_project_code"),

    /**
     * 商业中心商户编码
     */
    BUSINESS_MALL_MERCHANT_CODE("business_mall_merchant_code"),

    /**
     * 商业中心店铺编码
     */
    BUSINESS_MALL_STORE_CODE("business_mall_store_code"),

    /**
     * 商业中心组织架构编码
     */
    BUSINESS_MALL_ORG_CODE("business_mall_org_code");


    private final String key;

    RedisIdGeneratorType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
