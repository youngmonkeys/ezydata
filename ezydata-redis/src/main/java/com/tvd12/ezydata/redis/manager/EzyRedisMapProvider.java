package com.tvd12.ezydata.redis.manager;

import com.tvd12.ezydata.redis.EzyRedisMap;
import com.tvd12.ezydata.redis.factory.EzyRedisMapFactory;
import com.tvd12.ezyfox.builder.EzyBuilder;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EzyRedisMapProvider {

    protected final Map<String, EzyRedisMap> maps;
    protected final EzyRedisMapFactory mapFactory;

    protected EzyRedisMapProvider(Builder builder) {
        this.maps = new HashMap<>();
        this.mapFactory = builder.mapFactory;
    }

    public static Builder builder() {
        return new Builder();
    }

    public <K, V> EzyRedisMap<K, V> getMap(String name) {
        EzyRedisMap<K, V> map = maps.get(name);
        if (map == null) {
            map = newMap(name);
        }
        return map;
    }

    public <K, V> EzyRedisMap<K, V> getMap(
        String name, Class<K> keyType, Class<V> valueType) {
        EzyRedisMap<K, V> map = maps.get(name);
        if (map == null) {
            map = newMap(name, keyType, valueType);
        }
        return map;
    }

    protected <K, V> EzyRedisMap<K, V> newMap(String name) {
        synchronized (maps) {
            EzyRedisMap<K, V> map = maps.get(name);
            if (map == null) {
                map = mapFactory.newMap(name);
                maps.put(name, map);
            }
            return map;
        }
    }

    protected <K, V> EzyRedisMap<K, V> newMap(
        String name, Class<K> keyType, Class<V> valueType) {
        synchronized (maps) {
            EzyRedisMap<K, V> map = maps.get(name);
            if (map == null) {
                map = mapFactory.newMap(name, keyType, valueType);
                maps.put(name, map);
            }
            return map;
        }
    }

    public static class Builder implements EzyBuilder<EzyRedisMapProvider> {

        protected EzyRedisMapFactory mapFactory;

        public Builder mapFactory(EzyRedisMapFactory mapFactory) {
            this.mapFactory = mapFactory;
            return this;
        }

        @Override
        public EzyRedisMapProvider build() {
            return new EzyRedisMapProvider(this);
        }
    }
}
