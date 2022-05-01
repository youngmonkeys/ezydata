package com.tvd12.ezydata.redis.test.manager;

import com.tvd12.ezydata.redis.EzyRedisAtomicLong;
import com.tvd12.ezydata.redis.factory.EzyRedisAtomicLongFactory;
import com.tvd12.ezydata.redis.manager.EzyRedisAtomicLongProvider;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodUtil;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EzyRedisAtomicLongProviderTest {

    @Test
    public void getAtomicLong2Times() {
        // given
        EzyRedisAtomicLongFactory atomicLongFactory = mock(EzyRedisAtomicLongFactory.class);
        EzyRedisAtomicLong atomicLong = mock(EzyRedisAtomicLong.class);

        when(atomicLongFactory.newAtomicLong("test")).thenReturn(atomicLong);

        EzyRedisAtomicLongProvider sut = EzyRedisAtomicLongProvider.builder()
            .atomicLongFactory(atomicLongFactory)
            .build();

        // when
        EzyRedisAtomicLong atomicLong1 = sut.getAtomicLong("test");
        EzyRedisAtomicLong atomicLong2 = sut.getAtomicLong("test");

        // then
        Asserts.assertEquals(atomicLong1, atomicLong2);
    }

    @Test
    public void newAtomicLong2Times() {
        // given
        EzyRedisAtomicLongFactory atomicLongFactory = mock(EzyRedisAtomicLongFactory.class);
        EzyRedisAtomicLong atomicLong = mock(EzyRedisAtomicLong.class);

        when(atomicLongFactory.newAtomicLong("test")).thenReturn(atomicLong);

        EzyRedisAtomicLongProvider sut = EzyRedisAtomicLongProvider.builder()
            .atomicLongFactory(atomicLongFactory)
            .build();

        // when
        EzyRedisAtomicLong atomicLong1 = sut.getAtomicLong("test");
        EzyRedisAtomicLong atomicLong2 = MethodUtil.invokeMethod("newAtomicLong", sut, "test");

        // then
        Asserts.assertEquals(atomicLong1, atomicLong2);
    }
}
