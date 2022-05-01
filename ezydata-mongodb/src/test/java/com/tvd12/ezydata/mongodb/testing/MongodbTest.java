package com.tvd12.ezydata.mongodb.testing;

import com.mongodb.MongoClient;
import com.tvd12.ezydata.mongodb.loader.EzyMongoClientLoader;
import com.tvd12.ezydata.mongodb.loader.EzySimpleMongoClientLoader;
import com.tvd12.ezyfox.stream.EzyAnywayInputStreamLoader;
import com.tvd12.ezyfox.util.EzyMapBuilder;
import com.tvd12.test.base.BaseTest;

import java.io.InputStream;

public class MongodbTest extends BaseTest {

    protected static String databaseName;
    protected static MongoClient mongoClient;

    static {
        databaseName = "test";
        mongoClient = mongoClientLoader().load();
    }

    protected static EzySimpleMongoClientLoader mongoClientLoader() {
        InputStream inputStream = EzyAnywayInputStreamLoader.builder()
            .build()
            .load("mongodb_config.properties");
        EzySimpleMongoClientLoader loader = new EzySimpleMongoClientLoader()
            .inputStream(inputStream)
            .property(EzyMongoClientLoader.HOST, "127.0.0.1")
            .property(EzyMongoClientLoader.PORT, "27017")
            .properties(EzyMapBuilder.mapBuilder()
                .put(EzyMongoClientLoader.USERNAME, "root")
                .put(EzyMongoClientLoader.PASSWORD, "123456")
                .put(EzyMongoClientLoader.DATABASE, databaseName)
                .build());
        return loader;
    }


}
