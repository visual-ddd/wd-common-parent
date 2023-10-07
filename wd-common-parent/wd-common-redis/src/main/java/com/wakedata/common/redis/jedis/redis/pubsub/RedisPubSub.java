package com.wakedata.common.redis.jedis.redis.pubsub;


import redis.clients.jedis.JedisPubSub;

@Deprecated
public abstract class RedisPubSub extends JedisPubSub {


    @Override
    public void onPMessage(String pattern, String channel, String message) {

    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {

    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {

    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {

    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {

    }
}
