package com.tvd12.ezydata.database.test.query;

import org.testng.annotations.Test;

import com.tvd12.ezydata.database.query.EzyQueryOperation;
import com.tvd12.test.assertion.Asserts;

public class EzyQueryOperationTest {

    @Test
    public void test() {
        Asserts.assertEquals(EzyQueryOperation.LTE.getSign(), "<=");
        Asserts.assertEquals(EzyQueryOperation.LTE.getSignName(), "lte");
    }
}
