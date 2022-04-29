package com.tvd12.ezydata.hazelcast.testing.map;

import org.testng.annotations.Test;

import com.tvd12.ezydata.hazelcast.map.EzyMaxIdEntryProcessor;
import com.tvd12.test.base.BaseTest;

public class EzyMaxIdEntryProcessorTest extends BaseTest {

    @Test
    public void test() {
        EzyMaxIdEntryProcessor processor = new EzyMaxIdEntryProcessor();
        assert processor.getDelta() == 0;
    }
    
}
