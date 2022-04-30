package com.tvd12.ezydata.hazelcast;

import com.tvd12.ezydata.hazelcast.mapstore.EzyMapstoreCreator;
import com.tvd12.ezydata.hazelcast.mapstore.EzySimpleMapstoreCreator;

public class EzySimpleHazelcastFactory extends EzyAbstractHazelcastFactory {

    @Override
    protected EzyMapstoreCreator newMapstoreCreator() {
        return new EzySimpleMapstoreCreator();
    }
}
