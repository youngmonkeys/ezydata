package com.tvd12.ezydata.mongodb.testing.loader;

import com.tvd12.ezydata.mongodb.loader.EzyMongoClientLoader;
import com.tvd12.ezydata.mongodb.loader.EzySimpleMongoClientLoader;
import com.tvd12.ezyfox.stream.EzyAnywayInputStreamLoader;
import com.tvd12.ezyfox.util.EzyMapBuilder;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import java.io.InputStream;

public class EzyInputStreamMongoClientLoaderTest extends BaseTest {

    @Test
    public void test() {
        InputStream inputStream = EzyAnywayInputStreamLoader.builder()
            .context(getClass())
            .build()
            .load("mongodb_config.properties");
        EzySimpleMongoClientLoader loader = new EzySimpleMongoClientLoader()
            .inputStream(inputStream)
            .property(EzyMongoClientLoader.HOST, "127.0.0.1")
            .property(EzyMongoClientLoader.PORT, "27017")
            .configFile("mongodb_config.properties")
            .properties(EzyMapBuilder.mapBuilder()
                .put(EzyMongoClientLoader.USERNAME, "root")
                .put(EzyMongoClientLoader.PASSWORD, "123456")
                .put(EzyMongoClientLoader.DATABASE, "test")
                .build());
        loader.load();
    }
}
