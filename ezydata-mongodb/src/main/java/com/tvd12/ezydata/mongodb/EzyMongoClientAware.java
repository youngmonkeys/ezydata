package com.tvd12.ezydata.mongodb;

import com.mongodb.MongoClient;

public interface EzyMongoClientAware {

    void setMongoClient(MongoClient mongoClient);
    
}
