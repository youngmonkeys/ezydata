package com.tvd12.ezydata.database.test.converter;

import com.tvd12.ezydata.database.converter.EzyResultDeserializer;
import com.tvd12.ezydata.database.converter.EzyResultDeserializers;
import com.tvd12.ezyfox.database.annotation.EzyResultDeserialized;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EzyResultDeserializerTest {

    @Test
    public void test() {
        // given
        Repo sut = new Repo();

        Data data = new Data();

        EzyResultDeserializers deserializers = mock(EzyResultDeserializers.class);

        // when
        // then
        Asserts.assertEquals(sut.deserialize(data, deserializers), data);
        Asserts.assertEquals(sut.getOutType(), Data.class);
    }

    @Test
    public void getOutTypeTest() {
        // given
        Repo2 sut = new Repo2();

        // when
        // then
        Asserts.assertEquals(sut.getOutType(), Data.class);
    }

    @Test
    public void getOutTypeNullTest() {
        // given
        Repo3 sut = new Repo3();

        // when
        // then
        Asserts.assertNull(sut.getOutType());
    }

    @Test
    public void deserializeWithDeserializersTest() {
        // given
        EzyResultDeserializers deserializers = mock(EzyResultDeserializers.class);
        Object data = 1L;
        when(deserializers.deserialize(data, Long.class)).thenReturn(data);

        EzyResultDeserializer<Long> sut = new EzyResultDeserializer<Long>() {
            @Override
            public Long deserialize(Object data, EzyResultDeserializers deserializers) {
                return (Long) deserializers.deserialize(data, Long.class);
            }
        };

        // when
        Long actual = sut.deserialize(data, deserializers);

        // then
        Asserts.assertEquals(actual, data);
    }

    private static class Data {}

    private static class Repo implements EzyResultDeserializer<Data> {}

    @SuppressWarnings("rawtypes")
    @EzyResultDeserialized(Data.class)
    private static class Repo2 implements EzyResultDeserializer {}

    @SuppressWarnings("rawtypes")
    private static class Repo3 implements EzyResultDeserializer {}
}
