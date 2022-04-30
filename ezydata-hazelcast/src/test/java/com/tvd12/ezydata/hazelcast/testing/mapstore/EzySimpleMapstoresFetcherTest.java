package com.tvd12.ezydata.hazelcast.testing.mapstore;

import com.tvd12.ezydata.hazelcast.mapstore.EzySimpleMapstoresFetcher;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzySimpleMapstoresFetcherTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleMapstoresFetcher fetcher = EzySimpleMapstoresFetcher.builder()
            .build();
        assert fetcher.getMapNames().isEmpty();
    }
}
