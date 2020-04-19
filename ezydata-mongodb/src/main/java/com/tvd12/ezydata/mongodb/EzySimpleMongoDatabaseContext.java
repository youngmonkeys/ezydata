package com.tvd12.ezydata.mongodb;

import org.bson.BsonValue;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tvd12.ezydata.database.EzySimpleDatabaseContext;
import com.tvd12.ezydata.mongodb.converter.EzyMongoDataConverter;
import com.tvd12.ezyfox.binding.EzyMarshaller;
import com.tvd12.ezyfox.binding.EzyUnmarshaller;

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
	protected EzyMongoDataConverter dataConverter;
	
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
	public <T> T bsonValueToData(BsonValue value, Class<T> dataType) {
		Object data = dataConverter.bsonValueToData(value);
		return unmarshaller.unmarshal(data, dataType);
	}

	@Override
	public <T extends BsonValue> T dataToBsonValue(Object data) {
		return dataConverter.dataToBsonValue(marshaller.marshal(data));
	}
	
}
