package com.tvd12.ezydata.hazelcast.testing.mapstore;

import com.tvd12.ezydata.hazelcast.mapstore.EzyAbstractMapstore;
import com.tvd12.ezydata.hazelcast.testing.entity.ExampleUser;
import com.tvd12.ezyfox.database.annotation.EzyMapstore;
import com.tvd12.ezyfox.io.EzyMaps;

import java.util.Map;

@EzyMapstore("example_users_2")
public class ExampleUserMapstore2 extends EzyAbstractMapstore<String, ExampleUser> {

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
