package com.tvd12.ezydata.hazelcast.testing.mapstore;

import com.tvd12.ezydata.hazelcast.mapstore.EzyMongoMaxIdMapstore;
import com.tvd12.ezydata.hazelcast.testing.HazelcastBaseTest;
import org.testng.annotations.Test;

public class EzyMongoMaxIdMapstoreTest extends HazelcastBaseTest {

    @Test
    public void test() {
        EzyMongoMaxIdMapstore mapstore = new EzyMongoMaxIdMapstore();
        mapstore.setDatabase(DATABASE);
        mapstore.postInit();
        mapstore.load("not exists key");
    }
}
