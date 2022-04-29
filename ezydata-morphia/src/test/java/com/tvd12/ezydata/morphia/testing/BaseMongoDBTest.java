package com.tvd12.ezydata.morphia.testing;

import java.io.InputStream;
import java.util.Map;

import com.mongodb.MongoClient;
import com.tvd12.ezydata.database.bean.EzyRepositoriesImplementer;
import com.tvd12.ezydata.mongodb.loader.EzySimpleMongoClientLoader;
import com.tvd12.ezydata.mongodb.loader.EzyMongoClientLoader;
import com.tvd12.ezydata.morphia.EzyDataStoreBuilder;
import com.tvd12.ezydata.morphia.bean.EzyMorphiaRepositoriesImplementer;
import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfox.bean.EzyBeanContextBuilder;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.io.EzyMaps;
import com.tvd12.ezyfox.reflect.EzyClasses;
import com.tvd12.ezyfox.stream.EzyAnywayInputStreamLoader;
import com.tvd12.test.base.BaseTest;

import dev.morphia.Datastore;

public class BaseMongoDBTest extends BaseTest {

    protected static final MongoClient MONGO_CLIENT = newMongoClient();
    protected static final Datastore DATASTORE = newDataStore();
    protected static final EzyBeanContext BEAN_CONTEXT = newBeanContext();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> MONGO_CLIENT.close()));
    }

    private static EzyBeanContext newBeanContext() {
        EzyBeanContextBuilder builder = EzyBeanContext.builder()
                .addSingleton("datastore", DATASTORE)
                .scan("com.tvd12.ezydata.morphia.testing.repo")
                .scan("com.tvd12.ezydata.morphia.testing.service")
                .scan("com.tvd12.ezydata.morphia.testing.repo", "com.tvd12.ezydata.morphia.testing.service")
                .scan(Sets.newHashSet("com.tvd12.ezydata.morphia.testing.service"));

        EzyRepositoriesImplementer implementer = new EzyMorphiaRepositoriesImplementer()
                .scan("com.tvd12.ezydata.morphia.testing.repo", "com.tvd12.ezydata.morphia.testing.repo1");
        Map<Class<?>, Object> repos = implementer.implement(DATASTORE);
        for(Class<?> key : repos.keySet()) {
            builder.addSingleton(EzyClasses.getVariableName(key), repos.get(key));
        }
        return builder.build();
    }

    private static Datastore newDataStore() {
        return EzyDataStoreBuilder.dataStoreBuilder()
                .mongoClient(MONGO_CLIENT)
                .databaseName("test")
                .scan("com.tvd12.ezydata.morphia.testing.data")
                .scan("com.tvd12.ezydata.morphia.testing.data", "com.tvd12.ezydata.morphia.testing.data")
                .scan(Sets.newHashSet("com.tvd12.ezydata.morphia.testing.data"))
                .addEntityClasses(Pig.class, Duck.class)
                .build();
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
                .context(BaseMongoDBTest.class)
                .build()
                .load("mongodb_config.properties");
    }

}
