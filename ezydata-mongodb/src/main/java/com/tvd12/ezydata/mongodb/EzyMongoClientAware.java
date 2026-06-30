package com.tvd12.ezydata.mongodb;

import com.mongodb.client.MongoClient;

public interface EzyMongoClientAware {

    void setMongoClient(MongoClient mongoClient);
}
