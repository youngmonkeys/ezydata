package com.tvd12.ezydata.redis.test.setting;

import com.tvd12.ezydata.redis.setting.EzyRedisMapSetting;
import com.tvd12.ezydata.redis.setting.EzyRedisMapSettingBuilder;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

public class EzyRedisMapSettingBuilderTest {

    @Test
    public void nullCaseTest() {
        // given
        EzyRedisMapSettingBuilder sut = new EzyRedisMapSettingBuilder()
            .keyType((Class<?>) null)
            .keyType((String) null)
            .valueType((Class<?>) null)
            .valueType((String) null);

        // when
        EzyRedisMapSetting setting = sut.build();

        // then
        Asserts.assertNull(setting.getKeyType());
        Asserts.assertNull(setting.getValueType());
    }
}
