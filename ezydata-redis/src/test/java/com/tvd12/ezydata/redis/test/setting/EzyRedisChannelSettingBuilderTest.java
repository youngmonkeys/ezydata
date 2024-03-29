package com.tvd12.ezydata.redis.test.setting;

import com.tvd12.ezydata.redis.setting.EzyRedisChannelSetting;
import com.tvd12.ezydata.redis.setting.EzyRedisChannelSettingBuilder;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

public class EzyRedisChannelSettingBuilderTest {

    @Test
    public void nullCaseTest() {
        // given
        EzyRedisChannelSettingBuilder sut = new EzyRedisChannelSettingBuilder()
            .messageType((String) null)
            .subThreadPoolSize(null);

        // when
        EzyRedisChannelSetting channel = sut.build();

        // then
        Asserts.assertNull(channel.getMessageType());
    }
}
