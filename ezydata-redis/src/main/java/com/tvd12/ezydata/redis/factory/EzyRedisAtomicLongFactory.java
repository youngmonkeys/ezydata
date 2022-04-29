package com.tvd12.ezydata.redis.factory;

import com.tvd12.ezydata.redis.EzyRedisAtomicLong;
import com.tvd12.ezydata.redis.EzyRedisClient;
import com.tvd12.ezydata.redis.setting.EzyRedisSettings;
import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.io.EzyStrings;

public class EzyRedisAtomicLongFactory {

    protected final EzyRedisSettings settings;
    protected final EzyRedisClient redisClient;

    protected EzyRedisAtomicLongFactory(Builder builder) {
        this.settings = builder.settings;
        this.redisClient = builder.redisClient;
    }

    public EzyRedisAtomicLong newAtomicLong(String name) {
        String mapName = settings.getAtomicLongMapName();
        if(EzyStrings.isNoContent(mapName))
            throw new IllegalArgumentException("has no setting for atomic long map name");
        return EzyRedisAtomicLong.builder()
                .name(name)
                .redisClient(redisClient)
                .mapName(mapName)
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements EzyBuilder<EzyRedisAtomicLongFactory> {

        protected EzyRedisSettings settings;
        protected EzyRedisClient redisClient;

        public Builder settings(EzyRedisSettings settings) {
            this.settings = settings;
            return this;
        }

        public Builder redisClient(EzyRedisClient redisClient) {
            this.redisClient = redisClient;
            return this;
        }

        @Override
        public EzyRedisAtomicLongFactory build() {
            return new EzyRedisAtomicLongFactory(this);
        }

    }

}
