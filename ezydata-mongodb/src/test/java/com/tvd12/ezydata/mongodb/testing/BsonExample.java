package com.tvd12.ezydata.mongodb.testing;

import org.bson.BsonDocument;

public class BsonExample {

    public static void main(String[] args) throws Exception {
        String json = "{'$cmd': 'find', $query: {name: \"dung\"}}";
        BsonDocument doc = BsonDocument.parse(json);
        System.out.println(doc);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
//        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
//        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
//        System.out.println(objectMapper.readValue(json, Map.class));
    }

}
