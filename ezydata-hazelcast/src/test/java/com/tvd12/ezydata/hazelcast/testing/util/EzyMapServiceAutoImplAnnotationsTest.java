package com.tvd12.ezydata.hazelcast.testing.util;

import org.testng.annotations.Test;

import com.tvd12.ezydata.hazelcast.annotation.EzyMapServiceAutoImpl;
import com.tvd12.ezydata.hazelcast.util.EzyMapServiceAutoImplAnnotations;
import com.tvd12.test.base.BaseTest;

public class EzyMapServiceAutoImplAnnotationsTest extends BaseTest {
    
    @Override
    public Class<?> getTestClass() {
        return EzyMapServiceAutoImplAnnotations.class;
    }
    
    @Test
    public void test() {
        assert EzyMapServiceAutoImplAnnotations.getBeanName(A.class).equals("x");
        assert EzyMapServiceAutoImplAnnotations.getBeanName(B.class).equals("b");
    }
    
    @EzyMapServiceAutoImpl(value = "d", name = "x")
    public static interface A {
        
    }
    
    @EzyMapServiceAutoImpl(value = "d")
    public static interface B {
        
    }
}

