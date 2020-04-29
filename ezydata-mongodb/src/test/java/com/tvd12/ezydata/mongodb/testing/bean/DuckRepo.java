package com.tvd12.ezydata.mongodb.testing.bean;

import com.tvd12.ezydata.database.annotation.EzyQuery;
import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;

@EzyAutoImpl
public interface DuckRepo extends EzyMongoRepository<DuckId, Duck> {
	
	@EzyQuery("{_id : ?0}")
	Duck findDuckById(DuckId id);
	
}
