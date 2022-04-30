package com.tvd12.ezydata.hazelcast.mapstore;

import com.hazelcast.map.MapStore;

import java.util.Properties;
import java.util.Set;

public interface EzyMapstoreCreator {

    Set<String> getMapNames();

    @SuppressWarnings("rawtypes")
    MapStore create(String mapName, Properties properties);
}
