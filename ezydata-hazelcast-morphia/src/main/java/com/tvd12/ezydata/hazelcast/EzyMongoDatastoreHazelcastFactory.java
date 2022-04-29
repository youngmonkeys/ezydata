package com.tvd12.ezydata.hazelcast;

import com.tvd12.ezydata.hazelcast.EzyMongoDatabaseHazelcastFactory;
import com.tvd12.ezydata.hazelcast.mapstore.EzyMongoDatabaseMapstoreCreator;
import com.tvd12.ezydata.hazelcast.mapstore.EzyMongoDatastoreMapstoreCreator;
import com.tvd12.ezydata.morphia.EzyDatastoreAware;

import dev.morphia.Datastore;
import lombok.Setter;

@Setter
public class EzyMongoDatastoreHazelcastFactory 
        extends EzyMongoDatabaseHazelcastFactory
        implements EzyDatastoreAware {

    protected Datastore datastore;

    @Override
    protected EzyMongoDatabaseMapstoreCreator newDatabaseMapstoreCreator() {
        EzyMongoDatastoreMapstoreCreator creator = new EzyMongoDatastoreMapstoreCreator();
        creator.setDatastore(datastore);
        return creator;
    }

}
