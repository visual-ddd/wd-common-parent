package com.wakedata.common.redis.jedis.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 对应配置文件common-wx-component.yaml
 *
 */
@Component
@RefreshScope
@Deprecated
public class CommonWxComponentConfig {

    public static CommonWxComponentConfig getInstance(){
        return BaseApplicationContext.getBeanByClass(CommonWxComponentConfig.class);
    }
    /**
     * 微信第三方开放平台支付服务商 绑定的微信公众号WX_APP_ID，服务商支付模式配置该ID
     */
    @Value("${wx.mp.appid}")
    private  String wxComponentMpAppId;

    /**
     * 微信第三方开放平台加解密token
     */
    @Value("${wx.encrypt.token}")
    private String wxComponentEncryptToken;

    /**
     * 微信第三方开放平台加解密密钥
     */
    @Value("${wx.encrypt.key}")
    private  String wxComponentEncryptKey;

    /**
     * 微信第三方开放平台服务商wxAppId
     */
    @Value("${wx.appid}")
    private  String wxComponentAppId;

    /**
     * 微信第三方开放平台服务商商户ID
     */
    @Value("${wx.component.mchid}")
    private  String wxComponentMchId;
    /**
     * 微信第三方开放平台服务商商户支付秘钥
     */
    @Value("${wx.component.mchsecret}")
    private  String wxComponentMchSecret;

    /**
     * 微信第三方开放平台服务商秘钥文件路径
     */
    @Value("${wx.component.cert.path}")
    private  String wxComponentMchCertPath;

    /**
     * 微信第三方开放平台AppSecret
     */
    @Value("${wx.appsecret}")
    private  String wxComponentAppSecret = "wx.appsecret";

    @Value("${wx.auth.redirect.url}")
    private  String wxAuthRedirectUrl = "wx.auth.redirect.url";


    public String getWxComponentMpAppId() {
        return wxComponentMpAppId;
    }

    public void setWxComponentMpAppId(String wxComponentMpAppId) {
        this.wxComponentMpAppId = wxComponentMpAppId;
    }

    public String getWxComponentEncryptToken() {
        return wxComponentEncryptToken;
    }

    public void setWxComponentEncryptToken(String wxComponentEncryptToken) {
        this.wxComponentEncryptToken = wxComponentEncryptToken;
    }

    public String getWxComponentEncryptKey() {
        return wxComponentEncryptKey;
    }

    public void setWxComponentEncryptKey(String wxComponentEncryptKey) {
        this.wxComponentEncryptKey = wxComponentEncryptKey;
    }

    public String getWxComponentAppId() {
        return wxComponentAppId;
    }

    public void setWxComponentAppId(String wxComponentAppId) {
        this.wxComponentAppId = wxComponentAppId;
    }

    public String getWxComponentMchId() {
        return wxComponentMchId;
    }

    public void setWxComponentMchId(String wxComponentMchId) {
        this.wxComponentMchId = wxComponentMchId;
    }

    public String getWxComponentMchSecret() {
        return wxComponentMchSecret;
    }

    public void setWxComponentMchSecret(String wxComponentMchSecret) {
        this.wxComponentMchSecret = wxComponentMchSecret;
    }

    public String getWxComponentMchCertPath() {
        return wxComponentMchCertPath;
    }

    public void setWxComponentMchCertPath(String wxComponentMchCertPath) {
        this.wxComponentMchCertPath = wxComponentMchCertPath;
    }

    public String getWxComponentAppSecret() {
        return wxComponentAppSecret;
    }

    public void setWxComponentAppSecret(String wxComponentAppSecret) {
        this.wxComponentAppSecret = wxComponentAppSecret;
    }

    public String getWxAuthRedirectUrl() {
        return wxAuthRedirectUrl;
    }

    public void setWxAuthRedirectUrl(String wxAuthRedirectUrl) {
        this.wxAuthRedirectUrl = wxAuthRedirectUrl;
    }
}
