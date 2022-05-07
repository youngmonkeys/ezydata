package com.tvd12.ezydata.redis.test;

import com.tvd12.ezydata.redis.EzyRedisClient;
import com.tvd12.ezydata.redis.EzyRedisClientPool;
import com.tvd12.ezydata.redis.EzyRedisProxyFactory;
import com.tvd12.ezydata.redis.setting.EzyRedisSettingsBuilder;
import com.tvd12.ezyfox.naming.EzyNamingCase;
import com.tvd12.ezyfox.naming.EzySimpleNameTranslator;
import com.tvd12.ezyfox.util.EzyThreads;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.FieldUtil;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.Test;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.tvd12.ezydata.redis.setting.EzyRedisSettings.MAX_CONNECTION_ATTEMPTS;
import static org.mockito.Mockito.*;

public class EzyRedisProxyFactoryTest {

    @Test
    public void prepareSettingsWithReflectionIsNullTest() {
        // given
        EzyRedisProxyFactory factory = new EzyRedisProxyFactory.Builder()
            .settingsBuilder(new EzyRedisSettingsBuilder())
            .build();

        // when
        // then
        assert factory.newRedisProxy() != null;
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void prepareSettingsWithErrorEntityTest() {
        new EzyRedisProxyFactory.Builder()
            .scan("com.tvd12.ezydata.redis.test.error_entity")
            .build();
    }

    @Test
    public void prepareMapNameTranslator2TimesTest() {
        // given
        EzyRedisProxyFactory.Builder factoryBuilder = new EzyRedisProxyFactory.Builder()
            .settingsBuilder(new EzyRedisSettingsBuilder());

        // when
        // then
        factoryBuilder.build();
        factoryBuilder.build();
    }

    @Test
    public void builderTest() {
        // given
        EzyNamingCase namingCase = RandomUtil.randomEnumValue(
            EzyNamingCase.class
        );
        String ignoredSuffix = RandomUtil.randomShortAlphabetString();

        // when
        EzyRedisProxyFactory.Builder sut = EzyRedisProxyFactory.builder()
            .mapNameTranslator(namingCase, ignoredSuffix);

        // then
        Asserts.assertEquals(
            FieldUtil.getFieldValue(sut, "mapNameTranslator"),
            EzySimpleNameTranslator.builder()
                .namingCase(namingCase)
                .ignoredSuffix(ignoredSuffix)
                .build()
        );
    }

    @Test
    public void getRedisClientRetryTest() {
        // given
        EzyRedisClientPool clientPool = mock(EzyRedisClientPool.class);
        EzyRedisClient redisClient = mock(EzyRedisClient.class);

        AtomicInteger count = new AtomicInteger();
        when(clientPool.getClient()).thenAnswer(it -> {
            if (count.incrementAndGet() >= 2) {
                return redisClient;
            }
            throw new IllegalStateException("just test");
        });
        Properties properties = new Properties();
        properties.put(MAX_CONNECTION_ATTEMPTS, Integer.MAX_VALUE);
        EzyRedisProxyFactory factory = new EzyRedisProxyFactory.Builder()
            .properties(properties)
            .settingsBuilder(new EzyRedisSettingsBuilder())
            .clientPool(clientPool)
            .build();

        // when
        // then
        assert factory.newRedisProxy() != null;
        verify(clientPool, times(2)).getClient();
    }

    @Test
    public void getRedisClientRetryButMaxConnectionAttemptsWasNotSetTest() {
        // given
        EzyRedisClientPool clientPool = mock(EzyRedisClientPool.class);

        Exception exception = new IllegalStateException("just test");
        when(clientPool.getClient()).thenAnswer(it -> {
            throw exception;
        });
        EzyRedisProxyFactory factory = new EzyRedisProxyFactory.Builder()
            .settingsBuilder(new EzyRedisSettingsBuilder())
            .clientPool(clientPool)
            .build();

        // when
        Throwable e = Asserts.assertThrows(factory::newRedisProxy);

        // then
        Asserts.assertEquals(e, exception);
        verify(clientPool, times(1)).getClient();
    }

    @Test
    public void getRedisClientInterruptTest() {
        // given
        EzyRedisClientPool clientPool = mock(EzyRedisClientPool.class);

        when(clientPool.getClient()).thenAnswer(it -> {
            throw new IllegalStateException("just test");
        });
        Properties properties = new Properties();
        properties.put(MAX_CONNECTION_ATTEMPTS, Integer.MAX_VALUE);
        EzyRedisProxyFactory factory = new EzyRedisProxyFactory.Builder()
            .properties(properties)
            .settingsBuilder(new EzyRedisSettingsBuilder())
            .clientPool(clientPool)
            .build();

        // when
        AtomicReference<Throwable> e = new AtomicReference<>();
        Thread newThread = new Thread(() -> {
            try {
                factory.newRedisProxy();
            } catch (Exception ex) {
                e.set(ex);
            }
        });
        newThread.start();
        EzyThreads.sleep(300);
        newThread.interrupt();

        // then
        while (e.get() == null) {
            EzyThreads.sleep(3);
        }
        Asserts.assertEqualsType(e.get(), IllegalStateException.class);
        verify(clientPool, times(1)).getClient();
    }
}
