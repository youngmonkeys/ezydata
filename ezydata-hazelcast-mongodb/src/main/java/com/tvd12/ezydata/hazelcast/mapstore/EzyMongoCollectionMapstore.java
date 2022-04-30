package com.tvd12.ezydata.hazelcast.mapstore;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public abstract class EzyMongoCollectionMapstore<K, V>
    extends EzyMongoDatabaseMapstore<K, V> {

    protected MongoCollection<Document> collection;

    @Override
    public void postInit() {
        super.postInit();
        this.collection = database.getCollection(getCollectionName());
    }

    protected abstract String getCollectionName();
}
