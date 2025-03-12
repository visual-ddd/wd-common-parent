package com.df.common.mybatis;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 砍价活动
 * @TableName mt_bargain_activity
 */
@TableName(value ="mt_bargain_activity")
@Data
public class BargainActivityDO implements Serializable {
    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 活动ID，营销活动唯一
     */
    private Long activityNo;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 业务单元ID
     */
    private Long appBuId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 适用门店类型 0适用所有 1部分有效
     */
    private Boolean suitStoreType;

    /**
     * 参与活动的门店列表
     */
    private Object suitStoreIds;

    /**
     * 限制商品数量类型 0：不限制， 1：所用门店共有 2：按门店配额
     */
    private Integer saleLimitType;

    /**
     * 活动限制数量, SALE_LIMIT_TYPE = 1 时生效
     */
    private Integer saleLimitNum;

    /**
     * 活动剩余数量, SALE_LIMIT_TYPE = 1 时生效
     */
    private Integer saleLimitRemain;

    /**
     * json,门店配额数量&剩余数量, SALE_LIMIT_TYPE = 2 时生效
     */
    private Object saleStoreLimit;

    /**
     * 商品原价
     */
    private Long salePrice;

    /**
     * 可砍最低价
     */
    private Long lowestPrice;

    /**
     * 是否最低价购买（0：否 1：是）
     */
    private Boolean lowestPriceBuy;

    /**
     * 活动开始时间
     */
    private Date startTime;

    /**
     * 活动结束时间
     */
    private Date endTime;

    /**
     * 砍价超时时间 （单位 秒）
     */
    private Long bargainDuration;

    /**
     * 参与砍价人数
     */
    private Integer bargainPeople;

    /**
     * 虚拟参与人数
     */
    private Integer virtualPeople;

    /**
     * 活动状态 （-1:删除 0：未开始 1：进行中 2：已结束）
     */
    private Byte activityStatus;

    /**
     * 发起砍价的次数限制（周期）
     */
    private Integer bargainTimesPeriod;

    /**
     * 发起砍价的次数限制（每天）
     */
    private Integer bargainTimesDay;

    /**
     * 帮砍的次数限制（周期）
     */
    private Integer assistTimesPeriod;

    /**
     * 帮砍的次数限制（每天）
     */
    private Integer assistTimesDay;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者用户
     */
    private String createBy;

    /**
     * 更新者用户
     */
    private String updateBy;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        BargainActivityDO other = (BargainActivityDO) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getActivityNo() == null ? other.getActivityNo() == null : this.getActivityNo().equals(other.getActivityNo()))
            && (this.getTenantId() == null ? other.getTenantId() == null : this.getTenantId().equals(other.getTenantId()))
            && (this.getAppBuId() == null ? other.getAppBuId() == null : this.getAppBuId().equals(other.getAppBuId()))
            && (this.getActivityName() == null ? other.getActivityName() == null : this.getActivityName().equals(other.getActivityName()))
            && (this.getSuitStoreType() == null ? other.getSuitStoreType() == null : this.getSuitStoreType().equals(other.getSuitStoreType()))
            && (this.getSuitStoreIds() == null ? other.getSuitStoreIds() == null : this.getSuitStoreIds().equals(other.getSuitStoreIds()))
            && (this.getSaleLimitType() == null ? other.getSaleLimitType() == null : this.getSaleLimitType().equals(other.getSaleLimitType()))
            && (this.getSaleLimitNum() == null ? other.getSaleLimitNum() == null : this.getSaleLimitNum().equals(other.getSaleLimitNum()))
            && (this.getSaleLimitRemain() == null ? other.getSaleLimitRemain() == null : this.getSaleLimitRemain().equals(other.getSaleLimitRemain()))
            && (this.getSaleStoreLimit() == null ? other.getSaleStoreLimit() == null : this.getSaleStoreLimit().equals(other.getSaleStoreLimit()))
            && (this.getSalePrice() == null ? other.getSalePrice() == null : this.getSalePrice().equals(other.getSalePrice()))
            && (this.getLowestPrice() == null ? other.getLowestPrice() == null : this.getLowestPrice().equals(other.getLowestPrice()))
            && (this.getLowestPriceBuy() == null ? other.getLowestPriceBuy() == null : this.getLowestPriceBuy().equals(other.getLowestPriceBuy()))
            && (this.getStartTime() == null ? other.getStartTime() == null : this.getStartTime().equals(other.getStartTime()))
            && (this.getEndTime() == null ? other.getEndTime() == null : this.getEndTime().equals(other.getEndTime()))
            && (this.getBargainDuration() == null ? other.getBargainDuration() == null : this.getBargainDuration().equals(other.getBargainDuration()))
            && (this.getBargainPeople() == null ? other.getBargainPeople() == null : this.getBargainPeople().equals(other.getBargainPeople()))
            && (this.getVirtualPeople() == null ? other.getVirtualPeople() == null : this.getVirtualPeople().equals(other.getVirtualPeople()))
            && (this.getActivityStatus() == null ? other.getActivityStatus() == null : this.getActivityStatus().equals(other.getActivityStatus()))
            && (this.getBargainTimesPeriod() == null ? other.getBargainTimesPeriod() == null : this.getBargainTimesPeriod().equals(other.getBargainTimesPeriod()))
            && (this.getBargainTimesDay() == null ? other.getBargainTimesDay() == null : this.getBargainTimesDay().equals(other.getBargainTimesDay()))
            && (this.getAssistTimesPeriod() == null ? other.getAssistTimesPeriod() == null : this.getAssistTimesPeriod().equals(other.getAssistTimesPeriod()))
            && (this.getAssistTimesDay() == null ? other.getAssistTimesDay() == null : this.getAssistTimesDay().equals(other.getAssistTimesDay()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getActivityNo() == null) ? 0 : getActivityNo().hashCode());
        result = prime * result + ((getTenantId() == null) ? 0 : getTenantId().hashCode());
        result = prime * result + ((getAppBuId() == null) ? 0 : getAppBuId().hashCode());
        result = prime * result + ((getActivityName() == null) ? 0 : getActivityName().hashCode());
        result = prime * result + ((getSuitStoreType() == null) ? 0 : getSuitStoreType().hashCode());
        result = prime * result + ((getSuitStoreIds() == null) ? 0 : getSuitStoreIds().hashCode());
        result = prime * result + ((getSaleLimitType() == null) ? 0 : getSaleLimitType().hashCode());
        result = prime * result + ((getSaleLimitNum() == null) ? 0 : getSaleLimitNum().hashCode());
        result = prime * result + ((getSaleLimitRemain() == null) ? 0 : getSaleLimitRemain().hashCode());
        result = prime * result + ((getSaleStoreLimit() == null) ? 0 : getSaleStoreLimit().hashCode());
        result = prime * result + ((getSalePrice() == null) ? 0 : getSalePrice().hashCode());
        result = prime * result + ((getLowestPrice() == null) ? 0 : getLowestPrice().hashCode());
        result = prime * result + ((getLowestPriceBuy() == null) ? 0 : getLowestPriceBuy().hashCode());
        result = prime * result + ((getStartTime() == null) ? 0 : getStartTime().hashCode());
        result = prime * result + ((getEndTime() == null) ? 0 : getEndTime().hashCode());
        result = prime * result + ((getBargainDuration() == null) ? 0 : getBargainDuration().hashCode());
        result = prime * result + ((getBargainPeople() == null) ? 0 : getBargainPeople().hashCode());
        result = prime * result + ((getVirtualPeople() == null) ? 0 : getVirtualPeople().hashCode());
        result = prime * result + ((getActivityStatus() == null) ? 0 : getActivityStatus().hashCode());
        result = prime * result + ((getBargainTimesPeriod() == null) ? 0 : getBargainTimesPeriod().hashCode());
        result = prime * result + ((getBargainTimesDay() == null) ? 0 : getBargainTimesDay().hashCode());
        result = prime * result + ((getAssistTimesPeriod() == null) ? 0 : getAssistTimesPeriod().hashCode());
        result = prime * result + ((getAssistTimesDay() == null) ? 0 : getAssistTimesDay().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", activityNo=").append(activityNo);
        sb.append(", tenantId=").append(tenantId);
        sb.append(", appBuId=").append(appBuId);
        sb.append(", activityName=").append(activityName);
        sb.append(", suitStoreType=").append(suitStoreType);
        sb.append(", suitStoreIds=").append(suitStoreIds);
        sb.append(", saleLimitType=").append(saleLimitType);
        sb.append(", saleLimitNum=").append(saleLimitNum);
        sb.append(", saleLimitRemain=").append(saleLimitRemain);
        sb.append(", saleStoreLimit=").append(saleStoreLimit);
        sb.append(", salePrice=").append(salePrice);
        sb.append(", lowestPrice=").append(lowestPrice);
        sb.append(", lowestPriceBuy=").append(lowestPriceBuy);
        sb.append(", startTime=").append(startTime);
        sb.append(", endTime=").append(endTime);
        sb.append(", bargainDuration=").append(bargainDuration);
        sb.append(", bargainPeople=").append(bargainPeople);
        sb.append(", virtualPeople=").append(virtualPeople);
        sb.append(", activityStatus=").append(activityStatus);
        sb.append(", bargainTimesPeriod=").append(bargainTimesPeriod);
        sb.append(", bargainTimesDay=").append(bargainTimesDay);
        sb.append(", assistTimesPeriod=").append(assistTimesPeriod);
        sb.append(", assistTimesDay=").append(assistTimesDay);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", createBy=").append(createBy);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}