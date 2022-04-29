package com.tvd12.ezydata.redis.test;

import org.testng.annotations.Test;

import com.tvd12.ezydata.redis.factory.EzyRedisMapFactory;
import com.tvd12.ezydata.redis.setting.EzyRedisSettingsBuilder;

public class EzyRedisMapFactoryTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void newMapFailed() {
        // given
        EzyRedisMapFactory factory = EzyRedisMapFactory.builder()
                .settings(new EzyRedisSettingsBuilder().build())
                .build();

        // when
        // then
        factory.newMap("no map");
    }

}
