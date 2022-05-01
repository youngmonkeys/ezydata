package com.tvd12.ezydata.redis.test;

import com.tvd12.ezydata.redis.EzyJedisProxy;
import org.testng.annotations.Test;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Jedis;

import static org.mockito.Mockito.*;

public class EzyJedisProxyTest {

    @Test
    public void subscribeTest() {
        // given
        Jedis jedis = mock(Jedis.class);
        doNothing().when(jedis).subscribe(any(BinaryJedisPubSub.class), any(byte[].class));
        doNothing().when(jedis).close();
        when(jedis.toString()).thenReturn("jedis");
        EzyJedisProxy sut = new EzyJedisProxy(jedis);

        // when
        sut.subscribe("testChannel".getBytes(), (channel, messageBytes) -> {});

        // then
        sut.close();
        System.out.println(sut);
    }
}
