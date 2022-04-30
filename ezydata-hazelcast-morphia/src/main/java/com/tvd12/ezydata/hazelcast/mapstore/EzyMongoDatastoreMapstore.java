package com.tvd12.ezydata.hazelcast.mapstore;

import com.tvd12.ezydata.morphia.EzyDatastoreAware;
import dev.morphia.Datastore;
import lombok.Setter;

public abstract class EzyMongoDatastoreMapstore<K, V>
    extends EzyAbstractMapstore<K, V>
    implements EzyDatastoreAware {

    @Setter
    protected Datastore datastore;
}
