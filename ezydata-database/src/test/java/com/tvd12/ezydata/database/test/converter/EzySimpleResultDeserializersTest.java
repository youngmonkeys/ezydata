package com.tvd12.ezydata.database.test.converter;

import org.testng.annotations.Test;

import com.tvd12.ezydata.database.converter.EzySimpleResultDeserializers;
import com.tvd12.test.assertion.Asserts;

public class EzySimpleResultDeserializersTest {

    @Test
    public void deserializeError() {
        // given
        EzySimpleResultDeserializers sut = new EzySimpleResultDeserializers();
        
        // when
        Throwable e = Asserts.assertThrows(() ->
            sut.deserialize(this, getClass())
        );
        
        // then
        Asserts.assertEqualsType(e, IllegalArgumentException.class);
    }
}
