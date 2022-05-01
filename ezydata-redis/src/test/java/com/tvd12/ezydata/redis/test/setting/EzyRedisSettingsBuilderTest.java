package com.tvd12.ezydata.redis.test.setting;

import com.tvd12.ezydata.redis.setting.*;
import com.tvd12.properties.file.reader.BaseFileReader;
import org.testng.annotations.Test;

public class EzyRedisSettingsBuilderTest {

    @Test
    public void testAll() {
        // give
        String atomicLongMapName = "atomicLongMapNameTest";
        EzyRedisSettings redisSettings = new EzyRedisSettingsBuilder()
            .atomicLongMapName(atomicLongMapName)
            .properties(new BaseFileReader().read("application_test.yaml"))
            .addMapSetting("ezydata_key_value2", new EzyRedisMapSettingBuilder()
                .keyType(String.class)
                .valueType(Integer.class)
                .build()
            )
            .addChannelSetting("ezydata_channel1", new EzyRedisChannelSettingBuilder()
                .messageType(String.class)
                .subThreadPoolSize(2)
                .build())
            .mapSettingBuilder("ezydata_key_value_x2")
            .parent()
            .mapSettingBuilder("ezydata_key_value_x2")
            .parent()
            .channelSettingBuilder("ezydata_channel_x2")
            .parent()
            .channelSettingBuilder("ezydata_channel_x2")
            .parent()
            .build();

        // when
        // then
        assert redisSettings.getAtomicLongMapName().equals(atomicLongMapName);
        EzyRedisChannelSetting channelSetting = redisSettings.getChannelSetting("ezydata_channel1");
        assert channelSetting.getSubThreadPoolSize() == 2;
        assert channelSetting.getMessageType() == String.class;
    }
}
