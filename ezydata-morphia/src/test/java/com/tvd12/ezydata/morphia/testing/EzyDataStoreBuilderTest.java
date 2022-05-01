package com.tvd12.ezydata.morphia.testing;

import com.tvd12.ezydata.morphia.EzyDataStoreBuilder;
import com.tvd12.ezydata.morphia.testing.data.Cat;
import com.tvd12.ezydata.morphia.testing.data.Person;
import org.testng.annotations.Test;

public class EzyDataStoreBuilderTest extends BaseMongoDBTest {

    @Test
    public void test() {
        DATASTORE.save(new Cat());
        DATASTORE.save(new Person());

        EzyDataStoreBuilder.dataStoreBuilder()
            .mongoClient(MONGO_CLIENT)
            .databaseName("test")
            .addEntityClasses(Pig.class, Duck.class)
            .build();
    }
}
