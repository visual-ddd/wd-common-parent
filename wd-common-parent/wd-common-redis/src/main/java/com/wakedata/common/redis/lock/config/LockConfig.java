package com.wakedata.common.redis.lock.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * common.lock-redis.address  : redis链接地址
 * common.lock-redis.password : redis密码
 * common.lock-redis.database : redis数据索引
 * common.lock-redis.waitTime : 获取锁最长阻塞时间（默认：60，单位：秒）
 * common.lock-redis.leaseTime: 已获取锁后自动释放时间（默认：60，单位：秒）
 * common.lock-redis.cluster-server.node-addresses : redis集群配置 如 127.0.0.1:7000,127.0.0.1:7001，127.0.0.1:7002
 * common.lock-redis.address 和 common.lock-redis.cluster-server.node-addresses 选其一即可
 *
 * @author hhf
 * @date 2020/12/28
 */
@ConfigurationProperties(prefix = LockConfig.PREFIX)
public class LockConfig {
    public static final String PREFIX = "common.redis-lock";
    private String address;
    private String password;
    private int database = 3;
    private ClusterServer clusterServer;
    private String codec = "org.redisson.codec.JsonJacksonCodec";
    private long waitTime = 60;
    private long leaseTime = 60;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
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

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public ClusterServer getClusterServer() {
        return clusterServer;
    }

    public void setClusterServer(ClusterServer clusterServer) {
        this.clusterServer = clusterServer;
    }

    public static class ClusterServer {

        private String[] nodeAddresses;

        public String[] getNodeAddresses() {
            return nodeAddresses;
        }

        public void setNodeAddresses(String[] nodeAddresses) {
            this.nodeAddresses = nodeAddresses;
        }
    }
}
