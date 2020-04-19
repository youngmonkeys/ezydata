package com.tvd12.ezydata.mongodb;

import com.mongodb.client.MongoCollection;

@SuppressWarnings("rawtypes")
public interface EzyMongoCollectionAware {

	void setCollection(MongoCollection collection);
	
}
