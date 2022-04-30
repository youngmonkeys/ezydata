package com.tvd12.ezydata.hazelcast.testing.service;

import com.tvd12.ezydata.hazelcast.mapstore.EzySimpleMapstoresFetcher;
import com.tvd12.ezydata.hazelcast.testing.mapstore.ExampleUserMapstore;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzySimpleMapstoresFetcherTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleMapstoresFetcher fetcher = EzySimpleMapstoresFetcher.builder()
            .scan(
                "com.tvd12.ezydata.hazelcast.testing.mapstore",
                "com.tvd12.ezydata.hazelcast.testing.mapstore"
            )
            .addMapstoreClass(Class.class)
            .addMapstoreClass("hello", Class.class)
            .addMapstoreClass("example_users", ExampleUserMapstore.class)
            .addMapstore(new ExampleUserMapstore())
            .addMapstore("example_users", new ExampleUserMapstore())
            .build();
        assert fetcher.getMapstores().size() == 1;
    }
}
