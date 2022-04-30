package com.tvd12.ezydata.database.test.query;

import com.tvd12.ezydata.database.query.EzyQueryData;
import com.tvd12.ezyfox.util.EzyMapBuilder;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class EzyQueryDataTest {

    @Test
    public void test() {
        // given
        EzyQueryData sut = EzyQueryData.builder()
            .query("youngmonkeys")
            .parameter("name", "hello")
            .parameter("value", "world")
            .parameters(Collections.singletonMap("foo", "bar"))
            .parameters(Arrays.asList("young", null, "monkeys"))
            .parameterCount(11)
            .parameter(2, true)
            .parameter(3, (byte) 1)
            .parameter(4, 'a')
            .parameter(5, 2.0D)
            .parameter(6, 3.0F)
            .parameter(7, 4)
            .parameter(8, 5L)
            .parameter(9, (short) 6)
            .parameter(10, "7")
            .parameterConverter(it -> it)
            .build();

        // when
        Map<String, Object> paramMap = sut.getParameterMap();
        Object[] params = sut.getParameters();

        // then
        Asserts.assertEquals(sut.getQuery(), "youngmonkeys");
        Map<String, Object> expectedParamMap = EzyMapBuilder.mapBuilder()
            .put("name", "hello")
            .put("value", "world")
            .put("foo", "bar")
            .toMap();
        Asserts.assertEquals(paramMap, expectedParamMap);

        Object[] expectedParams = new Object[]{
            "young",
            "monkeys",
            true,
            (byte) 1,
            'a',
            2.0D,
            3.0F,
            4,
            5L,
            (short) 6,
            "7"
        };
        Asserts.assertEquals(params, expectedParams);
    }

    @Test
    public void parameterMapTest() {
        // given
        EzyQueryData sut = EzyQueryData.builder()
            .parameters(Collections.singletonMap("name", "hello"))
            .parameters(Collections.singletonMap("value", "world"))
            .parameters(Collections.singletonMap("foo", "bar"))
            .build();

        // when
        Map<String, Object> paramMap = sut.getParameterMap();

        // then
        Map<String, Object> expectedParamMap = EzyMapBuilder.mapBuilder()
            .put("name", "hello")
            .put("value", "world")
            .put("foo", "bar")
            .toMap();
        Asserts.assertEquals(paramMap, expectedParamMap);
        Asserts.assertEquals(sut.getParameters(), new Object[0]);
    }

    @Test
    public void defaultValueTest() {
        // given
        EzyQueryData sut = EzyQueryData.builder()
            .build();

        // when
        // then
        Asserts.assertEquals(sut.getParameterMap(), Collections.emptyMap());
        Asserts.assertEquals(sut.getParameters(), new Object[0]);
    }
}
