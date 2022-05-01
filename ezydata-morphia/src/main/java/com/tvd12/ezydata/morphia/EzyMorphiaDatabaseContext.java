package com.tvd12.ezydata.morphia;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import dev.morphia.Datastore;

public interface EzyMorphiaDatabaseContext extends EzyDatabaseContext {

    Datastore getDatastore();
}
