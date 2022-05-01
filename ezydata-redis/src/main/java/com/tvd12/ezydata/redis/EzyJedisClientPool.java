package com.tvd12.ezydata.redis;

import redis.clients.jedis.JedisPool;

public class EzyJedisClientPool implements EzyRedisClientPool {

    protected final JedisPool jedisPool;

    public EzyJedisClientPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public EzyRedisClient getClient() {
        return new EzyJedisProxy(jedisPool.getResource());
    }

    @Override
    public void close() {
        this.jedisPool.close();
    }
}
