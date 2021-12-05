package com.tvd12.ezydata.database.test.query;

import org.testng.annotations.Test;

import com.tvd12.ezydata.database.query.EzyQueryString;
import com.tvd12.test.assertion.Asserts;

public class EzyQueryStringTest {

    @Test
    public void test() {
        // given
        EzyQueryString sut = new EzyQueryString("test");
        
        // when
        // then
        Asserts.assertFalse(sut.isNativeQuery());
    }
}
