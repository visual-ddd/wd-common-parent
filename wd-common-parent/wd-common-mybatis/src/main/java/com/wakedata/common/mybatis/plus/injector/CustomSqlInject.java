package com.wakedata.common.mybatis.plus.injector;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.AlwaysUpdateSomeColumnById;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import com.wakedata.common.mybatis.plus.method.InsertByBatch;

import java.util.List;

/**
 * 自定义 Sql 注入器，继承 MybatisPlus 提供的默认注入器
 * 使用方需要将这个注入器添加到Spring容器中
 *
 * @author neexz
 */
public class CustomSqlInject extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        //批量插入
        methodList.add(new InsertByBatch("insertByBatch"));
        //支持空字段更新
        methodList.add(new AlwaysUpdateSomeColumnById("fullUpdateById", i -> i.getFieldFill() != FieldFill.INSERT));
        return methodList;
    }
}
