package com.tvd12.ezydata.hazelcast.mapstore;

import com.mongodb.client.MongoDatabase;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseAware;
import lombok.Setter;

public abstract class EzyMongoDatabaseMapstore<K, V>
    extends EzyAbstractMapstore<K, V>
    implements EzyMongoDatabaseAware {

    @Setter
    protected MongoDatabase database;
}
