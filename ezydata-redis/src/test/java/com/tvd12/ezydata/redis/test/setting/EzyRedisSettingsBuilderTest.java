package com.tvd12.ezydata.redis.test.setting;

import org.testng.annotations.Test;

import com.tvd12.ezydata.redis.setting.EzyRedisChannelSetting;
import com.tvd12.ezydata.redis.setting.EzyRedisChannelSettingBuilder;
import com.tvd12.ezydata.redis.setting.EzyRedisMapSettingBuilder;
import com.tvd12.ezydata.redis.setting.EzyRedisSettings;
import com.tvd12.ezydata.redis.setting.EzyRedisSettingsBuilder;
import com.tvd12.properties.file.reader.BaseFileReader;

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
        EzyRedisChannelSetting channelSeting = redisSettings.getChannelSeting("ezydata_channel1");
        assert channelSeting.getSubThreadPoolSize() == 2;
        assert channelSeting.getMessageType() == String.class;
    }
    
}
