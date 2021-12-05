package com.tvd12.ezydata.database.test.query;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import com.tvd12.ezydata.database.query.EzyQueryConditionChain;
import com.tvd12.ezydata.database.query.EzyQueryConditionGroup;
import com.tvd12.test.assertion.Asserts;

public class EzyQueryConditionChainTest {

    @Test
    public void test() {
        // given
        List<EzyQueryConditionGroup> conditionGroups = Arrays.asList(
                EzyQueryConditionGroup.builder().build()
        );
        
        EzyQueryConditionChain sut = new EzyQueryConditionChain(conditionGroups);
        
        // when
        // then
        Asserts.assertEquals(sut.size(), 1);
    }
}
