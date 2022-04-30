package com.tvd12.ezydata.database.test.bean;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.database.EzyDatabaseContextAware;

public class MongoTemplate implements EzyDatabaseContextAware {

    @Override
    public void setDatabaseContext(EzyDatabaseContext context) {}
}
