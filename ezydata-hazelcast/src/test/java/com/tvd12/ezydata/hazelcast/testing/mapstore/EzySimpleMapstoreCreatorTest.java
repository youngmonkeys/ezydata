package com.tvd12.ezydata.hazelcast.testing.mapstore;

import com.tvd12.ezydata.hazelcast.mapstore.EzyMapstoresFetcher;
import com.tvd12.ezydata.hazelcast.mapstore.EzySimpleMapstoreCreator;
import com.tvd12.ezydata.hazelcast.mapstore.EzySimpleMapstoresFetcher;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import java.util.Properties;

public class EzySimpleMapstoreCreatorTest extends BaseTest {

    @Test
    public void test() {
        EzyMapstoresFetcher fetcher = newMapstoresFetcher();
        EzySimpleMapstoreCreator creator = new EzySimpleMapstoreCreator();
        creator.setMapstoresFetcher(fetcher);
        try {
            System.out.println(creator.create("example_users", new Properties()));
            creator.create("no", new Properties());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private EzyMapstoresFetcher newMapstoresFetcher() {
        return EzySimpleMapstoresFetcher.builder()
            .scan("com.tvd12.ezydata.hazelcast.testing.mapstore")
            .build();
    }
}
