package com.tvd12.ezydata.redis.test.loader;

import org.testng.annotations.Test;

import com.tvd12.ezydata.redis.EzyRedisClientPool;
import com.tvd12.ezydata.redis.loader.EzyJedisClientPoolLoader;
import com.tvd12.ezydata.redis.loader.EzyRedisClientPoolLoader;

public class EzyJedisClientPoolLoaderTest {

	@Test
	public void testAll() {
		// given
		EzyJedisClientPoolLoader sut1 = new EzyJedisClientPoolLoader();
		EzyJedisClientPoolLoader sut2 = new EzyJedisClientPoolLoader()
				.host("localhost");
		EzyJedisClientPoolLoader sut3 = new EzyJedisClientPoolLoader()
				.host("remotehost")
				.port(3005);
		
		// when
		EzyRedisClientPool pool1 = sut1.load();
		EzyRedisClientPool pool2 = sut2.load();
		EzyRedisClientPool pool3 = sut3.load();
		
		assert pool1 != null;
		assert pool2 != null;
		assert pool3 != null;
	}
	
	@Test
	public void testForURI() {
		// given
		EzyJedisClientPoolLoader sut = new EzyJedisClientPoolLoader()
				.host("localhost")
				.port(6379)
				.uri("redis://localhost:6379/")
				.property(EzyRedisClientPoolLoader.HOST, "localhost")
				.property(EzyRedisClientPoolLoader.PORT, "6379");
		
		
		// when
		EzyRedisClientPool pool = sut.load();
		
		assert pool != null;
	}
	
}
