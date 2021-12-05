package com.tvd12.ezydata.mongodb.testing.bean;

import java.util.List;
import java.util.Optional;

import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;
import com.tvd12.ezyfox.database.annotation.EzyQuery;
import com.tvd12.ezyfox.util.Next;

@EzyAutoImpl
public interface FoodRepo extends EzyMongoRepository<Integer, Food> {
	
	@EzyQuery("[" + 
		"{ $sort: { qty: 1 }}," + 
		"{ $match: { category: 'cake', qty: 10  } }," + 
		"{ $sort: { type: -1 } }" + 
	"]")
	List<Food> fetchListMatch();
	
	@EzyQuery("[" + 
			"{ $sort: { qty: 1 }}," + 
			"{ $match: { category: 'cake', qty: 10  } }," + 
			"{ $sort: { type: -1 } }" + 
		"]")
	List<Food> fetchListMatch2(Next next);
	
	@EzyQuery("[" + 
			"{ $sort: { qty: 1 }}," + 
			"{ $match: { category: 'cake', qty: 10  } }," + 
			"{ $sort: { type: -1 } }" + 
		"]")
	Food fetchOneMatch();
	
	@EzyQuery("[" + 
            "{ $sort: { qty: 1 }}," + 
            "{ $match: { category: 'cake', qty: 10  } }," + 
            "{ $sort: { type: -1 } }" + 
        "]")
    Optional<Food> fetchOneMatchOptional();
	
	@EzyQuery("[" + 
			"{ $sort: { qty: 1 }}," + 
			"{ $match: { category: 'cake no one', qty: 10  } }," + 
			"{ $sort: { type: -1 } }" + 
		"]")
	Food fetchOneMatch2();
	
	@EzyQuery("{$query: {_id : 4}, $update: {$set: {category: ?0}}}")
	void updateCategory(String category);
	
	@EzyQuery("{_id : ?0}")
	void deleteById(int id);
	
	@EzyQuery("{_id : {$gt: ?0}}")
	int countById(int gtId);
	
}
