package com.wakedata.common.core.jwt;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTException;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.wakedata.common.core.exception.BizException;
import com.wakedata.common.core.resultcode.CommonResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.Map;

/**
 * 提供jwt生成及校验
 * @author luomeng
 * @date 2022/6/22 20:30
 */
@Slf4j
public class WdJwtUtil {

    /**
     * 默认密钥
     */
    private static final String DEFAULT_SECRET = "4Qm5JYf+BaOv*i=@PY-380JdHp7Qj^+-";

    /**
     * 默认有效天数
     */
    private static final Integer DEFAULT_VALID_DAYS = 7;

    /**
     * 时间偏差
     */
    private static final int LEE_WAY = 0;

    /**
     * 创建token
     * @param wdPayload 有效荷载里面保存的业务数据
     * @param validDays 有效期(天)
     * @param secret 加密密钥,没有会使用默认值
     * @return
     */
    public static String createToken(WdPayload wdPayload,Integer validDays,String secret){
        return JWTUtil.createToken(getPayload(beanToMap(wdPayload),getValidDays(validDays)),getSecret(secret) );
    }

    /**
     * 获取Jwt，解析token
     * @param token
     * @param secret
     * @return
     * @throws BizException
     */
    public static JWT parseToken(String token, String secret) throws BizException{
        try {
            return JWTUtil.parseToken(token).setSigner(getSecret(secret));
        }catch (JWTException | JSONException jwtEx){
            log.error("jwt parse fail :{}", token);
            throw new BizException(CommonResultCode.JWT_INVALID);
        }
    }

    /**
     * 验证token是否有效
     * @param token token串
     * @param secret 加密密钥，没有会使用默认值
     * @return
     */
    public static Boolean verify(String token,String secret){
        return parseToken(token,secret).verify();
    }

    /**
     * 验证token是否生效或过期
     * @param token token串
     * @param secret 加密密钥，没有会使用默认值
     * @return
     */
    public static Boolean verifyDate(String token,String secret){
        return parseToken(token, secret).validate(LEE_WAY);
    }

    /**
     * 解析并获取payload的信息,校验token及有效期
     * @param token token串
     * @param secret 加密密钥，没有会使用默认值
     * @throws BizException token无效、过期均会抛异常
     * @return
     */
    public static WdPayload getWdPayloadAndVerify(String token, String secret) throws BizException{
        JWT jwt = getJwtAndVerify(token, secret);
        if(!jwt.validate(LEE_WAY)){
            log.error("jwt verify validate fail :{}",token);
            throw new BizException(CommonResultCode.JWT_EXPIRED);
        }
        return JSONUtil.toBean(jwt.getPayloads(),WdPayload.class);
    }

    /**
     * 解析并获取payload的信息,只做token校验
     * @param token token串
     * @param secret 加密密钥，没有会使用默认值
     * @return
     * @throws BizException
     */
    public static WdPayload getWdPayload(String token,String secret) throws BizException{
        JWT jwt = getJwtAndVerify(token, secret);
        return JSONUtil.toBean(jwt.getPayloads(),WdPayload.class);
    }

    /**
     * 获取jwt信息并校验是否有效
     * @param token
     * @param secret
     * @return
     */
    private static JWT getJwtAndVerify(String token, String secret) {
        JWT jwt = parseToken(token, secret);
        if (!jwt.verify()) {
            log.error("jwt verify fail :{}", token);
            throw new BizException(CommonResultCode.JWT_INVALID);
        }
        return jwt;
    }

    /**
     * 设置有效期
     * @param payload
     * @param validDays
     * @return
     */
    private static Map<String,Object> getPayload(Map<String,Object> payload,Integer validDays){
        DateTime time = DateTime.now();
        //签发时间
        payload.put(JWTPayload.ISSUED_AT,time);
        //生效时间
        payload.put(JWTPayload.NOT_BEFORE,time);
        //过期时间
        payload.put(JWTPayload.EXPIRES_AT,time.offsetNew(DateField.DAY_OF_YEAR,validDays));
        //设置jti,是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击，默认使用uuid生成，也可由业务传入
        if(StrUtil.isBlank((String)payload.get(JWTPayload.JWT_ID))){
            payload.put(JWTPayload.JWT_ID, IdUtil.fastSimpleUUID());
        }
        return payload;
    }


    /**
     * 设置有效期
     * @param validDays
     * @return
     */
    private static Integer getValidDays(Integer validDays) {
        return ObjectUtil.defaultIfNull(validDays,DEFAULT_VALID_DAYS);
    }

    /**
     * 设置secret
     * @param secret
     * @return
     */
    private static JWTSigner getSecret(String secret){
        return JWTSignerUtil.hs256(ObjectUtil.defaultIfBlank(secret,DEFAULT_SECRET).getBytes(CharsetUtil.CHARSET_UTF_8));
    }

    /**
     * bean转map
     * @param wdPayload
     * @return
     */
    private static Map<String,Object> beanToMap(WdPayload wdPayload){
        BeanMap beanMap = BeanMap.create(wdPayload);
        Map<String,Object> map = new HashMap<>();
        beanMap.forEach((key,value)->{
            map.put(String.valueOf(key),value);
        });
        return map;
    }


    public static void main(String[] args) {
//        String secret = null;
//        for(int i=0;i<10;i++) {
//            String token = WdJwtUtil.createToken(new WdPayload(1L, 1L, 1L), 30, secret);
//            log.info("token {}:{}",i, token);
//            Boolean verify = WdJwtUtil.verify(token, secret);
//            log.info("token verify {}:{}",i, verify);
//            Boolean verifyValidate = WdJwtUtil.verifyDate(token, secret);
//            log.info("token verify validate {}:{}",i, verifyValidate);
//            WdPayload wdPayload = WdJwtUtil.getWdPayloadAndVerify(token, secret);
//            log.info("token decode payload {}:{}",i, wdPayload);
//            secret = IdUtil.fastSimpleUUID();
//        }
//        WdPayload wdPayload1 = new WdPayload(1L, 1L, 1L);
//        wdPayload1.setJti("123");
//        String token = WdJwtUtil.createToken(wdPayload1, 30, secret);
//        log.info("token {}:{}",111, token);
//        Boolean verify = WdJwtUtil.verify(token, secret);
//        log.info("token verify {}:{}",111, verify);
//        Boolean verifyValidate = WdJwtUtil.verifyDate(token, secret);
//        log.info("token verify validate {}:{}",111, verifyValidate);
//        WdPayload wdPayload = WdJwtUtil.getWdPayloadAndVerify(token, secret);
//        log.info("token decode payload {}:{}",111, wdPayload);

        WdPayload payload = WdJwtUtil.getWdPayload("gewgawegawetw",null);
        log.info("payload 数据加载:{}",payload);

    }

}
