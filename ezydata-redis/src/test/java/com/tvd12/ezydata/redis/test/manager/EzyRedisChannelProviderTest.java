package com.tvd12.ezydata.redis.test.manager;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.testng.annotations.Test;

import com.tvd12.ezydata.redis.EzyRedisChannel;
import com.tvd12.ezydata.redis.factory.EzyRedisChannelFactory;
import com.tvd12.ezydata.redis.manager.EzyRedisChannelProvider;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodUtil;

public class EzyRedisChannelProviderTest {

    @Test
    @SuppressWarnings("unchecked")
    public void newChannel2Times() {
        // given
        EzyRedisChannelFactory channelFactory = mock(EzyRedisChannelFactory.class);
        EzyRedisChannel<Object> channel = mock(EzyRedisChannel.class);
        
        when(channelFactory.newChannel("test")).thenReturn(channel);
        
        EzyRedisChannelProvider sut = EzyRedisChannelProvider.builder()
            .channelFactory(channelFactory)
            .build();
        
        // when
        EzyRedisChannel<Object> channel1 = sut.getChannel("test");
        EzyRedisChannel<Object> channel2 = MethodUtil.invokeMethod("newChannel", sut, "test");
        
        // then
        Asserts.assertEquals(channel1, channel2);
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void newChannel2TimesWithType() {
        // given
        EzyRedisChannelFactory channelFactory = mock(EzyRedisChannelFactory.class);
        EzyRedisChannel<Object> channel = mock(EzyRedisChannel.class);
        
        when(channelFactory.newChannel("test", Object.class)).thenReturn(channel);
        
        EzyRedisChannelProvider sut = EzyRedisChannelProvider.builder()
            .channelFactory(channelFactory)
            .build();
        
        // when
        EzyRedisChannel<Object> channel1 = sut.getChannel("test", Object.class);
        EzyRedisChannel<Object> channel2 = MethodUtil.invokeMethod("newChannel", sut, "test", Object.class);
        
        // then
        Asserts.assertEquals(channel1, channel2);
    }
}
