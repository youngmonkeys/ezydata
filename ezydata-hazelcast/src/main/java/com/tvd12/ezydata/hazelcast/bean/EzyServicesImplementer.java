package com.tvd12.ezydata.hazelcast.bean;

import java.util.Map;

import com.hazelcast.core.HazelcastInstance;
import com.tvd12.ezydata.hazelcast.impl.EzySimpleServicesImplementer;

public interface EzyServicesImplementer {

    public static EzyServicesImplementer servicesImplement() {
        return new EzySimpleServicesImplementer();
    }

    public abstract EzyServicesImplementer scan(String packageName);

    public abstract EzyServicesImplementer scan(String... packageNames);

    public abstract EzyServicesImplementer scan(Iterable<String> packageNames);

    public abstract EzyServicesImplementer serviceInterface(String mapName, Class<?> itf);

    public abstract Map<Class<?>, Object> implement(HazelcastInstance hzInstance);

}
