package com.wakedata.common.core.context;

import lombok.experimental.UtilityClass;

/**
 * UserinfoWrapper 其他用户属性常量
 */
@UtilityClass
public class OtherUserProperties {

    /**
     * 应用唯一标识(String)
     */
    public static final String APP_KEY_FLAG = "appKeyFlag";

    /**
     * 选择应用的节点id(Long)
     */
    public static final String APP_NODE_ID = "appNodeId";

    /**
     * 选择应用的上级节点id(Long)
     */
    public static final String APP_PARENT_NODE_ID = "appParentNodeId";

    /**
     * 选择应用的主营行业/行业分类ID(Long)
     */
    public static final String APP_INDUSTRY_CATEGORY_ID = "appIndustryCategoryId";

    /**
     *  所属组织节点id列表(Long)
     */
    public static final String BELONG_NODE_IDS = "belongNodeIds";

    /**
     * 用户当前的拥有的叶子业务单元id列表
     */
    public static final String LEAF_BU_ID_LIST = "leafBuIdList";

    /**
     * 微信认证信息Key
     */
    public static final String WX_AUTH_INFO = "wxAuthInfo";

    /**
     * 角色等级
     */
    public static final String ROLE_LEVEL = "roleLevelList";


    /**
     * 是否开启数据权限过滤，Boolean类型 true为开启
     */
    public static final String DATA_SCOPE_IS_OPEN = "data_scope_is_open";

    /**
     * 适用范围数据buIds
     */
    public static final String DATA_SCOPE_APPLY_BUIDS = "data_scope_apply_buids";

    /**
     * 来源范围数据buIds
     */
    public static final String DATA_SCOPE_SOURCE_BUIDS = "data_scope_source_buids";

    /**
     * 适用范围过滤配置片段
     */
    public static final String DATA_SCOPE_APPLY_FILTER_KEY = "data_scope_apply_filter_key";

    /**
     * 来源范围过滤配置片段
     */
    public static final String DATA_SCOPE_SOURCE_FILTER_KEY = "data_scope_source_buids_filter_key";
}
