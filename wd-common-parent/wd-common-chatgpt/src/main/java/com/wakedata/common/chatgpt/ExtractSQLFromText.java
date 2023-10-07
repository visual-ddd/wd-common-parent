package com.wakedata.common.chatgpt;

import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * sql内容提取
 * @author luomeng
 * @date 2023/4/6 15:01
 */
public class ExtractSQLFromText {

    /**
     * sql解析规则
     */
    private static Pattern SQL_PARSE_RULE = Pattern.compile("(?i)(SELECT|INSERT|UPDATE|DELETE|CREATE|DROP|ALTER|GRANT|REVOKE|SHOW)\\s.+?;");

    /**
     * mongodb解析规则
     */
    private static Pattern MONGODB_PARSE_RULE = Pattern.compile("(?i)(db\\.[a-zA-Z]+.*?\\);)");

    /**
     * 解析sql
     * @param text
     * @return
     */
    public static List<String> parseSQLFromText(String text){
        List<String> list = new ArrayList<>();
        Matcher sqlMatcher = SQL_PARSE_RULE.matcher(text);
        while (sqlMatcher.find()){
            list.add(sqlMatcher.group().trim());
        }
        return list;
    }


    /**
     * 解析mongodb语法
     * @param text
     * @return
     */
    public static List<String> parseMongoDbFromText(String text){
        List<String> list = new ArrayList<>();
        Matcher sqlMatcher = MONGODB_PARSE_RULE.matcher(text);
        while (sqlMatcher.find()){
            list.add(sqlMatcher.group().trim());
        }
        return list;
    }

    public static void main(String[] args) {
        String inputText = "This is a sample text. db.getCollection('org_attribute').find({}, {id:1, attr_name:1, attr_code:1}).sort({id:-1}).limit(10).skip(5); " +
                "Another sample text. db.users.find({ \"name\": \"John\" }).sort({ \"age\": -1 }).limit(10); " +
                "Some more text. db.getCollection('orders').find({ status: \"shipped\" });";
        List<String> strings = parseMongoDbFromText(inputText);
        System.out.println("strings = " + JSONUtil.toJsonPrettyStr(strings));
    }


}
