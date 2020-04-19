package com.tvd12.ezydata.mongodb;

import org.bson.BsonValue;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tvd12.ezydata.database.EzyDatabaseContext;

public interface EzyMongoDatabaseContext extends EzyDatabaseContext {
	
	MongoClient getClient();
	
	MongoDatabase getDatabase();
	
	<T> MongoCollection<T> getCollection(String name, Class<T> documentType);

	<T extends BsonValue> T dataToBsonValue(Object data);
	
	<T> T bsonValueToData(BsonValue value, Class<T> dataType);
	
}
