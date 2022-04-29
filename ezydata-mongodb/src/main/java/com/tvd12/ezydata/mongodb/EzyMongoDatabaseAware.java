package com.tvd12.ezydata.mongodb;

import com.mongodb.client.MongoDatabase;

public interface EzyMongoDatabaseAware {

    void setDatabase(MongoDatabase database);

}
