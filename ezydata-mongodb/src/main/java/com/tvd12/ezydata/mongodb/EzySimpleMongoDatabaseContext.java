package com.tvd12.ezydata.mongodb;

import org.bson.BsonValue;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tvd12.ezydata.database.EzySimpleDatabaseContext;
import com.tvd12.ezydata.database.naming.EzyNameTranslator;
import com.tvd12.ezydata.database.query.EzyQLQuery;
import com.tvd12.ezydata.mongodb.converter.EzyMongoDataConverter;
import com.tvd12.ezydata.mongodb.query.EzyMongoQueryFactory;
import com.tvd12.ezydata.mongodb.reflect.EzyMongoObjectProxyProvider;
import com.tvd12.ezyfox.binding.EzyMarshaller;
import com.tvd12.ezyfox.binding.EzyUnmarshaller;
import com.tvd12.ezyfox.reflect.EzyObjectProxy;
import com.tvd12.ezyfox.reflect.EzyObjectProxyProvider;

import lombok.Getter;
import lombok.Setter;

@Setter
public class EzySimpleMongoDatabaseContext 
		extends EzySimpleDatabaseContext
		implements EzyMongoDatabaseContext {

	@Getter
	protected MongoClient client;
	@Getter
	protected MongoDatabase database;
	protected EzyMarshaller marshaller;
	protected EzyUnmarshaller unmarshaller;
	protected EzyMongoQueryFactory queryFactory;
	protected EzyMongoDataConverter dataConverter;
	@Getter
	protected EzyNameTranslator collectionNameTranslator;
	protected final EzyObjectProxyProvider objectProxyProvider;
	
	public EzySimpleMongoDatabaseContext() {
		this.objectProxyProvider = new EzyMongoObjectProxyProvider();
	}
	
	@Override
	public void close() {
		client.close();
	}
	
	@Override
	public <T> MongoCollection<T> 
			getCollection(String name, Class<T> documentType) {
		return database.getCollection(name, documentType);
	}
	
	@Override
	public EzyQLQuery.Builder newQueryBuilder() {
		return queryFactory.newQueryBuilder();
	}
	
	@Override
	public EzyObjectProxy getObjectProxy(Class<?> objectType) {
		return objectProxyProvider.getObjectProxy(objectType);
	}

	@Override
	public <T> T bsonValueToData(BsonValue value, Class<T> dataType) {
		Object data = dataConverter.bsonValueToData(value);
		return unmarshaller.unmarshal(data, dataType);
	}

	@Override
	public <T extends BsonValue> T dataToBsonValue(Object data) {
		Object mdata = marshaller.marshal(data);
		return dataConverter.dataToBsonValue(mdata);
	}
	
}
