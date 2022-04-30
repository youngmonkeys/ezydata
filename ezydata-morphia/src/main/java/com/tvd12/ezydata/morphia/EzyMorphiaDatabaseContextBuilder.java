package com.tvd12.ezydata.morphia;

import com.tvd12.ezydata.database.EzyDatabaseContextBuilder;
import com.tvd12.ezydata.database.EzySimpleDatabaseContext;
import com.tvd12.ezydata.database.bean.EzyAbstractRepositoriesImplementer;
import com.tvd12.ezydata.morphia.bean.EzyMorphiaRepositoriesImplementer;

import dev.morphia.Datastore;

public class EzyMorphiaDatabaseContextBuilder 
        extends EzyDatabaseContextBuilder<EzyMorphiaDatabaseContextBuilder> {

    protected Datastore datastore;

    public EzyMorphiaDatabaseContextBuilder datastore(Datastore datastore) {
        this.datastore = datastore;
        return this;
    }

    @Override
    protected EzySimpleDatabaseContext newDatabaseContext() {
        EzySimpleMorphiaDatabaseContext context = new EzySimpleMorphiaDatabaseContext();
        context.setDatastore(datastore);
        return context;
    }

    @Override
    protected EzyAbstractRepositoriesImplementer newRepositoriesImplement() {
        return new EzyMorphiaRepositoriesImplementer();
    }

}
