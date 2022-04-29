package com.tvd12.ezydata.redis.factory;

import com.tvd12.ezydata.redis.EzyRedisClient;
import com.tvd12.ezydata.redis.EzyRedisMap;
import com.tvd12.ezydata.redis.setting.EzyRedisMapSetting;
import com.tvd12.ezydata.redis.setting.EzyRedisMapSettingBuilder;
import com.tvd12.ezydata.redis.setting.EzyRedisSettings;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyEntityCodec;

public class EzyRedisMapFactory {

    protected final EzyRedisSettings settings;
    protected final EzyRedisClient redisClient;
    protected final EzyEntityCodec entityCodec;

    protected EzyRedisMapFactory(Builder builder) {
        this.settings = builder.settings;
        this.redisClient = builder.redisClient;
        this.entityCodec = builder.entityCodec;
    }

    public <K, V> EzyRedisMap<K, V> newMap(
            String name, Class<K> keyType, Class<V> valueType) {
        EzyRedisMapSetting mapSetting = new EzyRedisMapSettingBuilder()
                .keyType(keyType)
                .valueType(valueType)
                .build();
        return newMap(name, mapSetting);
    }

    public <K, V> EzyRedisMap<K, V> newMap(String name) {
        EzyRedisMapSetting mapSetting = settings.getMapSeting(name);
        return newMap(name, mapSetting);
    }

    @SuppressWarnings("unchecked")
    private <K, V> EzyRedisMap<K, V> newMap(
            String name, EzyRedisMapSetting mapSetting) {
        if(mapSetting == null)
            throw new IllegalArgumentException("has no setting for map: " + name);
        return EzyRedisMap.builder()
                .mapName(name)
                .setting(mapSetting)
                .redisClient(redisClient)
                .entityCodec(entityCodec)
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements EzyBuilder<EzyRedisMapFactory> {

        protected EzyRedisSettings settings;
        protected EzyRedisClient redisClient;
        protected EzyEntityCodec entityCodec;

        public Builder settings(EzyRedisSettings settings) {
            this.settings = settings;
            return this;
        }

        public Builder redisClient(EzyRedisClient redisClient) {
            this.redisClient = redisClient;
            return this;
        }

        public Builder entityCodec(EzyEntityCodec entityCodec) {
            this.entityCodec = entityCodec;
            return this;
        }

        @Override
        public EzyRedisMapFactory build() {
            return new EzyRedisMapFactory(this);
        }

    }

}
