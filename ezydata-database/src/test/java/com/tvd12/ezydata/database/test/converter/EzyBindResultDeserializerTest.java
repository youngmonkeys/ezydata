package com.tvd12.ezydata.database.test.converter;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import com.tvd12.ezydata.database.converter.EzyBindResultDeserializer;
import com.tvd12.ezyfox.binding.EzyBindingContext;
import com.tvd12.test.assertion.Asserts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class EzyBindResultDeserializerTest {

    @Test
    public void test() {
        EzyBindingContext bindingContext = EzyBindingContext.builder()
                .addArrayBindingClass(A.class)
                .build();
        EzyBindResultDeserializer deserializer = new EzyBindResultDeserializer(
                A.class, bindingContext.newUnmarshaller()
        );
        A a = (A) deserializer.deserialize(new Object[] {"hello", 123});
        System.out.println(a);
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void deserializeIterable() {
        // given
        EzyBindingContext bindingContext = EzyBindingContext.builder()
                .addArrayBindingClass(A.class)
                .build();
        EzyBindResultDeserializer sut = new EzyBindResultDeserializer(
                A.class, bindingContext.newUnmarshaller()
        );
        
        List result = Arrays.asList("hello", 1);
        
        // when
        Object actual = sut.deserialize(result);
        
        // then
        Asserts.assertEquals(actual, new A("hello", 1));
    }
    
    @Test
    public void deserializeObject() {
        // given
        EzyBindingContext bindingContext = EzyBindingContext.builder()
                .addArrayBindingClass(A.class)
                .build();
        EzyBindResultDeserializer sut = new EzyBindResultDeserializer(
                A.class, bindingContext.newUnmarshaller()
        );
        
        // when
        Object actual = sut.deserialize("hello");
        
        // then
        Asserts.assertEquals(actual, new A("hello", 0));
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class A {
        private String name;
        private int value;
    }
    
}
