package com.tvd12.ezydata.database;

public interface EzyDatabaseRepositoryWrapper {

	EzyDatabaseRepositoryWrapper DEFAULT = repo -> repo;
	
	Object wrap(Object repository);
	
}
