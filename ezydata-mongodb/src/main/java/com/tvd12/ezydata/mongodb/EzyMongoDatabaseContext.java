package com.tvd12.ezydata.mongodb;

import org.bson.BsonValue;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.database.query.EzyQLQuery;
import com.tvd12.ezyfox.naming.EzyNameTranslator;
import com.tvd12.ezyfox.reflect.EzyObjectProxy;

public interface EzyMongoDatabaseContext extends EzyDatabaseContext {
	
	MongoClient getClient();
	
	MongoDatabase getDatabase();
	
	<T> MongoCollection<T> getCollection(String name, Class<T> documentType);

	EzyQLQuery.Builder newQueryBuilder();
	
	EzyObjectProxy getObjectProxy(Class<?> objectType);
	
	<T extends BsonValue> T dataToBsonValue(Object data);
	
	<T> T bsonValueToData(BsonValue value, Class<T> dataType);
	
	EzyNameTranslator getCollectionNameTranslator();
	
}
