package com.wakedata.common.core.context;

import com.wakedata.common.core.constants.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * C 端用户登录信息封装
 *
 * @author zhangtielong
 * @date 2021-07-23 copy from dss_cshop
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class MemberInfoWrapper extends BaseUserInfo implements Serializable {

    /**
     * 微信APPID
     */
    private String wxAppId;

    /**
     * 登录类型 LoginStrategyEnum
     */
    private Integer loginType;


    /**
     * 配置
     */
    private Integer customizedDistanceShop;

    private Integer customizedRegionShop;

    /**
     * 省份id
     */
    private Long provinceId;

    /**
     * 城市
     */
    private Long cityId;

    /**
     * 区域id
     */
    private Long districtId;

    private String sessionKey;
    private String sessionCreateTime;
    private Date sessionExpireTime;

    /**
     * 用户的员工身份 {@link com.wakedata.common.core.constants.RoleEnum}
     */
    private List<RoleEnum> roleEnum;

    /**
     * 用户微信openId
     */
    private String openId;

    private String unionId;
    private String avatarImgUrl;

    /**
     * 用户收货地址
     */
    private Long addressId;

    /**
     * 会员一账通id
     */
    private String uniqueAccountId;

    /**
     * 二维码扫描登录所需参数
     */
    private String qrCodeScene;

    /**
     * 二维码扫描类型 ShareQrCodeTypeEnum
     */
    private Integer qrCodeType;
}
