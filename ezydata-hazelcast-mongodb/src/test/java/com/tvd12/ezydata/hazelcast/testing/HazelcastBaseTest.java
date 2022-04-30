package com.tvd12.ezydata.hazelcast.testing;


import com.hazelcast.core.HazelcastInstance;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.tvd12.ezydata.hazelcast.factory.EzyMapTransactionFactory;
import com.tvd12.ezydata.hazelcast.factory.EzySimpleMapTransactionFactory;
import com.tvd12.ezydata.hazelcast.service.EzyTransactionalMaxIdService;
import com.tvd12.ezydata.mongodb.loader.EzyMongoClientLoader;
import com.tvd12.ezydata.mongodb.loader.EzySimpleMongoClientLoader;
import com.tvd12.ezyfox.database.service.EzyMaxIdService;
import com.tvd12.ezyfox.io.EzyMaps;
import com.tvd12.ezyfox.stream.EzyAnywayInputStreamLoader;
import com.tvd12.test.base.BaseTest;

import java.io.InputStream;

public abstract class HazelcastBaseTest extends BaseTest {

    protected static final MongoClient MONGO_CLIENT;
    protected static final MongoDatabase DATABASE;
    protected static final HazelcastInstance HZ_INSTANCE;
    protected static final EzyMaxIdService MAX_ID_SERVICE;
    protected static final EzyMapTransactionFactory MAP_TRANSACTION_FACTORY;

    static {
        MONGO_CLIENT = newMongoClient();
        DATABASE = MONGO_CLIENT.getDatabase("test");
        HZ_INSTANCE = newHzInstance();
        MAP_TRANSACTION_FACTORY = newMapTransactionFactory();
        MAX_ID_SERVICE = newMaxIdService();
        Runtime.getRuntime().addShutdownHook(new Thread(MONGO_CLIENT::close));
    }

    private static HazelcastInstance newHzInstance() {
        return new ExampleHazelcastCreator()
            .filePath("hazelcast.xml")
            .database(MONGO_CLIENT.getDatabase("test"))
            .create();
    }

    private static EzyMaxIdService newMaxIdService() {
        EzyTransactionalMaxIdService service = new EzyTransactionalMaxIdService(HZ_INSTANCE);
        service.setMapTransactionFactory(MAP_TRANSACTION_FACTORY);
        return service;
    }

    private static EzyMapTransactionFactory newMapTransactionFactory() {
        return new EzySimpleMapTransactionFactory(HZ_INSTANCE);
    }

    private static MongoClient newMongoClient() {
        return new EzySimpleMongoClientLoader()
            .inputStream(getMongoConfigInputStream())
            .property(EzyMongoClientLoader.DATABASE, "test")
            .properties(EzyMaps.newHashMap(EzyMongoClientLoader.DATABASE, "test"))
            .load();
    }

    private static InputStream getMongoConfigInputStream() {
        return EzyAnywayInputStreamLoader.builder()
            .context(HazelcastBaseTest.class)
            .build()
            .load("mongo_config.properties");
    }
}
