package com.tvd12.ezydata.database.test.query;

import com.tvd12.ezydata.database.query.EzyQueryOperation;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

public class EzyQueryOperationTest {

    @Test
    public void test() {
        Asserts.assertEquals(EzyQueryOperation.LTE.getSign(), "<=");
        Asserts.assertEquals(EzyQueryOperation.LTE.getSignName(), "lte");
    }
}
