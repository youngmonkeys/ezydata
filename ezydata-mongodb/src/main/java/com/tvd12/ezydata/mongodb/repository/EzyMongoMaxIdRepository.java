package com.tvd12.ezydata.mongodb.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.tvd12.ezydata.database.repository.EzyMaxIdRepository;
import org.bson.*;

public class EzyMongoMaxIdRepository implements EzyMaxIdRepository {

    protected final MongoCollection<Document> collection;

    public EzyMongoMaxIdRepository(
        MongoCollection<Document> collection
    ) {
        this.collection = collection;
    }

    @Override
    public Long incrementAndGet(String key) {
        return incrementAndGet(key, 1);
    }

    @Override
    public Long incrementAndGet(String key, int delta) {
        BsonDocument filter = new BsonDocument();
        BsonValue bsonId = new BsonString(key);
        filter.put("_id", bsonId);
        BsonDocument updateValue = new BsonDocument();
        updateValue.put("value", new BsonInt32(delta));
        BsonDocument update = new BsonDocument();
        update.put("$inc", updateValue);
        FindOneAndUpdateOptions opts = new FindOneAndUpdateOptions()
            .upsert(true)
            .returnDocument(ReturnDocument.AFTER);
        Document result = collection.findOneAndUpdate(filter, update, opts);
        Object value = result != null ? result.get("value") : null;
        return value != null ? ((Number) value).longValue() : 0L;
    }
}
