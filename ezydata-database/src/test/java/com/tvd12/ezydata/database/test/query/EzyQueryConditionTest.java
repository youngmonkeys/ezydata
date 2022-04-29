package com.tvd12.ezydata.database.test.query;

import com.tvd12.ezydata.database.query.EzyQueryCondition;
import com.tvd12.ezydata.database.query.EzyQueryOperation;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

public class EzyQueryConditionTest {

    @Test
    public void parseTest() {
        // given
        String str = "nameIn";

        // when
        EzyQueryCondition actual = EzyQueryCondition.parse(str);

        // then
        Asserts.assertEquals(actual.getField(), "name");
        Asserts.assertEquals(actual.getOperation(), EzyQueryOperation.IN);
    }
}
