package com.tvd12.ezydata.hazelcast.transaction;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
public class EzySimpleTransactionOptions extends EzyTransactionOptions {

    protected long timeout = 60;
    protected TimeUnit timeoutUnit = TimeUnit.SECONDS;
    protected int durability = 1;
    protected EzyTransactionType transactionType = EzyTransactionType.TWO_PHASE;

    public EzySimpleTransactionOptions timeout(long timeout, TimeUnit timeUnit) {
        this.timeout = timeout;
        this.timeoutUnit = timeUnit;
        return this;
    }

    public EzySimpleTransactionOptions durability(int durability) {
        this.durability = durability;
        return this;
    }

    public EzySimpleTransactionOptions transactionType(EzyTransactionType type) {
        this.transactionType = type;
        return this;
    }
}
