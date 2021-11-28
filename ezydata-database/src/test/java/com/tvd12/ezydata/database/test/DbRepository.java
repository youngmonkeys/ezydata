package com.tvd12.ezydata.database.test;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.database.EzyDatabaseContextAware;

public class DbRepository<I,E> 
    extends BaseDbRepository<I, E>
    implements EzyDatabaseContextAware {
    
    @Override
    public void setDatabaseContext(EzyDatabaseContext context) {
    }
}
