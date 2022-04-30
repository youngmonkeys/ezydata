package com.tvd12.ezydata.hazelcast.mapstore;

import com.hazelcast.map.MapStore;
import com.tvd12.ezydata.morphia.EzyDatastoreAware;
import dev.morphia.Datastore;
import lombok.Setter;

import java.util.Properties;

public class EzyMongoDatastoreMapstoreCreator
    extends EzyMongoDatabaseMapstoreCreator
    implements EzyDatastoreAware {

    @Setter
    protected Datastore datastore;

    @SuppressWarnings("rawtypes")
    @Override
    public MapStore create(String mapName, Properties properties) {
        MapStore mapstore = super.create(mapName, properties);
        if (mapstore instanceof EzyDatastoreAware) {
            ((EzyDatastoreAware) mapstore).setDatastore(datastore);
        }
        return mapstore;
    }
}
