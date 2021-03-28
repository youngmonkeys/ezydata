package com.tvd12.ezydata.example.mongo.repository;

import com.tvd12.ezydata.example.mongo.entity.Category;
import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezyfox.database.annotation.EzyRepository;

@EzyRepository
public interface CategoryRepository extends EzyMongoRepository<Long, Category> {
	
    Category findByName(String name);
}