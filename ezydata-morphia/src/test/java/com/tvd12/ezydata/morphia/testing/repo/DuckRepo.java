package com.tvd12.ezydata.morphia.testing.repo;

import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezydata.morphia.testing.Duck;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;

@EzyAutoImpl
public interface DuckRepo extends EzyMongoRepository<Long, Duck> {}
