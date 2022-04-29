package com.tvd12.ezydata.database.test.query;

import com.tvd12.ezydata.database.query.EzyQueryConditionBuilder;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

public class EzyQueryConditionBuilderTest {


    @Test
    public void test() {
        // given
        EzyQueryConditionBuilder sut = new EzyQueryConditionBuilder()
            .append("e.id > :id")
            .and("e.name > :name")
            .or("e.value > :value");

        // when
        String actual = sut.toString();

        // then
        String expectation = "e.id > :id AND e.name > :name OR e.value > :value";
        Asserts.assertEquals(actual, expectation);
    }

    @Test
    public void andTest() {
        // given
        EzyQueryConditionBuilder sut = new EzyQueryConditionBuilder()
            .and("e.id > :id")
            .and("e.name > :name");

        // when
        String actual = sut.toString();

        // then
        String expectation = "e.id > :id AND e.name > :name";
        Asserts.assertEquals(actual, expectation);
    }

    @Test
    public void orTest() {
        // given
        EzyQueryConditionBuilder sut = new EzyQueryConditionBuilder()
            .or("e.name > :name")
            .or("e.value > :value");

        // when
        String actual = sut.build();

        // then
        String expectation = "e.name > :name OR e.value > :value";
        Asserts.assertEquals(actual, expectation);
    }
}
