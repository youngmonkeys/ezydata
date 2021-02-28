package com.tvd12.ezydata.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tvd12.ezyfox.util.EzyCloseable;

public interface EzyRedisClient extends EzyCloseable {

	void del(byte[] key);

	byte[] hget(byte[] key, byte[] field);
	
	Set<byte[]> hkeys(byte[] key);
	
	long hlen(byte[] key);
	
	void hset(byte[] key, byte[] field, byte[] value);
	
	void hdel(byte[] key, byte[] field);

	Long hdel(byte[] key, byte[]... fields);
	
	Map<byte[], byte[]> hgetAll(byte[] key);
	
	List<byte[]> hmget(byte[] key, byte[]... fields);
	
	void hmset(byte[] key, Map<byte[], byte[]> values);
	
	Long hincrBy(byte[] key, byte[] field, long value);
	
	Long publish(byte[] channelName, byte[] message);
	
	void subscribe(byte[] channelName, EzyRedisSubscriber subscriber);
	
	void close();

}
