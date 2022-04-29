package com.tvd12.ezydata.hazelcast.factory;

import java.util.Properties;

import com.tvd12.ezydata.hazelcast.mapstore.EzyMapstoreCreator;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EzySimpleMapstoreFactory extends EzyAbstractMapstoreFactory {

    protected EzyMapstoreCreator mapstoreCreator;

    @Override
    protected Object newMapstore(String mapName, Properties properties) {
        return mapstoreCreator.create(mapName, properties);
    }

}
