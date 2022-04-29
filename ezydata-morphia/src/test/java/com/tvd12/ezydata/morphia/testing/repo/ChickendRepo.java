package com.tvd12.ezydata.morphia.testing.repo;

import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezydata.morphia.testing.data.Chickend;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;

@EzyAutoImpl
public interface ChickendRepo extends EzyMongoRepository<Long, Chickend> {

}
