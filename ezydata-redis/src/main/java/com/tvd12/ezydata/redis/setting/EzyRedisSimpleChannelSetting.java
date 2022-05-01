package com.tvd12.ezydata.redis.setting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EzyRedisSimpleChannelSetting implements EzyRedisChannelSetting {

    protected Class<?> messageType;
    protected int subThreadPoolSize = 1;
}
