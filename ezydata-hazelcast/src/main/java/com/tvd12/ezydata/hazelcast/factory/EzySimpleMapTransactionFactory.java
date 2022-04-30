package com.tvd12.ezydata.hazelcast.factory;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.transaction.TransactionContext;
import com.hazelcast.transaction.TransactionOptions;
import com.tvd12.ezydata.hazelcast.transaction.EzyMapApplyTransaction;
import com.tvd12.ezydata.hazelcast.transaction.EzyMapReturnTransaction;
import com.tvd12.ezydata.hazelcast.transaction.EzyTransactionOptions;
import com.tvd12.ezydata.hazelcast.transaction.impl.EzySimpleMapApplyTransaction;
import com.tvd12.ezydata.hazelcast.transaction.impl.EzySimpleMapReturnTransaction;

import static com.hazelcast.transaction.TransactionOptions.TransactionType.valueOf;

public class EzySimpleMapTransactionFactory implements EzyMapTransactionFactory {

    protected final HazelcastInstance hazelcastInstance;

    public EzySimpleMapTransactionFactory(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public <K, V> EzyMapApplyTransaction<K, V>
    newApplyTransaction(String mapName, EzyTransactionOptions options) {
        TransactionOptions txOptions = newHazelcastTransactionOptions(options);
        TransactionContext txCxt = hazelcastInstance.newTransactionContext(txOptions);
        return new EzySimpleMapApplyTransaction<>(txCxt, mapName);
    }

    @Override
    public <K, V, R> EzyMapReturnTransaction<K, V, R>
    newReturnTransaction(String mapName, EzyTransactionOptions options) {
        TransactionOptions txOptions = newHazelcastTransactionOptions(options);
        TransactionContext txCxt = hazelcastInstance.newTransactionContext(txOptions);
        return new EzySimpleMapReturnTransaction<>(txCxt, mapName);
    }

    private TransactionOptions newHazelcastTransactionOptions(EzyTransactionOptions options) {
        return new TransactionOptions()
            .setTimeout(options.getTimeout(), options.getTimeoutUnit())
            .setTransactionType(valueOf(options.getTransactionType().toString()))
            .setDurability(options.getDurability());
    }
}
