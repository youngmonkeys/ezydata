package com.tvd12.ezydata.hazelcast.testing;

import org.testng.annotations.Test;

import com.tvd12.ezydata.hazelcast.impl.EzySimpleMapNameFetcher;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;
import com.tvd12.ezyfox.annotation.EzyKeyValue;

public class EzySimpleMapNameFetcherTest {

    @Test
    public void test1() {
        try {
            new EzySimpleMapNameFetcher().getMapName(Object.class);
        }
        catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    public void test2() {
        try {
            new EzySimpleMapNameFetcher().getMapName(InterfaceA.class);
        }
        catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    public void test3() {
        try {
            new EzySimpleMapNameFetcher().getMapName(InterfaceB.class);
        }
        catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @EzyAutoImpl(properties = {
    })
    public static interface InterfaceA {

    }

    @EzyAutoImpl(properties = {
            @EzyKeyValue(key = "key", value = "value")
    })
    public static interface InterfaceB {

    }

}
