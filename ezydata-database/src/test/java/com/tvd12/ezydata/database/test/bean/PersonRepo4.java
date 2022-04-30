package com.tvd12.ezydata.database.test.bean;

import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;

@SuppressWarnings("unused")
@EzyAutoImpl
public abstract class PersonRepo4 implements EzyDatabaseRepository<Integer, Person> {}
