package com.tvd12.ezydata.database.test.bean;

import com.tvd12.ezydata.database.EzyDatabaseContext;
import com.tvd12.ezydata.database.EzyDatabaseContextAware;
import com.tvd12.ezydata.database.test.DbRepository;
import com.tvd12.ezyfox.database.annotation.EzyRepository;

@SuppressWarnings("unused")
@EzyRepository
public class PersonRepo6 extends DbRepository<Integer, Person> implements EzyDatabaseContextAware {

    @Override
    public void setDatabaseContext(EzyDatabaseContext context) {}
}
