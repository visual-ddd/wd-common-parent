package com.df.common.sms.enums;

/**
 * @author luomeng
 * @Description 国际区号编码
 * @createTime 2021-12-11 11:43:00
 */
public enum NationCodeEnum {

    /**
     * 国际区号
     */
    CHINA("86","中国"),
    HK("852","香港"),
    MACAO("853","澳门"),
    TW("886","台湾");

    private String code;
    private String desc;

    NationCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
