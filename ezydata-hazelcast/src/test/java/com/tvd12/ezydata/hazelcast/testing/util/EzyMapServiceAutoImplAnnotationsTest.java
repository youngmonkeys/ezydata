package com.tvd12.ezydata.hazelcast.testing.util;

import com.tvd12.ezydata.hazelcast.annotation.EzyMapServiceAutoImpl;
import com.tvd12.ezydata.hazelcast.util.EzyMapServiceAutoImplAnnotations;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

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
    public interface A {}

    @EzyMapServiceAutoImpl(value = "d")
    public interface B {}
}

