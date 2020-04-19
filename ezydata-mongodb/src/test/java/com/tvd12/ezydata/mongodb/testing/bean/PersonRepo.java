package com.tvd12.ezydata.mongodb.testing.bean;

import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;

@EzyAutoImpl
public interface PersonRepo extends EzyMongoRepository<Integer, Person> {
	
//	@EzyQuery("{$query: {name: ?}, $sort : [{name: 1}]}")
//	Person findByName(String name);
	
}
