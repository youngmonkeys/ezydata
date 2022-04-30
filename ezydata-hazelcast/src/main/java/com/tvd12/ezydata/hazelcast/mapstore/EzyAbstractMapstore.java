package com.tvd12.ezydata.hazelcast.mapstore;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.MapLoaderLifecycleSupport;
import com.hazelcast.map.MapStore;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfox.util.EzyPostInit;

import java.util.*;

public abstract class EzyAbstractMapstore<K, V>
    extends EzyLoggable
    implements MapStore<K, V>, MapLoaderLifecycleSupport, EzyPostInit {

    protected Properties properties;
    protected HazelcastInstance hzInstance;

    @Override
    public final void init(
        HazelcastInstance hzInstance,
        Properties properties,
        String mapName
    ) {
        initComponents(hzInstance, properties);
        config(hzInstance, properties, mapName);
    }

    private void initComponents(
        HazelcastInstance hzInstance,
        Properties properties
    ) {
        this.properties = properties;
        this.hzInstance = hzInstance;
    }

    protected void config(
        HazelcastInstance hzInstance,
        Properties properties,
        String mapName
    ) {}

    @Override
    public void postInit() {}

    @Override
    public void destroy() {}

    @Override
    public void delete(K key) {}

    @Override
    public Map<K, V> loadAll(Collection<K> keys) {
        Map<K, V> map = new HashMap<>();
        for (K key : keys) {
            V value = load(key);
            if (value != null) {
                map.put(key, value);
            }
        }
        return map;
    }

    @Override
    public Iterable<K> loadAllKeys() {
        return new HashSet<>();
    }

    @Override
    public void storeAll(Map<K, V> map) {
        for (K key : map.keySet()) {
            store(key, map.get(key));
        }
    }

    @Override
    public void deleteAll(Collection<K> keys) {
    }

    @SuppressWarnings("unchecked")
    protected final <T> T getProperty(Object key) {
        return (T) properties.get(key);
    }

    protected final boolean containsProperty(Object key) {
        return properties.containsKey(key);
    }
}
