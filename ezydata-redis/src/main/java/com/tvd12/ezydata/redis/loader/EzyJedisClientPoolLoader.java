package com.tvd12.ezydata.redis.loader;

import com.tvd12.ezydata.redis.EzyJedisClientPool;
import com.tvd12.ezydata.redis.EzyRedisClientPool;

import redis.clients.jedis.JedisPool;

public class EzyJedisClientPoolLoader 
        extends EzyRedisAbstractClientPoolLoader<EzyJedisClientPoolLoader> {

    @Override
    protected EzyRedisClientPool doLoad() {
        JedisPool jedisPool = null;
        if(uri != null) {
            jedisPool = new JedisPool(java.net.URI.create(uri));
        }
        else if(host != null && port > 0) {
            jedisPool = new JedisPool(host, port);
        }
        else if(host != null) {
            jedisPool = new JedisPool(host, DEFAULT_PORT);
        }
        else {
            jedisPool = new JedisPool();
        }
        return new EzyJedisClientPool(jedisPool);
    }

}
