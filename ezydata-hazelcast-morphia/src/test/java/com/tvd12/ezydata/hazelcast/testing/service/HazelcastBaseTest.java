package com.tvd12.ezydata.hazelcast.testing.service;

import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import com.mongodb.MongoClient;
import com.tvd12.ezydata.hazelcast.EzyMongoDatastoreHazelcastFactory;
import com.tvd12.ezydata.hazelcast.service.EzyTransactionalMaxIdService;
import com.tvd12.ezydata.mongodb.loader.EzySimpleMongoClientLoader;
import com.tvd12.ezyfox.database.service.EzyMaxIdService;
import com.tvd12.test.base.BaseTest;

import java.io.InputStream;

public abstract class HazelcastBaseTest extends BaseTest {

    protected static final MongoClient MONGO_CLIENT;
    protected static final EzyMaxIdService MAX_ID_SERVICE;
    protected static final HazelcastInstance HZ_INSTANCE;

    static {
        MONGO_CLIENT = newMongoClient();
        HZ_INSTANCE = newHzInstance();
        MAX_ID_SERVICE = newMaxIdService();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("\n\nshutdown hook, close mongo client\n\n");
            MONGO_CLIENT.close();
        }));
    }

    private static MongoClient newMongoClient() {
        return new EzySimpleMongoClientLoader()
            .inputStream(getConfigStream())
            .load();
    }

    private static EzyMaxIdService newMaxIdService() {
        return new EzyTransactionalMaxIdService(HZ_INSTANCE);
    }

    private static HazelcastInstance newHzInstance() {
        EzyMongoDatastoreHazelcastFactory factory = new EzyMongoDatastoreHazelcastFactory();
        factory.setDatabase(MONGO_CLIENT.getDatabase("test"));
        return factory.newHazelcast(new Config());
    }

    private static InputStream getConfigStream() {
        return HazelcastBaseTest.class.getResourceAsStream("/mongo_config.properties");
    }
}
