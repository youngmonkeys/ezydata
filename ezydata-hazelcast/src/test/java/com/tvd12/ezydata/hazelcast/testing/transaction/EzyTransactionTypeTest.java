package com.tvd12.ezydata.hazelcast.testing.transaction;

import com.tvd12.ezydata.hazelcast.transaction.EzyTransactionType;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyTransactionTypeTest extends BaseTest {

    @Test
    public void test() {
        assert EzyTransactionType.TWO_PHASE.getId() == 1;
        assert EzyTransactionType.TWO_PHASE.getName().equals("TWO_PHASE");
        assert EzyTransactionType.valueOf(1) == EzyTransactionType.TWO_PHASE;
        assert EzyTransactionType.valueOf("TWO_PHASE") == EzyTransactionType.TWO_PHASE;
    }
}
