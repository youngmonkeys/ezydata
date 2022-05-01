package com.tvd12.ezydata.mongodb.util;

import org.bson.BsonDocument;
import org.bson.BsonValue;

public final class BsonDocuments {

    private BsonDocuments() {}

    public static void putIfNotNull(
        BsonDocument document,
        String key,
        BsonValue value
    ) {
        if (value != null) {
            document.put(key, value);
        }
    }
}
