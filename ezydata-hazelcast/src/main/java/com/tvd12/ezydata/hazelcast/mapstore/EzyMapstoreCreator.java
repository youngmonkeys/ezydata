package com.tvd12.ezydata.hazelcast.mapstore;

import java.util.Properties;
import java.util.Set;

import com.hazelcast.map.MapStore;

public interface EzyMapstoreCreator {

    Set<String> getMapNames();
    
    @SuppressWarnings("rawtypes")
    MapStore create(String mapName, Properties properties);
    
}
