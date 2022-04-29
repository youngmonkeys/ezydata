package com.tvd12.ezydata.morphia;

import dev.morphia.Datastore;

public interface EzyDatastoreAware {

    void setDatastore(Datastore datastore);

}
