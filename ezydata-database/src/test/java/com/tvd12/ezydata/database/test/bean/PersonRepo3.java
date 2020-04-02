package com.tvd12.ezydata.database.test.bean;

import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;

@EzyAutoImpl
interface PersonRepo3 extends EzyDatabaseRepository<Integer, Person> {
}
