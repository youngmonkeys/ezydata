package com.tvd12.ezydata.redis.setting;

public interface EzyRedisChannelSetting {

    Class<?> getMessageType();

    int getSubThreadPoolSize();
}
