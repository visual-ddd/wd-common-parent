package com.wakedata.common.core.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zhuhao
 */
public class JsonUtil {

    /**
     * can reuse, share
     */
    private static ObjectMapper mapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);


    /**
     * 将对象转成json.
     *
     * @param obj 对象
     * @return json字符串
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            String str = mapper.writeValueAsString(obj);
            return str;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 类型转换.
     *
     * @param obj   java对象
     * @param clazz 类型西
     * @return java对象转化的特对类对象
     */
    public static <T> T convert(Object obj, Class<T> clazz) {
        String json = toJson(obj);
        return toObject(json, clazz);
    }


    /**
     * 将Json转换成对象.
     *
     * @param json  json字符串
     * @param clazz 类信息
     * @return 特定类对象
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            logger.error("message:" + e.getMessage() + " json:" + json);
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    @SuppressWarnings("unchecked")
    public static <T> Map<String, T> toMap(String json,
                                           Class<T> clazz) {
        return toObject(json, Map.class);
    }


    @SuppressWarnings("unchecked")
    public static <T> Set<T> toSet(String json, Class<T> clazz) {
        return toObject(json, Set.class);
    }


    /**
     * json转List.
     *
     * @param content   json数据
     * @param valueType 泛型数据类型
     * @return 对象数组
     */
    @SuppressWarnings("deprecation")
    public static <T> List<T> toListObject(String content, Class<T> valueType) {
        if (content == null || content.length() == 0) {
            return Collections.emptyList();
        }
        try {
            return mapper.readValue(content, TypeFactory.defaultInstance().constructCollectionType(List.class, valueType));
        } catch (Exception e) {
            logger.error("message:" + e.getMessage() + " content:" + content);
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 将Json转换成对象. 仅供sync-web使用
     *
     * @param json  json字符串
     * @param clazz 类信息
     * @return json字符串转化的类对象
     */
    public static <T> T toObjectWithoutCtrlChars(String json, Class<T> clazz) {
        if (json == null || json.length() == 0) {
            return null;
        }
        try {
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            mapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            logger.error("message:" + e.getMessage() + " json:" + json);
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * json转List. 会处理特殊字符
     *
     * @param content   json数据
     * @param valueType 泛型数据类型
     * @return 对象数组
     */
    @SuppressWarnings("deprecation")
    public static <T> List<T> toListObjectWithoutCtrlChars(
            String content, Class<T> valueType) {
        if (content == null || content.length() == 0) {
            return Collections.emptyList();
        }
        try {
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            mapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
            return mapper.readValue(content, TypeFactory.defaultInstance().constructCollectionType(List.class, valueType));
        } catch (Exception e) {
            logger.error("message:" + e.getMessage() + " content:" + content);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        String json = "{\"a\":1,\"b\":2}";
        Map map = JsonUtil.toMap(json, Object.class);
        System.out.println(map);
    }
}
