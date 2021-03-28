package com.tvd12.kotlin.examples.mongo.repository;

import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezyfox.database.annotation.EzyRepository;
import com.tvd12.kotlin.examples.mongo.entity.Category;

@EzyRepository
public interface CategoryRepository extends EzyMongoRepository<Long, Category> {
	
    Category findByName(String name);
}