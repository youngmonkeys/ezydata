package com.tvd12.ezydata.redis.test;

import com.tvd12.ezydata.redis.EzyJedisClientPool;
import com.tvd12.ezydata.redis.EzyRedisChannel;
import com.tvd12.ezydata.redis.EzyRedisClientPool;
import com.tvd12.ezydata.redis.EzyRedisMap;
import com.tvd12.ezydata.redis.EzyRedisProxy;
import com.tvd12.ezydata.redis.EzyRedisProxyFactory;
import com.tvd12.ezydata.redis.setting.EzyRedisSettings;

import redis.clients.jedis.JedisPool;

public class EzyRedisProxyTest extends EzyRedisBaseTest {

//	@Test
	public void test() throws Exception {
		EzyRedisClientPool clientPool = new EzyJedisClientPool(new JedisPool());
		EzyRedisSettings settings = EzyRedisSettings.builder()
				.mapSettingBuilder("ezydata.key_value")
					.keyType(String.class)
					.valueType(int.class)
					.parent()
				.channelSettingBuilder("ezydata.simple_channel")
					.messageType(String.class)
					.parent()
				.build();
		EzyRedisProxyFactory factory = EzyRedisProxyFactory.builder()
				.settings(settings)
				.clientPool(clientPool)
				.entityCodec(ENTITY_CODEC)
				.build();
		EzyRedisProxy proxy = factory.newRedisProxy();
		EzyRedisProxy proxy2 = factory.newRedisProxy();
		EzyRedisMap<String, Integer> map = proxy.getMap("ezydata.key_value");
		map.put("a", 1);
		System.out.println("a = " + map.get("a"));
		EzyRedisChannel<String> channel2 = proxy2.getChannel("ezydata.simple_channel");
		channel2.addSubscriber(m -> {
			System.out.println("received message: " + m);
		});
		Thread.sleep(100);
		System.out.println("listening");
		EzyRedisChannel<String> channel = proxy.getChannel("ezydata.simple_channel");
		channel.publish("I'm a monkey");
		System.out.println("publish ok");
		Thread.sleep(100);
	}
	
	public static void main(String[] args) throws Exception {
		new EzyRedisProxyTest().test();
	}
	
}
