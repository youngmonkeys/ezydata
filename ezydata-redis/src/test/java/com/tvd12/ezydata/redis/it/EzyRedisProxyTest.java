package com.tvd12.ezydata.redis.it;

import org.testng.annotations.Test;

import com.tvd12.ezydata.redis.EzyJedisClientPool;
import com.tvd12.ezydata.redis.EzyRedisAtomicLong;
import com.tvd12.ezydata.redis.EzyRedisChannel;
import com.tvd12.ezydata.redis.EzyRedisClientPool;
import com.tvd12.ezydata.redis.EzyRedisMap;
import com.tvd12.ezydata.redis.EzyRedisProxy;
import com.tvd12.ezydata.redis.EzyRedisProxyFactory;
import com.tvd12.ezydata.redis.setting.EzyRedisSettings;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.util.EzyMapBuilder;

import redis.clients.jedis.JedisPool;

public class EzyRedisProxyTest extends EzyRedisBaseTest {

	@SuppressWarnings("unchecked")
	@Test
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
		map.clear();
		map.put("a", 1);
		map.put("b", 2);
		System.out.println("a = " + map.get("a"));
		map.putAll(EzyMapBuilder.mapBuilder()
				.put("c", 3)
				.put("d", 4)
				.put("e", 5)
				.build());
		assert map.size() == 5;
		assert map.sizeLong() == 5;
		assert map.get(Sets.newHashSet("a", "b", "c", "no one")).size() == 3;
		assert map.containsKey("a");
		assert !map.containsKey("no one");
		assert map.containsValue(1);
		assert !map.containsValue(100);
		assert map.remove("a") == null;
		assert map.size() == 4;
		assert map.remove("no one") == null;
		assert map.remove(Sets.newHashSet("b", "c", "no one")) == 2;
		assert map.size() == 2;
		assert map.keySet().size() == 2;
		assert map.values().size() == 2;
		assert map.entrySet().size() == 2;
		assert !map.isEmpty();
		assert map.getName().equals("ezydata.key_value");
		map.clear();
		assert map.isEmpty();
		EzyRedisAtomicLong atomicLong = proxy.getAtomicLong("ezydata.test_atomic_long");
		long atomicLongCurrentValue = atomicLong.get();
		System.out.println("atomicLongCurrentValue: " + atomicLongCurrentValue);
		assert atomicLong.incrementAndGet() == atomicLongCurrentValue + 1;
		assert atomicLong.addAndGet(3) == atomicLongCurrentValue + 1 + 3;
		assert atomicLong.getName().equals("ezydata.test_atomic_long");
		System.out.println(atomicLong.incrementAndGet());
		System.out.println("hello: " + atomicLong.addAndGet(3));
		EzyRedisChannel<String> channel2 = proxy2.getChannel("ezydata.simple_channel");
		channel2.addSubscriber(m -> {
			System.out.println("received message: " + m);
		});
		Thread.sleep(100);
		System.out.println("listening");
		EzyRedisChannel<String> channel = proxy.getChannel("ezydata.simple_channel");
		channel.publish("I'm a monkey");
		System.out.println("publish ok");
		Thread.sleep(1000);
	}
	
	public static void main(String[] args) throws Exception {
		new EzyRedisProxyTest().test();
	}
	
}
