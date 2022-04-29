package com.tvd12.ezydata.hazelcast.testing.mapstore;

import java.util.Map;

import com.tvd12.ezydata.hazelcast.mapstore.EzyMongoDatastoreMapstore;
import com.tvd12.ezydata.hazelcast.testing.entity.ExampleUser;
import com.tvd12.ezyfox.database.annotation.EzyMapstore;
import com.tvd12.ezyfox.io.EzyMaps;

@EzyMapstore("example_users")
public class ExampleUserMapstore extends EzyMongoDatastoreMapstore<String, ExampleUser> {

    private static final Map<String, ExampleUser> MAP =
            EzyMaps.newHashMap("dungtv", new ExampleUser("dungtv"));

    @Override
    public void store(String key, ExampleUser value) {
        MAP.put(key, value);
    }

    @Override
    public ExampleUser load(String key) {
        return MAP.get(key);
    }



}
