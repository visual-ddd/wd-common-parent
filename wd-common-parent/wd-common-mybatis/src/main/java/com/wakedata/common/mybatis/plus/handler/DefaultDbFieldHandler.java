package com.wakedata.common.mybatis.plus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.wakedata.common.core.context.BaseUserInfo;
import com.wakedata.common.core.context.UserInfoContext;
import com.wakedata.common.core.context.UserInfoWrapper;
import com.wakedata.common.mybatis.plus.po.BaseConstants;
import com.wakedata.common.mybatis.plus.po.BaseDO;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

/**
 * 通用参数填充实现类 如果没有显式的对通用参数进行赋值，这里会对通用参数进行填充、赋值
 *
 * @author neexz
 * @date 2021/05/28
 */
@Slf4j
public class DefaultDbFieldHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {

        if (Objects.nonNull(metaObject)) {

            if (metaObject.getOriginalObject() instanceof BaseDO) {
                BaseDO baseDO = (BaseDO) metaObject.getOriginalObject();

                LocalDateTime current = LocalDateTime.now();
                // 创建时间为空，则以当前时间为插入时间
                if (Objects.isNull(baseDO.getCreateTime())) {
                    setFieldValByName(BaseConstants.CREATE_TIME, current, metaObject);
                }

                // 更新时间为空，则以当前时间为更新时间
                if (Objects.isNull(baseDO.getUpdateTime())) {
                    setFieldValByName(BaseConstants.UPDATE_TIME, current, metaObject);
                }
                BaseUserInfo currentUser = getUser();
                // 当前登录用户不为空，创建人为空，则当前登录用户为创建人
                if (Objects.nonNull(currentUser) && Objects.isNull(baseDO.getCreateBy())) {
                    setFieldValByName(BaseConstants.CREATE_BY, currentUser.getNickName(), metaObject);
                }

                // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
                if (Objects.nonNull(currentUser) && Objects.isNull(baseDO.getUpdateBy())) {
                    setFieldValByName(BaseConstants.UPDATE_BY, currentUser.getNickName(), metaObject);
                }
                // buId tenantId
                Object buId = getFieldValByName(BaseConstants.BUSINESS_UNIT_ID, metaObject);
                if (Objects.nonNull(currentUser) && Objects.isNull(buId)) {
                    setFieldValByName(BaseConstants.BUSINESS_UNIT_ID, currentUser.getBuId(), metaObject);
                }
                Object tenantId = getFieldValByName(BaseConstants.TENANT_ID, metaObject);
                if (Objects.nonNull(currentUser) && Objects.isNull(tenantId)) {
                    setFieldValByName(BaseConstants.TENANT_ID, currentUser.getTenantId(), metaObject);
                }
                Object version = getFieldValByName(BaseConstants.VERSION, metaObject);
                if (Objects.isNull(version)) {
                    setFieldValByName(BaseConstants.VERSION, 0, metaObject);
                }
                Object deleted = getFieldValByName(BaseConstants.DELETED, metaObject);
                if (Objects.isNull(deleted)) {
                    setFieldValByName(BaseConstants.DELETED, Boolean.FALSE, metaObject);
                }
                //set appBuId
                Object appBuId = getFieldValByName(BaseConstants.APP_BU_ID, metaObject);
                if (Objects.nonNull(currentUser) && Objects.isNull(appBuId)) {
                    setFieldValByName(BaseConstants.APP_BU_ID, currentUser.getAppBuId(), metaObject);
                }
                Object createName = getFieldValByName(BaseConstants.CREATE_NAME, metaObject);
                if (Objects.nonNull(currentUser) && Objects.isNull(createName)) {
                    setFieldValByName(BaseConstants.CREATE_NAME, currentUser.getNickName(), metaObject);
                }
                // 当前登录用户不为空，更新人名称为空，则当前登录用户为更新人名称
                Object updateName = getFieldValByName(BaseConstants.UPDATE_NAME, metaObject);
                if (Objects.nonNull(currentUser) && Objects.isNull(updateName)) {
                    setFieldValByName(BaseConstants.UPDATE_NAME, currentUser.getNickName(), metaObject);
                }
            }

        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {

        if (Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseDO) {
            // 更新时间为空，则以当前时间为更新时间
            Object modifyTime = getFieldValByName(BaseConstants.UPDATE_TIME, metaObject);
            if (Objects.isNull(modifyTime)) {
                setFieldValByName(BaseConstants.UPDATE_TIME, LocalDateTime.now(), metaObject);
            }

            // 当前登录用户不为空，更新人为空，则当前登录用户为更新人
            Object updateBy = getFieldValByName(BaseConstants.UPDATE_BY, metaObject);
            BaseUserInfo currentUser = getUser();
            if (Objects.isNull(updateBy) && Objects.nonNull(currentUser)) {
                setFieldValByName(BaseConstants.UPDATE_BY, currentUser.getNickName(), metaObject);
            }

            // 当前登录用户不为空，更新人名称为空，则当前登录用户为更新人名称
            Object updateName = getFieldValByName(BaseConstants.UPDATE_NAME, metaObject);
            if (Objects.nonNull(currentUser) && Objects.isNull(updateName)) {
                setFieldValByName(BaseConstants.UPDATE_NAME, currentUser.getNickName(), metaObject);
            }
        }

    }

    private BaseUserInfo getUser() {
        BaseUserInfo currentUser = UserInfoContext.getUser();
        if (Objects.isNull(currentUser)) {
            log.warn("UserInfoContext get current user is null");
            currentUser = new UserInfoWrapper();
            currentUser.setBuId(0L);
            currentUser.setNickName("system");
            currentUser.setUserId("0");
            currentUser.setAppBuId(0L);
            currentUser.setTenantId(0L);
            return currentUser;
        }
        return currentUser;
    }
}
