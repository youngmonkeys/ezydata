package com.tvd12.ezydata.hazelcast.testing.service;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.tvd12.ezydata.hazelcast.service.EzyEntryProcessorMaxIdService;
import com.tvd12.ezydata.hazelcast.testing.HazelcastBaseTest;

public class EzyEntryProcessorMaxIdServiceTest extends HazelcastBaseTest {

    @Test
    public void test() throws Exception {
        final EzyEntryProcessorMaxIdService service = new EzyEntryProcessorMaxIdService(HZ_INSTANCE);
        service.setMapTransactionFactory(MAP_TRANSACTION_FACTORY);

        List<Long> nums = new ArrayList<>();
        Thread[] threads = new Thread[1000];
        for(int i = 0 ; i < threads.length ; ++i) {
            threads[i] = new Thread(() -> {
                nums.add(service.incrementAndGet("something"));
            });
        }
        for(int i = 0 ; i < threads.length ; ++i) {
            threads[i].start();
//            threads[i].join();
        }

        Thread.sleep(2000L);

        System.out.println(nums);
        for(int i = 0 ; i < nums.size() - 1 ; ++i) {
            if(nums.get(i + 1) != nums.get(i) + 1) {
                System.err.println("entryprocessor xxx: error in " + i);
            }
        }

    }

    @Test
    public void test2() throws Exception {
        final EzyEntryProcessorMaxIdService service = new EzyEntryProcessorMaxIdService();
        service.setHazelcastInstance(HZ_INSTANCE);
        service.setMapTransactionFactory(MAP_TRANSACTION_FACTORY);

        List<Long> nums = new ArrayList<>();
        Thread[] threads = new Thread[1000];
        for(int i = 0 ; i < threads.length ; ++i) {
            threads[i] = new Thread(() -> {
                nums.add(service.incrementAndGet("somethingy", 2));
            });
        }
        for(int i = 0 ; i < threads.length ; ++i) {
            threads[i].start();
//            threads[i].join();
        }

        Thread.sleep(3000L);

        System.out.println(nums);
        try {
            for(int i = 0 ; i < nums.size() - 1 ; ++i) {
                if(nums.get(i + 1) != nums.get(i) + 2) {
                    System.err.println("entryprocessor yyy: error in " + i);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void test3() {
        IMap map = mock(IMap.class);
        HazelcastInstance hzInstance = mock(HazelcastInstance.class);
        when(hzInstance.getMap(anyString())).thenReturn(map);
        EzyEntryProcessorMaxIdService service = new EzyEntryProcessorMaxIdService(hzInstance);
        service.setMapTransactionFactory(MAP_TRANSACTION_FACTORY);
        service.loadAll();
    }

}
