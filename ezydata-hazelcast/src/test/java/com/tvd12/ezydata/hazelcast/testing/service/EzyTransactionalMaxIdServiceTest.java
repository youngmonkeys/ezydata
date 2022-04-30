package com.tvd12.ezydata.hazelcast.testing.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.transaction.TransactionalMap;
import com.tvd12.ezydata.hazelcast.constant.EzyMapNames;
import com.tvd12.ezydata.hazelcast.factory.EzyMapTransactionFactory;
import com.tvd12.ezydata.hazelcast.service.EzyTransactionalMaxIdService;
import com.tvd12.ezydata.hazelcast.testing.HazelcastBaseTest;
import com.tvd12.ezydata.hazelcast.transaction.EzyMapApplyTransaction;
import com.tvd12.ezydata.hazelcast.transaction.EzyMapReturnTransaction;
import com.tvd12.ezydata.hazelcast.transaction.EzyTransactionOptions;
import com.tvd12.ezyfox.function.EzyExceptionFunction;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EzyTransactionalMaxIdServiceTest extends HazelcastBaseTest {

    @Test
    public void test() throws Exception {
        final EzyTransactionalMaxIdService service =
            new EzyTransactionalMaxIdService(HZ_INSTANCE);
        service.setMapTransactionFactory(MAP_TRANSACTION_FACTORY);

        List<Long> nums = new ArrayList<>();
        Thread[] threads = new Thread[1000];
        for (int i = 0; i < threads.length; ++i) {
            threads[i] = new Thread(() ->
                nums.add(service.incrementAndGet("somethingy"))
            );
        }
        for (Thread thread : threads) {
            thread.start();
        }

        Thread.sleep(2000L);

        System.out.println(nums);
        for (int i = 0; i < nums.size() - 1; ++i) {
            if (nums.get(i + 1) != nums.get(i) + 1) {
                System.err.println("transaction xxx: error in " + i);
            }
        }
    }

    @Test
    public void test11() throws Exception {
        final EzyTransactionalMaxIdService service = new EzyTransactionalMaxIdService(HZ_INSTANCE);
        service.setMapTransactionFactory(MAP_TRANSACTION_FACTORY);

        List<Long> nums = new ArrayList<>();
        Thread[] threads = new Thread[1000];
        for (int i = 0; i < threads.length; ++i) {
            threads[i] = new Thread(() ->
                nums.add(service.incrementAndGet("somethingx", 2))
            );
        }
        for (Thread thread : threads) {
            thread.start();
        }

        Thread.sleep(2000L);

        System.out.println(nums);
        for (int i = 0; i < nums.size() - 1; ++i) {
            if (nums.get(i + 1) != nums.get(i) + 2) {
                System.err.println("transaction yyy: error in " + i);
            }
        }
    }

    @Test
    public void test2() throws Exception {
        EzyTransactionalMaxIdService service = new EzyTransactionalMaxIdService() {
            @Override
            protected String getMapName() {
                return EzyMapNames.MAX_ID + "_failed_transaction";
            }
        };
        service.setHazelcastInstance(HZ_INSTANCE);
        service.setMapTransactionFactory(new EzyMapTransactionFactory() {

            @Override
            public <K, V, R> EzyMapReturnTransaction<K, V, R> newReturnTransaction(String mapName,
                                                                                   EzyTransactionOptions options) {
                return new EzyMapReturnTransaction<K, V, R>() {
                    @Override
                    public R apply(EzyExceptionFunction<TransactionalMap<K, V>, R> func) {
                        throw new RuntimeException();
                    }

                    @Override
                    public void begin() {}

                    @Override
                    public void commit() {}

                    @Override
                    public void rollback() {}
                };
            }

            @Override
            public <K, V> EzyMapApplyTransaction<K, V> newApplyTransaction(String mapName, EzyTransactionOptions options) {
                return null;
            }
        });
        try {
            service.incrementAndGet("d");
        } catch (Exception e) {
            e.printStackTrace();
            assert e instanceof IllegalStateException;
        }
        try {
            service.incrementAndGet("d", 100);
        } catch (Exception e) {
            e.printStackTrace();
            assert e instanceof IllegalStateException;
        }
        Thread.sleep(1000);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void test3() {
        IMap map = mock(IMap.class);
        HazelcastInstance hzInstance = mock(HazelcastInstance.class);
        when(hzInstance.getMap(anyString())).thenReturn(map);
        EzyTransactionalMaxIdService service = new EzyTransactionalMaxIdService(hzInstance);
        service.setMapTransactionFactory(MAP_TRANSACTION_FACTORY);
        service.loadAll();
    }
}
