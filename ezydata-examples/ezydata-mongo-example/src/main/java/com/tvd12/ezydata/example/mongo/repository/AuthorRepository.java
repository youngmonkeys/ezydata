package com.tvd12.ezydata.example.mongo.repository;

import com.tvd12.ezydata.example.mongo.entity.Author;
import com.tvd12.ezydata.mongodb.EzyMongoRepository;
import com.tvd12.ezyfox.database.annotation.EzyRepository;

@EzyRepository
public interface AuthorRepository extends EzyMongoRepository<Long, Author> {
}