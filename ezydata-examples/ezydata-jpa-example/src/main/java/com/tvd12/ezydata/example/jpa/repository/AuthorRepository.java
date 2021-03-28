package com.tvd12.ezydata.example.jpa.repository;

import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.example.jpa.entity.Author;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;

@EzyAutoImpl
public interface AuthorRepository extends EzyDatabaseRepository<Long, Author> {
}