package com.tvd12.ezydata.database;

import java.util.List;
import java.util.Map;

import com.tvd12.ezydata.database.query.EzyQueryEntity;
import com.tvd12.ezyfox.util.EzyCloseable;

@SuppressWarnings("rawtypes")
public interface EzyDatabaseContext extends EzyCloseable {

	<T> T getRepository(Class<T> repoType);
	
	Map<Class, EzyDatabaseRepository> getRepositories();
	
	EzyQueryEntity getQuery(String queryName);

	Object deserializeResult(Object result, Class<?> resultType);
	
	List deserializeResultList(Object result, Class<?> resultType);
	
	@Override
	default void close() {}
	
}
