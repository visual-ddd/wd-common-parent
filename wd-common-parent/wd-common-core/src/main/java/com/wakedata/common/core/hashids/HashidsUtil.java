package com.wakedata.common.core.hashids;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.BeanProperty;
import com.wakedata.common.core.exception.BizException;
import com.wakedata.common.core.hashids.annotation.HashidsConvert;
import lombok.extern.slf4j.Slf4j;
import org.hashids.Hashids;

import java.util.*;

/**
 * @author luomeng
 * @Description 将数字转换成短的、唯一的、可解码的哈希字符串
 * @createTime 2022-05-12 16:28:00
 */
@Slf4j
public class HashidsUtil {

    /**
     * 填充加密盐值
     *
     * @param salt 加密盐值
     * @return
     */
    private static String fillSalt(String salt) {
        if (StrUtil.isEmpty(salt)) {
            salt = HashidsConstant.DEFAULT_SALT;
        }
        return String.join(HashidsConstant.DEFAULT_JOIN, HashidsConstant.DEFAULT_SALT_PREFIX, salt);
    }

    /**
     * 填充最小hash长度
     *
     * @param minHashLength 最小长度
     * @return
     */
    private static int fillMinHashLength(Integer minHashLength) {
        if (Objects.nonNull(minHashLength)) {
            return minHashLength;
        }
        return HashidsConstant.DEFAULT_MIN_HASH_LENGTH;
    }

    /**
     * id编码，转成字符串，使用默认的加盐值和最小长度
     *
     * @param id 正整数，需要转换的值
     * @return
     * @throws BizException id超限制会抛异常
     */
    public static String encodeDefault(long id) throws BizException{
        return encode(id, HashidsConstant.DEFAULT_SALT, HashidsConstant.DEFAULT_MIN_HASH_LENGTH);
    }

    /**
     * hash解码，转成id，使用默认的加盐值和最小长度
     *
     * @param hash 需要转换的字符串
     * @return
     * @throws BizException 解码失败时会抛出异常
     */
    public static long decodeDefault(String hash) throws BizException {
        return decode(hash, HashidsConstant.DEFAULT_SALT, HashidsConstant.DEFAULT_MIN_HASH_LENGTH);
    }


    /**
     * id编码，转成hash串
     *
     * @param id            正整数
     * @param salt          加密时候加的盐值，不填用默认的，解码需一样
     * @param minHashLength 转换生成的最小hash串长度，为空使用默认设置
     * @return
     * @throws BizException id超限制会抛异常,目前限制最大为9007199254740992L
     */
    public static String encode(long id, String salt, Integer minHashLength) throws BizException{
        salt = fillSalt(salt);
        minHashLength = fillMinHashLength(minHashLength);
        try {
            return new Hashids(salt, minHashLength).encode(id);
        }catch (IllegalArgumentException e){
            log.error("id convert hash encode fail：{}",e.getMessage());
            throw new BizException(e.getMessage());
        }
    }


    /**
     * hash解码，转成id
     *
     * @param hash          hash串
     * @param salt          加密时候加的盐值，不填用默认的，需和加密一样
     * @param minHashLength 转换生成的最小hash串长度，为空使用默认设置，需和加密时一样
     * @return
     * @throws BizException 解码失败时会抛出异常
     */
    public static long decode(String hash, String salt, Integer minHashLength) throws BizException {
        salt = fillSalt(salt);
        minHashLength = fillMinHashLength(minHashLength);
        long[] decode = new Hashids(salt, minHashLength).decode(hash);
        if (decode.length > 0) {
            return decode[0];
        }
        log.error("hash convert id decode fail：{}",hash);
        throw new BizException(String.format("[%s] Decoding failure", hash));
    }

    /**
     * id十六进制编码，转成hash串
     * @param id 可为objectid ，id超过9007199254740992L时可用该方法编码,不区分大小写
     * @param salt 加密时候加的盐值，不填用默认的，需和加密一样
     * @return
     */
    public static String encodeHex(String id,String salt){
        salt = fillSalt(salt);
        return new Hashids(salt).encodeHex(id);
    }

    /**
     * hash解码，仅支持通过encodehex编码的数据解码
     * @param hash hash串
     * @param salt 加密时候加的盐值，不填用默认的，需和加密一样
     * @return
     * @throws BizException
     */
    public static String decodeHex(String hash, String salt) throws BizException{

        salt = fillSalt(salt);
        String decode = new Hashids(salt).decodeHex(hash);
        if(StrUtil.isBlank(decode)){
            throw new BizException(String.format("[%s] Decoding HEX failure", hash));
        }
        return decode;
    }

    /**
     * 检查属性配置，只对number类型做处理
     * @param rawClass 类型
     * @return 是否匹配
     */
    public static boolean checkProperty(Class<?> rawClass) {
        if (Objects.equals(rawClass, Long.class)
                || Objects.equals(rawClass, Integer.class)
                || Objects.equals(rawClass,long.class)
                || Objects.equals(rawClass,int.class)) {
            return true;
        }
        return false;
    }

    /**
     * 获取注解
     * @param beanProperty
     * @return
     */
    public static HashidsConvert getHashidsConvertAnnotation(BeanProperty beanProperty) {
        HashidsConvert annotation = beanProperty.getAnnotation(HashidsConvert.class);
        if (annotation == null) {
            annotation = beanProperty.getContextAnnotation(HashidsConvert.class);
        }
        return annotation;
    }


    /**
     * 根据指定类型做转换
     * @param cls 需要转换的类型
     * @param id 转换对象
     * @return 对应类型的值
     */
    public static Object convertId(Class<?> cls,Long id) {
        if (Objects.equals(cls, Integer.class)) {
            return id.intValue();
        }
        if(Objects.equals(cls,int.class)){
            return id.intValue();
        }
        return id;
    }
}
