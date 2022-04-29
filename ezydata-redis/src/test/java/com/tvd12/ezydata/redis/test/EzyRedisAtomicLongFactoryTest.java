package com.tvd12.ezydata.redis.test;

import org.testng.annotations.Test;

import com.tvd12.ezydata.redis.factory.EzyRedisAtomicLongFactory;
import com.tvd12.ezydata.redis.setting.EzyRedisSettingsBuilder;

public class EzyRedisAtomicLongFactoryTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void newMapFailed() {
        // given
        EzyRedisAtomicLongFactory factory = EzyRedisAtomicLongFactory.builder()
                .settings(new EzyRedisSettingsBuilder()
                        .atomicLongMapName("")
                        .build()
                )
                .build();

        // when
        // then
        factory.newAtomicLong("hello");
    }

}
