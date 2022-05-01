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

    public static void decorateIdValue(BsonDocument document) {
        if (!document.containsKey("id")) {
            try {
                if (document.containsKey("_id")) {
                    document.put("id", document.get("_id"));
                }
            } catch (Exception ignored) {
                // do nothing
            }
        }
    }
}
