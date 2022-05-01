package com.tvd12.ezydata.redis.test;

import com.tvd12.ezydata.redis.*;
import com.tvd12.ezydata.redis.setting.EzyRedisSettings;
import com.tvd12.ezydata.redis.test.entity.Author;
import com.tvd12.ezydata.redis.test.entity.ChatMessage3;
import com.tvd12.ezydata.redis.test.entity.ChatMessage4;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfox.util.EzyMapBuilder;
import com.tvd12.properties.file.reader.BaseFileReader;
import org.testng.annotations.Test;
import redis.clients.jedis.JedisPool;

import java.util.Arrays;
import java.util.Map;

public class EzyRedisProxyTest extends EzyRedisBaseTest {

    public static void main(String[] args) {
        new EzyRedisProxyTest().testWithProperties();
    }

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
        System.out.println(proxy.getMap("ezydata.key_value"));
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
        //noinspection AssertWithSideEffects
        assert map.remove("a") == null;
        assert map.size() == 4;
        //noinspection AssertWithSideEffects
        assert map.remove("no one") == null;
        assert map.remove(Sets.newHashSet("b", "c", "no one")) == 2;
        assert map.size() == 2;
        //noinspection ConstantConditions
        assert map.keySet().size() == 2;
        //noinspection ConstantConditions
        assert map.values().size() == 2;
        //noinspection ConstantConditions
        assert map.entrySet().size() == 2;
        //noinspection ConstantConditions
        assert !map.isEmpty();
        assert map.getName().equals("ezydata.key_value");
        map.clear();
        //noinspection RedundantOperationOnEmptyContainer
        assert map.get("any value") == null;
        assert map.isEmpty();
        EzyRedisAtomicLong atomicLong = proxy.getAtomicLong("ezydata.test_atomic_long");
        long atomicLongCurrentValue = atomicLong.get();
        System.out.println("atomicLongCurrentValue: " + atomicLongCurrentValue);
        assert atomicLong.incrementAndGet() == atomicLongCurrentValue + 1;
        assert atomicLong.addAndGet(3) == atomicLongCurrentValue + 1 + 3;
        assert atomicLong.getName().equals("ezydata.test_atomic_long");
        System.out.println(atomicLong.incrementAndGet());
        System.out.println("hello: " + atomicLong.addAndGet(3));
        System.out.println(proxy2.getChannel("ezydata.simple_channel"));
        EzyRedisChannel<String> channel2 = proxy2.getChannel("ezydata.simple_channel");
        proxy2.getChannel("ezydata.simple_channel");
        channel2.addSubscriber(m -> System.out.println("received message: " + m));
        Thread.sleep(100);
        System.out.println("listening");
        EzyRedisChannel<String> channel = proxy.getChannel("ezydata.simple_channel");
        System.out.println(channel.publish("I'm a monkey"));
        System.out.println("publish ok");
        Thread.sleep(1000);
        clientPool.close();
    }

    @Test
    public void testWithProperties() {
        EzyRedisProxyFactory factory = EzyRedisProxyFactory.builder()
            .properties(new BaseFileReader().read("application_test.yaml"))
            .scan("com.tvd12.ezydata.redis.test.entity")
            .scan("com.tvd12.ezydata.redis.test.entity", "com.tvd12.ezydata.redis.test.entity")
            .scan(Arrays.asList("com.tvd12.ezydata.redis.test.entity", "com.tvd12.ezydata.redis.test.entity"))
            .build();
        EzyRedisProxy proxy = factory.newRedisProxy();
        EzyRedisMap<String, Integer> map = proxy.getMap("ezydata_key_value2");
        map.clear();
        map.put("hello", 1);
        System.out.println(map.get("hello"));
        assert map.get("hello").equals(1);

        EzyRedisMap<String, String> map3 = proxy.getMap("ezydata_key_value3", String.class, String.class);
        map3.clear();
        map3.put("hello", "world");
        System.out.println(map3.get("hello"));
        assert map3.get("hello").equals("world");

        System.out.println(proxy.getMap("ezydata_author"));
        Map<Long, Author> authorMap = proxy.getMap("ezydata_author");
        authorMap.clear();
        EzyRedisAtomicLong atomicLong = proxy.getAtomicLong("ezydata_author");
        Long authorId = atomicLong.incrementAndGet();
        authorMap.put(authorId, new Author(authorId, "Author1"));
        System.out.println(authorMap.get(authorId));
        assert authorMap.get(authorId).equals(new Author(authorId, "Author1"));

        System.out.println(
            proxy.getChannel("ezydata_chat_message3", ChatMessage3.class)
        );
        EzyRedisChannel<ChatMessage3> channel =
            proxy.getChannel("ezydata_chat_message3", ChatMessage3.class);
        channel.publish(new ChatMessage3("Hello World"));

        System.out.println(
            proxy.getChannel("ezydata_chat_message4", ChatMessage4.class)
        );
        EzyRedisChannel<ChatMessage4> channel4 =
            proxy.getChannel("ezydata_chat_message4", ChatMessage4.class);
        channel4.publish(new ChatMessage4("Hello World"));
    }
}
