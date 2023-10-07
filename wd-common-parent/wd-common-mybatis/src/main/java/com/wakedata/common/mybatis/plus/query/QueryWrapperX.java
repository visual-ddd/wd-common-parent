package com.wakedata.common.mybatis.plus.query;


import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.pagehelper.util.StringUtil;
import com.wakedata.common.core.exception.BizException;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 *  * 拓展 MyBatis Plus QueryWrapper，LambdaQueryWrapper 类，可通过lambda表达式来替换表字段。增加如下功能，具体测试用例{com.wakedata.common.mybatis.TestQueryWrapperX}
 * 1. 拼接条件的方法，增加 xxxIfPresent 方法，用于判断值不存在的时候，不拼接到条件中。
 * 2、可以使用lambda表达式判空，如 xxxIfPresent(SFunction<T, ?> column, Object val)
 * 3、一些特殊函数或语法，支持使用占位符的方式（'?'占位符），如 wrapperX.eqIfPresent("DATE_FORMAT(?, '%Y-%m-%d')", UserInfoDO::getCreateTime, day);
 * @Author zkz
 * @Date 2022/1/17
 */
public class QueryWrapperX<T> extends AbstractLambdaWrapper<T, QueryWrapperX<T>> implements Query<QueryWrapperX<T>, T, SFunction<T, ?>> {
    private SharedString sqlSelect;

    public final static String PLACEHOLDER = "\\?";

    public QueryWrapperX() {
        this((T) null);
    }

    public QueryWrapperX(T entity) {
        this.sqlSelect = new SharedString();
        super.setEntity(entity);
        super.initNeed();
    }

    public QueryWrapperX(Class<T> entityClass) {
        this.sqlSelect = new SharedString();
        super.setEntityClass(entityClass);
        super.initNeed();
    }

    QueryWrapperX(T entity, Class<T> entityClass, SharedString sqlSelect, AtomicInteger paramNameSeq, Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments, SharedString lastSql, SharedString sqlComment, SharedString sqlFirst) {
        this.sqlSelect = new SharedString();
        super.setEntity(entity);
        super.setEntityClass(entityClass);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.sqlSelect = sqlSelect;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
    }
    @Override
    @SafeVarargs
    public final QueryWrapperX<T> select(SFunction<T, ?>... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            this.sqlSelect.setStringValue(this.columnsToString(false, columns));
        }

        return this.typedThis;
    }
    @Override
    public QueryWrapperX<T> select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
        if (entityClass == null) {
            entityClass = this.getEntityClass();
        } else {
            this.setEntityClass(entityClass);
        }

        Assert.notNull(entityClass, "entityClass can not be null", new Object[0]);
        this.sqlSelect.setStringValue(TableInfoHelper.getTableInfo(entityClass).chooseSelect(predicate));
        return this.typedThis;
    }
    @Override
    public String getSqlSelect() {
        return this.sqlSelect.getStringValue();
    }
    @Override
    protected QueryWrapperX<T> instance() {
        return new QueryWrapperX(this.getEntity(), this.getEntityClass(), (SharedString)null, this.paramNameSeq, this.paramNameValuePairs, new MergeSegments(), SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString());
    }

    @Override
    public void clear() {
        super.clear();
        this.sqlSelect.toNull();
    }
    // ==========非Lambda方法==========================================


    public QueryWrapperX<T> likeIfPresent(String column, String val) {
        if (StringUtils.hasText(val)) {
            return this.queryX().like(column, val).lambda();
        }
        return this;
    }

    public QueryWrapperX<T> inIfPresent(String column, Collection<?> values) {
        if (!CollectionUtils.isEmpty(values)) {
            return  this.queryX().in(column, values).lambda();
        }
        return this;
    }

    public QueryWrapperX<T> inIfPresent(String column, Object... values) {
        if (!ArrayUtils.isEmpty(values)) {
            return  this.queryX().in(column, values).lambda();
        }
        return this;
    }

    public QueryWrapperX<T> eqIfPresent(String column, Object val) {
        if (val != null) {
            return  this.queryX().eq(column, val).lambda();
        }
        return this;
    }

    public QueryWrapperX<T> gtIfPresent(String column, Object val) {
        if (val != null) {
            return  this.queryX().gt(column, val).lambda();
        }
        return this;
    }

    public QueryWrapperX<T> betweenIfPresent(String column, Object val1, Object val2) {
        if (val1 != null && val2 != null) {
            return this.queryX().between(column, val1, val2).lambda();
        }
        if (val1 != null) {
            return this.queryX().ge(column, val1).lambda();
        }
        if (val2 != null) {
            return this.queryX().le(column, val2).lambda();
        }
        return this;
    }



    public QueryWrapperX<T> eq(boolean condition, String column, Object val) {
        this.queryX().eq(condition, column, val);
        return this;
    }

    public QueryWrapperX<T> eq(String column, Object val) {
        this.queryX().eq(column, val);
        return this;
    }


    public QueryWrapperX<T> orderByDesc(String column) {
        this.queryX().orderByDesc(true, column);
        return this;
    }

    @Override
    public QueryWrapperX<T> last(String lastSql) {
        super.last(lastSql);
        return this;
    }

//==========非Lambda方法结束==========================================


    public QueryWrapperX<T> likeIfPresent(SFunction<T, ?> column, Object val) {
        return !ObjectUtils.isEmpty(val) ? super.like(column, val) : this;
    }

    public QueryWrapperX<T> inIfPresent(SFunction<T, ?> column, Collection<?> values) {
        return !CollectionUtils.isEmpty(values) ? super.in(column, values) : this;
    }

    public QueryWrapperX<T> eqIfPresent(SFunction<T, ?> column, Object val) {
        return !ObjectUtils.isEmpty(val) ? super.eq(column, val) : this;
    }

    public QueryWrapperX<T> gtIfPresent(SFunction<T, ?> column, Object val) {
        return !ObjectUtils.isEmpty(val) ? super.gt(column, val) : this;
    }
    public QueryWrapperX<T> geIfPresent(SFunction<T, ?> column, Object val) {
        return !ObjectUtils.isEmpty(val) ? super.ge(column, val) : this;
    }
    public QueryWrapperX<T> ltIfPresent(SFunction<T, ?> column, Object val) {
        return !ObjectUtils.isEmpty(val) ? super.lt(column, val) : this;
    }
    public QueryWrapperX<T> leIfPresent(SFunction<T, ?> column, Object val) {
        return !ObjectUtils.isEmpty(val) ? super.le(column, val) : this;
    }
    public QueryWrapperX<T> betweenIfPresent(SFunction<T, ?> column, Object val1, Object val2) {
        if (val1 != null && val2 != null) {
            return super.between(column, val1, val2);
        } else if (val1 != null) {
            return this.ge(column, val1);
        } else {
            return val2 != null ? this.le(column, val2) : this;
        }
    }



    public QueryWrapperX<T> likeIfPresent(String sql, SFunction<T, ?> column, Object val) {
        return !ObjectUtils.isEmpty(val) ? this.like(sql, column, val) : this;
    }

    public QueryWrapperX<T> inIfPresent(String sql, SFunction<T, ?> column, Collection<?> values) {
        return !CollectionUtils.isEmpty(values) ? this.in(sql, column, values) : this;
    }

    public QueryWrapperX<T> eqIfPresent(String sql, SFunction<T, ?> column, Object val) {
        return !ObjectUtils.isEmpty(val) ? this.eq(sql, column, val) : this;
    }

    public QueryWrapperX<T> gtIfPresent(String sql, SFunction<T, ?> column, Object val) {
        return !ObjectUtils.isEmpty(val) ? this.gt(sql, column, val) : this;
    }

    public QueryWrapperX<T> geIfPresent(String sql, SFunction<T, ?> column, Object val) {
        return !ObjectUtils.isEmpty(val) ? this.ge(sql, column, val) : this;
    }

    public QueryWrapperX<T> ltIfPresent(String sql, SFunction<T, ?> column, Object val) {
        return !ObjectUtils.isEmpty(val) ? this.lt(sql, column, val) : this;
    }

    public QueryWrapperX<T> leIfPresent(String sql, SFunction<T, ?> column, Object val) {
        return !ObjectUtils.isEmpty(val) ? this.le(sql, column, val) : this;
    }
    public QueryWrapperX<T> betweenIfPresent(String sql, SFunction<T, ?> column, Object val1, Object val2) {
        if (val1 != null && val2 != null) {
            return this.between(sql, column, val1, val2);
        } else if (val1 != null) {
            return this.ge(sql, column, val1);
        } else {
            return val2 != null ? this.le(sql, column, val2) : this;
        }
    }


    /**
     * 按顺序将字段替换占位符，PLACEHOLDER = "?"替换
     * @param sql SQL脚本段，包含占位符PLACEHOLDER="?"
     * @param column 需要替换的字段
     * @return
     */
    public QueryWrapperX<T> eq(String sql, SFunction<T, ?> column, Object val) {
        return this.queryX().eq(handleSQL(sql, column), val).lambda();
    }

    public QueryWrapperX<T> le(String sql, SFunction<T, ?> column, Object val) {
        return this.queryX().le(handleSQL(sql, column), val).lambda();
    }
    public QueryWrapperX<T> lt(String sql, SFunction<T, ?> column, Object val) {
        return this.queryX().lt(handleSQL(sql, column), val).lambda();
    }

    public QueryWrapperX<T> ge(String sql, SFunction<T, ?> column, Object val) {
        return this.queryX().ge(handleSQL(sql, column), val).lambda();
    }
    public QueryWrapperX<T> gt(String sql, SFunction<T, ?> column, Object val) {
        return this.queryX().gt(handleSQL(sql, column), val).lambda();
    }

    public QueryWrapperX<T> in(String sql, SFunction<T, ?> column, Object val) {
        return this.queryX().in(handleSQL(sql, column), val).lambda();
    }
    public QueryWrapperX<T> like(String sql, SFunction<T, ?> column, Object val) {
        return this.queryX().like(handleSQL(sql, column), val).lambda();
    }
    public QueryWrapperX<T> between(String sql, SFunction<T, ?> column, Object val1, Object val2) {
        return this.queryX().between(handleSQL(sql, column), val1, val2).lambda();
    }

    public QueryWrapperX<T> select(String sql, SFunction<T, ?>... columns) {
        return this.select(handleSQL(sql, columns));
    }
    public QueryWrapperX<T> select(String sql) {
        return this.queryX().select(sql).lambda();
    }
    public QueryWrapperX<T> groupBy(String sql, SFunction<T, ?>... columns) {
        return this.groupBy(handleSQL(sql, columns));
    }
    public QueryWrapperX<T> groupBy(String sql) {
        return this.queryX().groupBy(sql).lambda();
    }

    private String handleSQL(String sql, SFunction<T, ?>... columns) {
        if (ArrayUtils.isEmpty(columns) || StringUtil.isEmpty(sql)) {
            throw new BizException("数据过滤参数不正确");
        }
        try {
            for (SFunction<T, ?> x : columns) {
                sql = sql.replaceFirst(PLACEHOLDER, this.columnsToString(x));
            }
        }catch (Exception e) {
            throw new BizException("参数不正确");
        }

        return sql;
    }

    private MyQueryWrapper<T> queryX() {
        return new MyQueryWrapper(this.getEntity(), this.getEntityClass(), this.paramNameSeq, this.paramNameValuePairs,
                this.expression, this.lastSql, this.sqlComment, this.sqlFirst, this.sqlSelect);
    }

}

class MyQueryWrapper<T> extends AbstractWrapper<T, String, MyQueryWrapper<T>> implements Query<MyQueryWrapper<T>, T, String> {
    private final SharedString sqlSelect;

    public MyQueryWrapper() {
        this((T)null);
    }

    public MyQueryWrapper(T entity) {
        this.sqlSelect = new SharedString();
        super.setEntity(entity);
        super.initNeed();
    }

    public MyQueryWrapper(T entity, String... columns) {
        this.sqlSelect = new SharedString();
        super.setEntity(entity);
        super.initNeed();
        this.select(columns);
    }

    public MyQueryWrapper(T entity, Class<T> entityClass, AtomicInteger paramNameSeq, Map<String, Object> paramNameValuePairs,
                          MergeSegments mergeSegments, SharedString lastSql, SharedString sqlComment, SharedString sqlFirst, SharedString sqlSelect) {
        this.sqlSelect = sqlSelect;
        super.setEntity(entity);
        super.setEntityClass(entityClass);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
    }
    @Override
    public MyQueryWrapper<T> select(String... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            this.sqlSelect.setStringValue(String.join(",", columns));
        }

        return this.typedThis;
    }
    @Override
    public MyQueryWrapper<T> select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
        super.setEntityClass(entityClass);
        this.sqlSelect.setStringValue(TableInfoHelper.getTableInfo(this.getEntityClass()).chooseSelect(predicate));
        return this.typedThis;
    }
    @Override
    public String getSqlSelect() {
        return this.sqlSelect.getStringValue();
    }

    public QueryWrapperX lambda() {
        return new QueryWrapperX(this.getEntity(), this.getEntityClass(), this.sqlSelect, this.paramNameSeq, this.paramNameValuePairs, this.expression, this.lastSql, this.sqlComment, this.sqlFirst);
    }
    @Override
    public MyQueryWrapper<T> instance() {
        return new MyQueryWrapper(this.getEntity(), this.getEntityClass(), this.paramNameSeq, this.paramNameValuePairs,
                new MergeSegments(), SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString(), new SharedString());
    }
    @Override
    public void clear() {
        super.clear();
        this.sqlSelect.toNull();
    }
}
