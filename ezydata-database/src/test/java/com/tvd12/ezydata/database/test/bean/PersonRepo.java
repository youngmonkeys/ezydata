package com.tvd12.ezydata.database.test.bean;

import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;
import com.tvd12.ezyfox.database.annotation.EzyQuery;

@EzyAutoImpl
public interface PersonRepo extends EzyDatabaseRepository<Integer, Person> {

    @EzyQuery("select e from Person e")
    void findByName();

    @EzyQuery
    void findByName2();

    @EzyQuery(name = "hello", value = "select e from Person e")
    void findByName3();

}
