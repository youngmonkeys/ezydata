package com.tvd12.ezydata.example.jpa.repository;

import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.example.jpa.entity.Category;
import com.tvd12.ezyfox.database.annotation.EzyRepository;

@EzyRepository
public interface CategoryRepository extends EzyDatabaseRepository<Long, Category> {
	
    Category findByName(String name);
}