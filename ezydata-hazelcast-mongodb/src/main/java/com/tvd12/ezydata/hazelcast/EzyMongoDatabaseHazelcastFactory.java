package com.tvd12.ezydata.hazelcast;

import com.mongodb.client.MongoDatabase;
import com.tvd12.ezydata.hazelcast.EzyAbstractHazelcastFactory;
import com.tvd12.ezydata.hazelcast.mapstore.EzyMapstoreCreator;
import com.tvd12.ezydata.hazelcast.mapstore.EzyMongoDatabaseMapstoreCreator;
import com.tvd12.ezydata.mongodb.EzyMongoDatabaseAware;

import lombok.Setter;

@Setter
public class EzyMongoDatabaseHazelcastFactory 
		extends EzyAbstractHazelcastFactory 
		implements EzyMongoDatabaseAware {
	
	protected MongoDatabase database;
	
	@Override
	protected EzyMapstoreCreator newMapstoreCreator() {
		EzyMongoDatabaseMapstoreCreator creator = newDatabaseMapstoreCreator();
		creator.setDatabase(database);
		return creator;
	}

	protected EzyMongoDatabaseMapstoreCreator newDatabaseMapstoreCreator() {
		return new EzyMongoDatabaseMapstoreCreator(); 
	}
	
}
