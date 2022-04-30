package com.tvd12.ezydata.hazelcast.transaction;

public interface EzyTransaction {

    void begin();

    void commit();

    void rollback();
}
