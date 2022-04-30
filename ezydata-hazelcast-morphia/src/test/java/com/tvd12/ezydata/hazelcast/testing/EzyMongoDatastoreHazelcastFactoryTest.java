package com.tvd12.ezydata.hazelcast.testing;

import com.hazelcast.config.Config;
import com.tvd12.ezydata.hazelcast.EzyMongoDatastoreHazelcastFactory;
import com.tvd12.ezydata.hazelcast.mapstore.EzyMapstoresFetcher;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.test.base.BaseTest;
import dev.morphia.Datastore;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EzyMongoDatastoreHazelcastFactoryTest extends BaseTest {

    @Test
    public void test() {
        Datastore datastore = mock(Datastore.class);
        EzyMapstoresFetcher mapstoresFetcher = mock(EzyMapstoresFetcher.class);
        when(mapstoresFetcher.getMapNames()).thenReturn(Sets.newHashSet());
        EzyMongoDatastoreHazelcastFactory factory = new EzyMongoDatastoreHazelcastFactory();
        factory.setDatastore(datastore);
        factory.setMapstoresFetcher(mapstoresFetcher);
        factory.newHazelcast(new Config());
    }
}
