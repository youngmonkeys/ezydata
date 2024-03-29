package com.tvd12.ezydata.hazelcast.mapstore;

import com.hazelcast.map.MapStore;
import com.tvd12.ezyfox.util.EzyLoggable;
import lombok.Setter;

import java.util.Properties;
import java.util.Set;

public class EzySimpleMapstoreCreator
    extends EzyLoggable
    implements EzyMapstoreCreator, EzyMapstoresFetcherAware {

    @Setter
    protected EzyMapstoresFetcher mapstoresFetcher;

    @Override
    public Set<String> getMapNames() {
        return mapstoresFetcher.getMapNames();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public MapStore create(String mapName, Properties properties) {
        if (mapstoresFetcher.containsMapstore(mapName)) {
            return (MapStore) mapstoresFetcher.getMapstore(mapName);
        }
        throw new IllegalArgumentException("has no mapstore with name = " + mapName);
    }
}
