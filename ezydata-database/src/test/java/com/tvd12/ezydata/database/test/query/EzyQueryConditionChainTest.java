package com.tvd12.ezydata.database.test.query;

import com.tvd12.ezydata.database.query.EzyQueryConditionChain;
import com.tvd12.ezydata.database.query.EzyQueryConditionGroup;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

public class EzyQueryConditionChainTest {

    @Test
    public void test() {
        // given
        List<EzyQueryConditionGroup> conditionGroups = Collections.singletonList(
            EzyQueryConditionGroup.builder().build()
        );

        EzyQueryConditionChain sut = new EzyQueryConditionChain(conditionGroups);

        // when
        // then
        Asserts.assertEquals(sut.size(), 1);
    }

    @Test
    public void builderTest() {
        // given
        EzyQueryConditionGroup queryConditionGroup = EzyQueryConditionGroup.builder()
            .build();

        // when
        EzyQueryConditionChain sut = EzyQueryConditionChain.builder()
            .addConditionGroup(queryConditionGroup)
            .build();

        // then
        Asserts.assertEquals(
            sut.getConditionGroups(),
            Collections.singletonList(queryConditionGroup),
            false
        );
    }
}
