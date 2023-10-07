package com.wakedata.common.mybatis.plus.enums;


/**
 * 自定义 Sql 方法枚举
 *
 * @author neexz
 */
public enum CustomSqlMethodEnum {
    /**
     * 批量插入
     */
    INSERT_BATCH("insertByBatch",
            "批量插入数据",
            "<script>\n"
                    + "INSERT INTO %s %s VALUES \n"
                    + "<foreach collection=\"collection\"  item=\"item\" separator=\",\"> %s\n </foreach>\n"
                    + "</script>");

    private final String method;
    private final String desc;
    private final String sql;

    CustomSqlMethodEnum(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }

    public String getMethod() {
        return method;
    }

    public String getDesc() {
        return desc;
    }

    public String getSql() {
        return sql;
    }

}
