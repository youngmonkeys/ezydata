package com.tvd12.ezydata.redis;

import com.tvd12.ezyfox.builder.EzyBuilder;

public class EzyRedisAtomicLong {

    protected final String name;
    protected final String mapName;
    protected final byte[] nameBytes;
    protected final byte[] mapNameBytes;
    protected final EzyRedisClient redisClient;

    protected EzyRedisAtomicLong(Builder builder) {
        this.name = builder.name;
        this.mapName = builder.mapName;
        this.nameBytes = name.getBytes();
        this.mapNameBytes = mapName.getBytes();
        this.redisClient = builder.redisClient;
    }

    public long get() {
        Long answer = redisClient.hincrBy(mapNameBytes, nameBytes, 0);
        return answer;
    }

    public long addAndGet(long delta) {
        Long answer = redisClient.hincrBy(mapNameBytes, nameBytes, delta);
        return answer;
    }

    public long incrementAndGet() {
        return addAndGet(1);
    }

    public String getName() {
        return name;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements EzyBuilder<EzyRedisAtomicLong> {

        protected String name;
        protected String mapName;
        protected EzyRedisClient redisClient;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder mapName(String mapName) {
            this.mapName = mapName;
            return this;
        }

        public Builder redisClient(EzyRedisClient redisClient) {
            this.redisClient = redisClient;
            return this;
        }

        @Override
        public EzyRedisAtomicLong build() {
            return new EzyRedisAtomicLong(this);
        }


    }
}
