package com.tvd12.ezydata.hazelcast.bean;

import com.hazelcast.core.HazelcastInstance;
import com.tvd12.ezydata.hazelcast.impl.EzySimpleServicesImplementer;

import java.util.Map;

public interface EzyServicesImplementer {

    static EzyServicesImplementer servicesImplementer() {
        return new EzySimpleServicesImplementer();
    }

    EzyServicesImplementer scan(String packageName);

    EzyServicesImplementer scan(String... packageNames);

    EzyServicesImplementer scan(Iterable<String> packageNames);

    EzyServicesImplementer serviceInterface(String mapName, Class<?> itf);

    Map<Class<?>, Object> implement(HazelcastInstance hzInstance);
}
