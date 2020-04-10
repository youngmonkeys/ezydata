package com.tvd12.ezydata.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Jedis;

public class EzyJedisProxy implements EzyRedisClient {

	protected final Jedis jedis;
	
	public EzyJedisProxy(Jedis jedis) {
		this.jedis = jedis;
	}
	
	@Override
	public void del(byte[] key) {
		jedis.del(key);
	}

	@Override
	public byte[] hget(byte[] key, byte[] field) {
		return jedis.hget(key, field);
	}

	@Override
	public Set<byte[]> hkeys(byte[] key) {
		return jedis.hkeys(key);
	}

	@Override
	public long hlen(byte[] key) {
		return jedis.hlen(key);
	}

	@Override
	public void hset(byte[] key, byte[] field, byte[] value) {
		jedis.hset(key, field, value);
	}

	@Override
	public void hdel(byte[] key, byte[] field) {
		jedis.hdel(key, field);
	}

	@Override
	public Long hdel(byte[] key, byte[]... fields) {
		return jedis.hdel(key, fields);
	}

	@Override
	public Map<byte[], byte[]> hgetAll(byte[] key) {
		return jedis.hgetAll(key);
	}

	@Override
	public List<byte[]> hmget(byte[] key, byte[]... fields) {
		return jedis.hmget(key, fields);
	}

	@Override
	public void hmset(byte[] key, Map<byte[], byte[]> values) {
		jedis.hmset(key, values);
	}

	@Override
	public Long hincrBy(byte[] key, byte[] field, long value) {
		return jedis.hincrBy(key, field, value);
	}

	@Override
	public Long publish(byte[] channelName, byte[] message) {
		return jedis.publish(channelName, message);
	}

	@Override
	public void subscribe(EzyRedisSubscriber subscriber, byte[] channelName) {
		BinaryJedisPubSub pubSub = new BinaryJedisPubSub() {
			@Override
			public void onMessage(byte[] channel, byte[] message) {
				subscriber.onMessage(channel, message);
			}
		};
		jedis.subscribe(pubSub, channelName);
	}
	
	@Override
	public void close() {
		jedis.close();
	}
	
	@Override
	public String toString() {
		return jedis.toString();
	}
	
}
