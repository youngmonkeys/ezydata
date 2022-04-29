package com.tvd12.ezydata.redis.manager;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezydata.redis.EzyRedisAtomicLong;
import com.tvd12.ezydata.redis.factory.EzyRedisAtomicLongFactory;
import com.tvd12.ezyfox.builder.EzyBuilder;

public class EzyRedisAtomicLongProvider {

    protected final EzyRedisAtomicLongFactory atomicLongFactory;
    protected final Map<String, EzyRedisAtomicLong> atomicLongs;

    protected EzyRedisAtomicLongProvider(Builder builder) {
        this.atomicLongs = new HashMap<>();
        this.atomicLongFactory = builder.atomicLongFactory;
    }

    public EzyRedisAtomicLong getAtomicLong(String name) {
        EzyRedisAtomicLong atomicLong = atomicLongs.get(name);
        if(atomicLong == null)
            atomicLong = newAtomicLong(name);
        return atomicLong;
    }

    protected EzyRedisAtomicLong newAtomicLong(String name) {
        synchronized (atomicLongs) {
            EzyRedisAtomicLong atomicLong = atomicLongs.get(name);
            if(atomicLong == null) {
                atomicLong = atomicLongFactory.newAtomicLong(name);
                atomicLongs.put(name, atomicLong);
            }
            return atomicLong;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements EzyBuilder<EzyRedisAtomicLongProvider> {

        protected EzyRedisAtomicLongFactory atomicLongFactory;

        public Builder atomicLongFactory(EzyRedisAtomicLongFactory atomicLongFactory) {
            this.atomicLongFactory = atomicLongFactory;
            return this;
        }

        @Override
        public EzyRedisAtomicLongProvider build() {
            return new EzyRedisAtomicLongProvider(this);
        }

    }

}
