package com.tvd12.ezydata.hazelcast.mapstore;

import com.mongodb.MongoClient;
import com.tvd12.ezydata.mongodb.EzyMongoClientAware;
import lombok.Setter;

public abstract class EzyMongoMapstore<K, V>
    extends EzyAbstractMapstore<K, V>
    implements EzyMongoClientAware {

    @Setter
    protected MongoClient mongoClient;
}
