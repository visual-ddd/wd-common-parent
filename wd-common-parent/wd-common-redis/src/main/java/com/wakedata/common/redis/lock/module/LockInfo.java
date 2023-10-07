package com.wakedata.common.redis.lock.module;

/**
 * 锁基本信息
 *
 * @author hhf
 * @date 2020/12/28
 */
public class LockInfo {

    /**
     * 锁的类型
     */
    private LockType type;
    /**
     * 锁的名称
     */
    private String name;
    /**
     * 获取锁的最大等待时间
     */
    private long waitTime;
    /**
     * 锁租约时间
     */
    private long leaseTime;

    public LockInfo() {
    }

    public LockInfo(LockType type, String name, long waitTime, long leaseTime) {
        this.type = type;
        this.name = name;
        this.waitTime = waitTime;
        this.leaseTime = leaseTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    public long getLeaseTime() {
        return leaseTime;
    }

    public void setLeaseTime(long leaseTime) {
        this.leaseTime = leaseTime;
    }

    public LockType getType() {
        return type;
    }

    public void setType(LockType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "LockInfo{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", waitTime=" + waitTime +
                ", leaseTime=" + leaseTime +
                '}';
    }
}
