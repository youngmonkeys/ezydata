package com.tvd12.ezydata.mongodb.testing.bean;

import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;
import com.tvd12.ezyfox.database.annotation.EzyQuery;

@EzyAutoImpl
public interface PersonRepo extends EzyMongoRepository<Integer, Person> {

    @EzyQuery("{$query: {_id: ?0}}")
    Person findById(int id);

    @EzyQuery("{$query: {personName: ?0}}")
    Person findByName(String name);

}
