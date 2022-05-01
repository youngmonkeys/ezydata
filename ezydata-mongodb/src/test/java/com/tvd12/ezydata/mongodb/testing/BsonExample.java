package com.tvd12.ezydata.mongodb.testing;

import org.bson.BsonDocument;

public class BsonExample {

    public static void main(String[] args) {
        String json = "{'$cmd': 'find', $query: {name: \"dung\"}}";
        BsonDocument doc = BsonDocument.parse(json);
        System.out.println(doc);
    }
}
