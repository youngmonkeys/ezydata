package com.tvd12.ezydata.hazelcast.factory;

import com.tvd12.ezydata.hazelcast.transaction.EzyMapApplyTransaction;
import com.tvd12.ezydata.hazelcast.transaction.EzyMapReturnTransaction;
import com.tvd12.ezydata.hazelcast.transaction.EzyTransactionOptions;

public interface EzyMapTransactionFactory {

    <K, V> EzyMapApplyTransaction<K, V>
    newApplyTransaction(String mapName, EzyTransactionOptions options);

    <K, V, R> EzyMapReturnTransaction<K, V, R>
    newReturnTransaction(String mapName, EzyTransactionOptions options);

    default <K, V> EzyMapApplyTransaction<K, V> newApplyTransaction(String mapName) {
        return newApplyTransaction(mapName, EzyTransactionOptions.newInstance());
    }

    default <K, V, R> EzyMapReturnTransaction<K, V, R> newReturnTransaction(String mapName) {
        return newReturnTransaction(mapName, EzyTransactionOptions.newInstance());
    }
}
