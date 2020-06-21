package com.tvd12.ezydata.example.jpa.repo;

import com.tvd12.ezydata.database.EzyDatabaseRepository;
import com.tvd12.ezydata.database.annotation.EzyQuery;
import com.tvd12.ezydata.example.jpa.entity.User;
import com.tvd12.ezyfox.annotation.EzyAutoImpl;

@EzyAutoImpl
public interface UserRepo extends EzyDatabaseRepository<String, User> {
	
	@EzyQuery("select e from User e where e.email = ?0")
	User findByEmail(String email);
	
}
