package com.tvd12.ezydata.hazelcast.testing.map;

import com.tvd12.ezydata.hazelcast.map.EzyMaxIdOneEntryProcessor;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyMaxIdOneEntryProcessorTest extends BaseTest {

    @Test
    public void test() throws Exception {
        EzyMaxIdOneEntryProcessor processor = new EzyMaxIdOneEntryProcessor();
        processor.writeData(null);
        processor.readData(null);
    }
}
