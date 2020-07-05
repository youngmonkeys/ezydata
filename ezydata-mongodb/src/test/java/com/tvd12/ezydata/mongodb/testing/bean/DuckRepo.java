package com.tvd12.ezydata.mongodb.testing.bean;

import java.util.List;

import com.tvd12.ezydata.database.annotation.EzyQuery;
import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;
import com.tvd12.ezyfox.util.Next;

@EzyAutoImpl
public interface DuckRepo extends EzyMongoRepository<DuckId, Duck> {
	
	@EzyQuery("{_id : ?0}")
	Duck findDuckById(DuckId id);
	
	@EzyQuery("{$query: {age: {$gte : ?0}}, $orderby : {age: -1}}")
	List<Duck> findListByAge(int age, Next next);
	
	@EzyQuery("{age: {$gte : ?0}}")
	List<Duck> findListByAge2(int age);
	
	@EzyQuery("{$query: {age: {$gte : ?0}}}")
	int countByAge(int age, Next next);
	
	@EzyQuery("{}")
	void updateByAge();
	
	@EzyQuery("{age : ?0}")
	int deleteByAge(int age);
	
	@EzyQuery("{$query: {age : ?0}}")
	int deleteByAge2(int age);
	
}
