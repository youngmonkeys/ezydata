package com.tvd12.ezydata.example.redis.config;

import java.util.Properties;

import com.tvd12.ezydata.redis.EzyRedisProxy;
import com.tvd12.ezydata.redis.EzyRedisProxyFactory;
import com.tvd12.ezyfox.bean.annotation.EzyConfigurationBefore;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.util.EzyPropertiesAware;

import lombok.Setter;

@Setter
@EzyConfigurationBefore
public class RedisConfig implements EzyPropertiesAware {
    private Properties properties;

    @EzySingleton
    public EzyRedisProxy openRedisProxy() {
        return EzyRedisProxyFactory.builder()
            .properties(properties)
            .scan("com.tvd12.ezydata.example.redis.entity")
            .build()
            .newRedisProxy();
    }
}