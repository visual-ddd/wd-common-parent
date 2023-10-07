package com.wakedata.common.core.jwt;

/**
 * jwt使用常量配置
 *
 * @author luomeng
 * @date 2022/6/23 11:49
 */
public class WdJwtConstant {

    /**
     * jwt生成密钥
     */
    public static final String JWT_GENERATE_SECRET = "+ja&I?0U9X=ydRyif!2EGdo?wwJF?=C?";

    /**
     * token设置有效天数
     */
    public static final Integer JWT_VALID_DAYS = 7;

    /**
     * 刷新token设置有效天数
     */
    public static final Integer JWT_REFRESH_VALID_DAYS = 30;

    /**
     * 刷新token设置有效时间，30天（单位秒）
     */
    public static final Long JWT_REFRESH_VALID_SECONDS = 24L * 60 * 60 * JWT_REFRESH_VALID_DAYS;

}
