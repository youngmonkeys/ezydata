package com.tvd12.ezydata.redis.test;

import com.tvd12.ezydata.redis.EzyRedisChannel;
import com.tvd12.ezydata.redis.EzyRedisClient;
import com.tvd12.ezydata.redis.setting.EzyRedisChannelSetting;
import org.testng.annotations.Test;

import java.util.function.Consumer;

import static org.mockito.Mockito.*;

public class EzyRedisChannelTest {

    @SuppressWarnings("unchecked")
    @Test
    public void addSubscriberButSubscribed() throws Exception {
        // given
        EzyRedisChannelSetting setting = mock(EzyRedisChannelSetting.class);
        when(setting.getSubThreadPoolSize()).thenReturn(1);

        EzyRedisClient redisClient = mock(EzyRedisClient.class);

        EzyRedisChannel<String> sut = EzyRedisChannel.builder()
            .setting(setting)
            .channelName("test")
            .redisClient(redisClient)
            .build();

        Consumer<String> consumer = mock(Consumer.class);

        // when
        sut.addSubscriber(consumer);
        sut.addSubscriber(consumer);
        Thread.sleep(100);

        // then
        verify(redisClient, times(1)).subscribe(any(), any());
    }
}
