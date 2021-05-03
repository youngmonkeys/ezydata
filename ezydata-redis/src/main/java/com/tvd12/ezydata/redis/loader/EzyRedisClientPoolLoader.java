package com.tvd12.ezydata.redis.loader;

import com.tvd12.ezydata.redis.EzyRedisClientPool;

public interface EzyRedisClientPoolLoader {

	String HOST = "redis.host";
	String PORT = "redis.port";
	String URI = "redis.uri";
	
	int DEFAULT_PORT = 6379;
	
	EzyRedisClientPool load();
	
}
