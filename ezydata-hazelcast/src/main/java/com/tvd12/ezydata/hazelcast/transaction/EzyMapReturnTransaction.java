package com.tvd12.ezydata.hazelcast.transaction;

import com.hazelcast.transaction.TransactionalMap;

public interface EzyMapReturnTransaction<K,V,R> 
        extends EzyReturnTransaction<TransactionalMap<K, V>, R> {

}
