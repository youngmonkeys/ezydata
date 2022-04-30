package com.tvd12.ezydata.database.test.query;

import com.tvd12.ezydata.database.query.EzyQueryCondition;
import com.tvd12.ezydata.database.query.EzyQueryConditionGroup;
import com.tvd12.ezydata.database.query.EzyQueryOperation;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

import java.util.Collections;

public class EzyQueryConditionGroupTest {

    @Test
    public void test() {
        // given
        EzyQueryCondition condition = new EzyQueryCondition(
            "field",
            EzyQueryOperation.EQUAL
        );

        // when
        EzyQueryConditionGroup sut = EzyQueryConditionGroup.builder()
            .addCondition(condition)
            .build();

        // then
        Asserts.assertEquals(
            sut.getConditions(),
            Collections.singletonList(condition),
            false
        );
    }
}
