package com.tvd12.ezydata.hazelcast.transaction;

import com.hazelcast.transaction.TransactionalMap;

public interface EzyMapApplyTransaction<K, V>
    extends EzyApplyTransaction<TransactionalMap<K, V>> {}
