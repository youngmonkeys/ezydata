package com.tvd12.ezydata.mongodb.testing.bean;

import java.util.List;

import com.tvd12.ezydata.database.annotation.EzyQuery;
import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;

@EzyAutoImpl
public interface FoodRepo extends EzyMongoRepository<Integer, Food> {
	
	@EzyQuery("[" + 
		"{ $sort: { qty: 1 }}," + 
		"{ $match: { category: 'cake', qty: 10  } }," + 
		"{ $sort: { type: -1 } }" + 
	"]")
	List<Food> fetchListMatch();
	
	@EzyQuery("{$query: {_id : 4}, $update: {$set: {category: ?0}}}")
	void updateCategory(String query);
	
	@EzyQuery("{_id : ?0}")
	void deleteById(int id);
	
}
