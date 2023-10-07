package com.wakedata.common.bizlog.core;

import com.google.common.collect.Lists;
import com.mzt.logapi.service.impl.DiffParseFunction;
import com.mzt.logapi.starter.annotation.DiffLogField;
import com.mzt.logapi.starter.configuration.LogRecordProperties;
import com.mzt.logapi.starter.diff.DefaultDiffItemsToLogContentService;
import de.danielbechler.diff.node.DiffNode;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * BizDiffItemsToLogContentService
 *
 * @author focus
 * @date 2022/10/9
 **/
public class BizDiffItemsToLogContentService extends DefaultDiffItemsToLogContentService {

    public BizDiffItemsToLogContentService(LogRecordProperties logRecordProperties) {
        super(logRecordProperties);
    }

    @Override
    public String getCollectionDiffLogContent(String filedLogName, DiffNode node, Object sourceObject, Object targetObject, String functionName) {
        //集合走单独的diff模板
        Collection<Object> sourceList = getListValue(node, sourceObject);
        Collection<Object> targetList = getListValue(node, targetObject);
        Collection<Object> addItemList = listSubtract(targetList, sourceList);
        Collection<Object> delItemList = listSubtract(sourceList, targetList);

        String listAddContent = listToContent(functionName, addItemList);
        String listDelContent = listToContent(functionName, delItemList);

        return getLogRecordProperties().formatList(filedLogName, listAddContent, listDelContent) +
                            retainList(filedLogName,sourceList,targetList);

    }

    private String retainList(String filedLogName, Collection<Object> sourceList, Collection<Object> targetList){

        Collection<Object> sources = new ArrayList<>(sourceList);
        Collection<Object> targets = new ArrayList<>(targetList);

        StringBuilder stringBuilder = new StringBuilder();
        Iterator sourceIt = sources.iterator();

        while (sourceIt.hasNext()){
            Object source = sourceIt.next();
            Iterator targetIt = targets.iterator();
            while (targetIt.hasNext()){
                Object target = targetIt.next();
                if(equals(source,target) && isContainsCompareId(source)){
                    stringBuilder.append(getCompareFieldAndValue(source))
                            .append(getBeanFactory().getBean(DiffParseFunction.class).diff(source,target))
                            .append(",");
                    targetIt.remove();
                }
            }
        }

        if(stringBuilder.length() > 0){
            String preStr = "【" +filedLogName + "】：内部的数据更新： {";
            stringBuilder.insert(0,preStr);

        }
       return stringBuilder.append("}").toString();

    }



    private Collection<Object> getListValue(DiffNode node, Object object) {
        Object fieldSourceValue = getFieldValue(node, object);
        //noinspection unchecked
        if (fieldSourceValue != null && fieldSourceValue.getClass().isArray()) {
            return new ArrayList<>(Arrays.asList((Object[]) fieldSourceValue));
        }
        return fieldSourceValue == null ? Lists.newArrayList() : (Collection<Object>) fieldSourceValue;
    }

    private Collection<Object> listSubtract(Collection<Object> minuend, Collection<Object> subTractor) {
        Collection<Object> addItemList = new ArrayList<>(minuend);
        Collection<Object> delItemList = new ArrayList<>(subTractor);

        Iterator sourceIt = addItemList.iterator();
        boolean removeFlag = false;

        while ( sourceIt.hasNext()){
            Object source = sourceIt.next();
            Iterator targetIt = delItemList.iterator();

            while (targetIt.hasNext()){
                Object target = targetIt.next();
                if(equals(source,target)){
                    removeFlag = true;
                }
            }

            if(removeFlag){
                sourceIt.remove();
                removeFlag = false;
            }
        }

        return addItemList;
    }

    /**
     * 两个对象之间的比较
     * @param source
     * @param target
     * @return
     */
    public boolean equals(Object source,Object target){

        if(!isContainsCompareId(source)){
            return source.equals(target);
        }

        Map<String,Object> sourceFieldValueMap = getCompareIdFieldValueMap(source);
        Map<String,Object> targetFieldValueMap = getCompareIdFieldValueMap(target);

        for (Map.Entry<String,Object> entry : sourceFieldValueMap.entrySet()){
            String key = entry.getKey();
            Object value = entry.getValue();

            if(!Objects.equals(value,targetFieldValueMap.get(key))){
                return false;
            }
        }

        return true;
    }

    private boolean isContainsCompareId(Object target){

        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            WKCompareID compareID = field.getAnnotation(WKCompareID.class);
            if(compareID != null){
                return true;
            }
        }
        return false;
    }

    private String getCompareFieldAndValue(Object target){
        Field[] fields = target.getClass().getDeclaredFields();
        StringBuilder stringBuilder = new StringBuilder();
        for (Field field : fields) {
            field.setAccessible(true);
            WKCompareID compareID = field.getAnnotation(WKCompareID.class);
            if(compareID == null){
                continue;
            }

            Object obj = null;
            try {
                obj = field.get(target);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            DiffLogField diffLogField = field.getAnnotation(DiffLogField.class);
            String compareKey = diffLogField == null ?  field.getName() : diffLogField.name();
            stringBuilder.append(compareKey).append("=").append(obj).append(",");

        }
        if(stringBuilder.length() > 0){
            stringBuilder.insert(0,"[").append("]").append(":\t");
        }
        return stringBuilder.toString();
    }

    /**
     * 获取存在比较唯一ID的注解的对象
     * @param source
     * @return
     */
    private Map<String,Object> getCompareIdFieldValueMap(Object source){

        Map<String,Object> fieldValueMap = new HashMap<>();

        Field[] fields = source.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            WKCompareID compareID = field.getAnnotation(WKCompareID.class);
            if(compareID != null){
                try {
                    fieldValueMap.put(field.getName(),field.get(source));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return fieldValueMap;
    }

    private String listToContent(String functionName, Collection<Object> addItemList) {
        StringBuilder listAddContent = new StringBuilder();
        if (!CollectionUtils.isEmpty(addItemList)) {
            for (Object item : addItemList) {
                listAddContent.append(getFunctionValue(item, functionName)).append(getLogRecordProperties().getListItemSeparator());
            }
        }
        return listAddContent.toString().replaceAll(getLogRecordProperties().getListItemSeparator() + "$", "");
    }

    private String getFunctionValue(Object canonicalGet, String functionName) {
        if (StringUtils.isEmpty(functionName)) {
            return canonicalGet.toString();
        }
        return getFunctionService().apply(functionName, canonicalGet.toString());
    }

    private Object getFieldValue(DiffNode node, Object o2) {
        return node.canonicalGet(o2);
    }

}
