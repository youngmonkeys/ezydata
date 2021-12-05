package com.tvd12.ezydata.redis.test.manager;

import static org.mockito.Mockito.*;

import java.util.Map;

import org.testng.annotations.Test;

import com.tvd12.ezydata.redis.EzyRedisMap;
import com.tvd12.ezydata.redis.factory.EzyRedisMapFactory;
import com.tvd12.ezydata.redis.manager.EzyRedisMapProvider;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodUtil;

public class EzyRedisMapProviderTest {

    @SuppressWarnings("unchecked")
    @Test
    public void getMap2TimesTest() {
        // given
        EzyRedisMapFactory mapFactory = mock(EzyRedisMapFactory.class);
        
        EzyRedisMap<String, String> map = mock(EzyRedisMap.class);
        when(mapFactory.newMap("test", String.class, String.class)).thenReturn(map);
        
        EzyRedisMapProvider sut = EzyRedisMapProvider.builder()
            .mapFactory(mapFactory)
            .build();
        
        // when
        Map<String, String> map1 = sut.getMap("test", String.class, String.class);
        Map<String, String> map2 = sut.getMap("test", String.class, String.class);
        
        // then
        Asserts.assertEquals(map1, map2);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void newMap2TimesTest() {
        // given
        EzyRedisMapFactory mapFactory = mock(EzyRedisMapFactory.class);
        
        EzyRedisMap<String, String> map = mock(EzyRedisMap.class);
        when(mapFactory.newMap("test", String.class, String.class)).thenReturn(map);
        
        EzyRedisMapProvider sut = EzyRedisMapProvider.builder()
            .mapFactory(mapFactory)
            .build();
        
        // when
        Map<String, String> map1 = sut.getMap("test", String.class, String.class);
        Map<String, String> map2 = MethodUtil.invokeMethod("newMap", sut, "test");
        
        // then
        Asserts.assertEquals(map1, map2);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void newMapWithKeyValueType2TimesTest() {
        // given
        EzyRedisMapFactory mapFactory = mock(EzyRedisMapFactory.class);
        
        EzyRedisMap<String, String> map = mock(EzyRedisMap.class);
        when(mapFactory.newMap("test", String.class, String.class)).thenReturn(map);
        
        EzyRedisMapProvider sut = EzyRedisMapProvider.builder()
            .mapFactory(mapFactory)
            .build();
        
        // when
        Map<String, String> map1 = sut.getMap("test", String.class, String.class);
        Map<String, String> map2 = MethodUtil.invokeMethod("newMap", sut, "test", String.class, String.class);
        
        // then
        Asserts.assertEquals(map1, map2);
    }
}
