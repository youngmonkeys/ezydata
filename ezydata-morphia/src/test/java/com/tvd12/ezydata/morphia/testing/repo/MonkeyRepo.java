package com.tvd12.ezydata.morphia.testing.repo;

import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezydata.morphia.testing.data.Monkey;

public interface MonkeyRepo extends EzyMongoRepository<Long, Monkey> {

    void save2Monkey(Monkey monkey1, Monkey monkey2);

}
