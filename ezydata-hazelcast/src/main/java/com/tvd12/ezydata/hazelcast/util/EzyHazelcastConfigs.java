package com.tvd12.ezydata.hazelcast.util;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.tvd12.ezyfox.stream.EzyAnywayInputStreamLoader;

import java.io.InputStream;

public final class EzyHazelcastConfigs {

    private EzyHazelcastConfigs() {}

    public static Config newXmlConfigBuilder(String filePath) {
        InputStream stream = EzyAnywayInputStreamLoader.builder()
            .build()
            .load(filePath);
        return newXmlConfigBuilder(stream);
    }

    public static Config newXmlConfigBuilder(InputStream stream) {
        return new XmlConfigBuilder(stream).build();
    }
}
