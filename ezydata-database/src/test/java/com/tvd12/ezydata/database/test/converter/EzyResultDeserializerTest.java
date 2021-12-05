package com.tvd12.ezydata.database.test.converter;

import static org.mockito.Mockito.mock;

import org.testng.annotations.Test;

import com.tvd12.ezydata.database.converter.EzyResultDeserializer;
import com.tvd12.ezydata.database.converter.EzyResultDeserializers;
import com.tvd12.ezyfox.database.annotation.EzyResultDeserialized;
import com.tvd12.test.assertion.Asserts;

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
    
    private static class Data {}
    
    private static class Repo implements EzyResultDeserializer<Data> {
    }
    
    @SuppressWarnings("rawtypes")
    @EzyResultDeserialized(Data.class)
    private static class Repo2 implements EzyResultDeserializer {
    }
    
    @SuppressWarnings("rawtypes")
    private static class Repo3 implements EzyResultDeserializer {
    }
}
