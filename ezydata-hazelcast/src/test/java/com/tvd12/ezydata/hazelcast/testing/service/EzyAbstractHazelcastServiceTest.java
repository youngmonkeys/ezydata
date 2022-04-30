package com.tvd12.ezydata.hazelcast.testing.service;

import com.tvd12.ezydata.hazelcast.service.EzyAbstractHazelcastService;
import com.tvd12.ezydata.hazelcast.testing.HazelcastBaseTest;
import org.testng.annotations.Test;

public class EzyAbstractHazelcastServiceTest extends HazelcastBaseTest {

    @Test
    public void test() throws Exception {
        MyService service = new MyService() {};
        service.setHazelcastInstance(HZ_INSTANCE);
        service.init();
    }

    public static class MyService extends EzyAbstractHazelcastService {}
}
